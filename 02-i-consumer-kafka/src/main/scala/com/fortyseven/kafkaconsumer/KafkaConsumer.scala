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
import scala.util.matching.Regex

import org.apache.kafka.clients.producer.ProducerConfig

import cats.*
import cats.effect.kernel.Async
import cats.implicits.*
import com.fortyseven.coreheaders.config.KafkaConsumerConfig
import com.fortyseven.coreheaders.config.internal.KafkaConfig.ConsumerConf
import com.fortyseven.coreheaders.{ConfigHeader, KafkaConsumerHeader}
import fs2.kafka.*

final class KafkaConsumer[F[_]: Async] extends KafkaConsumerHeader[F]:

  extension (cc: ConsumerConf)

    private def autoOffsetResetAsKafka: AutoOffsetReset = cc.autoOffsetReset match
      case "Earliest" => AutoOffsetReset.Earliest
      case "Latest"   => AutoOffsetReset.Latest
      case "None" | _ => AutoOffsetReset.None

  override def consume(conf: ConfigHeader[F, KafkaConsumerConfig]): F[Unit] = for
    kc <- conf.load
    _  <- runWithConfiguration(kc)
  yield ()

  private def runWithConfiguration(kc: KafkaConsumerConfig): F[Unit] =

    val producerConfig = kc.kafkaConf.producer.getOrElse(
      throw new RuntimeException("No producer config available")
    )

    val consumerConfig = kc.kafkaConf.consumer.getOrElse(
      throw new RuntimeException("No consumer config available")
    )

    def processRecord(record: ConsumerRecord[String, Array[Byte]]): F[(String, Array[Byte])] =
      Applicative[F].pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[F, String, Array[Byte]]
        .withAutoOffsetReset(consumerConfig.autoOffsetResetAsKafka)
        .withBootstrapServers(kc.kafkaConf.broker.brokerAddress)
        .withGroupId(consumerConfig.groupId)

    val producerSettings =
      ProducerSettings[F, String, Array[Byte]]
        .withBootstrapServers(kc.kafkaConf.broker.brokerAddress)
        .withProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, producerConfig.compressionType)

    val stream =
      fs2.kafka.KafkaConsumer
        .stream(consumerSettings)
        .subscribe(new Regex(s"${consumerConfig.topicName}-(.*)"))
        .records
        .mapAsync(consumerConfig.maxConcurrent) { committable =>
          def addTopicPrefix(sourceTopic: String, targetTopic: String): String =
            val suffix = sourceTopic.substring(sourceTopic.lastIndexOf("-"))
            s"$targetTopic$suffix"
          processRecord(committable.record).map { case (key, value) =>
            val record = ProducerRecord(addTopicPrefix(committable.record.topic, producerConfig.topicName), key, value)
            committable.offset -> ProducerRecords.one(record)
          }
        }.through { offsetsAndProducerRecords =>
          KafkaProducer.stream(producerSettings).flatMap { producer =>
            offsetsAndProducerRecords
              .evalMap { case (offset, producerRecord) =>
                producer.produce(producerRecord).map(_.as(offset))
              }.parEvalMap(producerConfig.maxConcurrent)(identity)
          }
        }.through(
          commitBatchWithin(
            producerConfig.commitBatchWithinSize,
            producerConfig.commitBatchWithinTime
          )
        )

    stream.compile.drain
