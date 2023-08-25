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

import scala.util.matching.Regex

import cats.*
import cats.effect.kernel.Async
import cats.implicits.*

import com.fortyseven.common.api.ConfigurationAPI
import com.fortyseven.domain.configuration.refinedTypes.KafkaAutoOffsetReset
import com.fortyseven.input.api.KafkaConsumerAPI
import com.fortyseven.kafkaconsumer.configuration.KafkaConsumerConfiguration
import fs2.kafka.*
import org.apache.kafka.clients.producer.ProducerConfig

final class KafkaConsumer[F[_]: Async] extends KafkaConsumerAPI[F, KafkaConsumerConfiguration]:

  extension (kaor: KafkaAutoOffsetReset)
    def asKafka: AutoOffsetReset = kaor match
      case KafkaAutoOffsetReset.Earliest => AutoOffsetReset.Earliest
      case KafkaAutoOffsetReset.Latest   => AutoOffsetReset.Latest
      case KafkaAutoOffsetReset.None     => AutoOffsetReset.None

  override def consume(conf: ConfigurationAPI[F, KafkaConsumerConfiguration]): F[Unit] = for
    kc <- conf.load()
    _  <- runWithConfiguration(kc)
  yield ()

  private def runWithConfiguration(kc: KafkaConsumerConfiguration): F[Unit] =

    val producerConfig = kc.producer.getOrElse(
      throw new RuntimeException("No producer config available")
    )

    val consumerConfig = kc.consumer.getOrElse(
      throw new RuntimeException("No consumer config available")
    )

    def processRecord(record: ConsumerRecord[String, Array[Byte]]): F[(String, Array[Byte])] =
      Applicative[F].pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[F, String, Array[Byte]]
        .withAutoOffsetReset(consumerConfig.autoOffsetReset.asKafka)
        .withBootstrapServers(kc.broker.brokerAddress.asString)
        .withGroupId(consumerConfig.groupId.asString)

    val producerSettings =
      ProducerSettings[F, String, Array[Byte]]
        .withBootstrapServers(kc.broker.brokerAddress.asString)
        .withProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, producerConfig.compressionType.toString)

    val stream =
      fs2.kafka.KafkaConsumer
        .stream(consumerSettings)
        .subscribe(new Regex(s"${consumerConfig.topicName}-(.*)"))
        .records
        .mapAsync(consumerConfig.maxConcurrent.asInt) { committable =>
          def addTopicPrefix(sourceTopic: String, targetTopic: String): String =
            val suffix = sourceTopic.substring(sourceTopic.lastIndexOf("-"))
            s"$targetTopic$suffix"
          processRecord(committable.record).map { case (key, value) =>
            val record =
              ProducerRecord(addTopicPrefix(committable.record.topic, producerConfig.topicName.asString), key, value)
            committable.offset -> ProducerRecords.one(record)
          }
        }.through { offsetsAndProducerRecords =>
          KafkaProducer.stream(producerSettings).flatMap { producer =>
            offsetsAndProducerRecords
              .evalMap { case (offset, producerRecord) =>
                producer.produce(producerRecord).map(_.as(offset))
              }.parEvalMap(producerConfig.maxConcurrent.asInt)(identity)
          }
        }.through(
          commitBatchWithin(
            producerConfig.commitBatchWithinSize.asInt,
            producerConfig.commitBatchWithinTime
          )
        )

    stream.compile.drain
