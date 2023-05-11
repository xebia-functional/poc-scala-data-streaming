/*
 * Copyright 2023 Xebia Functional
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortyseven.kafkaconsumer

import scala.concurrent.duration.*

import org.apache.kafka.clients.producer.ProducerConfig

import cats.*
import cats.effect.kernel.Async
import cats.implicits.*
import com.fortyseven.coreheaders.config.KafkaConsumerConfig
import com.fortyseven.coreheaders.{ConfigHeader, KafkaConsumerHeader}
import fs2.kafka.*

final class KafkaConsumer[F[_]: Async] extends KafkaConsumerHeader[F]:

  override def consume(conf: ConfigHeader[F, KafkaConsumerConfig]): F[Unit] = for kc <- conf.load
  yield runWithConfiguration(kc)

  private def runWithConfiguration(kc: KafkaConsumerConfig): F[Unit] =

    val producerConfig = kc.kafkaConf.producer.getOrElse(
      throw new RuntimeException("No producer config available")
    )

    val consumerConfig = kc.kafkaConf.consumer.getOrElse(
      throw new RuntimeException("No consumer config available")
    )

    def processRecord(record: ConsumerRecord[String, String]): F[(String, String)] =
      Applicative[F].pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[F, String, String]
        .withAutoOffsetReset(consumerConfig.autoOffsetReset match {
          case "earliest" => AutoOffsetReset.Earliest
          case "latest" => AutoOffsetReset.Latest
          case _ => AutoOffsetReset.None
        })
        .withBootstrapServers(kc.kafkaConf.broker.brokerAddress)
        .withGroupId(consumerConfig.groupId)

    val producerSettings =
      ProducerSettings[F, String, String]
        .withBootstrapServers(kc.kafkaConf.broker.brokerAddress)
        .withProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, producerConfig.compressionType.getOrElse("none"))

    val stream =
      fs2.kafka.KafkaConsumer
        .stream(consumerSettings)
        .subscribeTo(consumerConfig.topicName)
        .records
        .mapAsync(consumerConfig.maxConcurrent.getOrElse(Int.MaxValue)) { committable =>
          processRecord(committable.record).map { case (key, value) =>
            val record = ProducerRecord(producerConfig.topicName, key, value)
            committable.offset -> ProducerRecords.one(record)
          }
        }.through { offsetsAndProducerRecords =>
          KafkaProducer.stream(producerSettings).flatMap { producer =>
            offsetsAndProducerRecords
              .evalMap { case (offset, producerRecord) =>
                producer.produce(producerRecord).map(_.as(offset))
              }.parEvalMap(producerConfig.maxConcurrent.getOrElse(Int.MaxValue))(identity)
          }
        }.through(
          commitBatchWithin(
            producerConfig.commitBatchWithinSize,
            producerConfig.commitBatchWithinTime
          )
        )

    stream.compile.drain
