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
import com.fortyseven.coreheaders.KafkaConsumerHeader
import com.fortyseven.coreheaders.config.KafkaConfigurationHeader
import fs2.kafka.*

final class KafkaConsumer[F[_]: Async] extends KafkaConsumerHeader[F]:

  override def consume(kc: KafkaConfigurationHeader): F[Unit] = run(kc)

  def run(kc: KafkaConfigurationHeader): F[Unit] =
    def processRecord(record: ConsumerRecord[String, String]): F[(String, String)] =
      Applicative[F].pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[F, String, String]
        .withAutoOffsetReset(kc.consumerConfiguration.autoOffsetReset)
        .withBootstrapServers(kc.brokerConfiguration.brokerAddress)
        .withGroupId(kc.consumerConfiguration.groupId)

    val producerSettings =
      ProducerSettings[F, String, String]
        .withBootstrapServers(kc.brokerConfiguration.brokerAddress)
        .withProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, kc.producerConfiguration.compressionType.name)

    val stream =
      fs2.kafka.KafkaConsumer
        .stream(consumerSettings)
        .subscribeTo(kc.streamConfiguration.inputTopic)
        .records
        .mapAsync(kc.streamConfiguration.maxConcurrent) { committable =>
          processRecord(committable.record).map { case (key, value) =>
            val record = ProducerRecord(kc.streamConfiguration.outputTopic, key, value)
            committable.offset -> ProducerRecords.one(record)
          }
        }.through { offsetsAndProducerRecords =>
          KafkaProducer.stream(producerSettings).flatMap { producer =>
            offsetsAndProducerRecords
              .evalMap { case (offset, producerRecord) =>
                producer.produce(producerRecord).map(_.as(offset))
              }.parEvalMap(kc.producerConfiguration.maxConcurrent)(identity)
          }
        }.through(
          commitBatchWithin(
            kc.streamConfiguration.commitBatchWithinSize,
            kc.streamConfiguration.commitBatchWithinTime
          )
        )

    stream.compile.drain
