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

package com.fortyseven.cirisconfiguration.flink

import cats.effect.kernel.Async

import scala.concurrent.duration.*

import com.fortyseven.cirisconfiguration.decoders.given
import com.fortyseven.common.api.ConfigurationAPI
import com.fortyseven.common.configuration.refinedTypes.*

import ciris.*

class FlinkProcessorConfigurationLoader[F[_]: Async] extends ConfigurationAPI[F, FlinkProcessorConfiguration]:

  private def defaultConfig(): ConfigValue[Effect, FlinkProcessorConfiguration] =
    for
      kafkaBrokerAddress <- default("localhost:9092").as[NonEmptyString]
      kafkaConsumerTopicName <- default("input-topic-pp").as[NonEmptyString]
      kafkaConsumerAutoOffsetReset <- default(KafkaAutoOffsetReset.earliest).as[KafkaAutoOffsetReset]
      kafkaConsumerGroupId <- default("groupId").as[NonEmptyString]
      kafkaConsumerMaxConcurrent <- default(25).as[PositiveInt]
      kafkaProducerTopicName <- default("output-topic").as[NonEmptyString]
      kafkaProducerValueSerializerClass <- default("io.confluent.kafka.serializers.KafkaAvroSerializer")
        .as[NonEmptyString]
      kafkaProducerMaxConcurrent <- default(Int.MaxValue).as[PositiveInt]
      kafkaProducerCompressionType <- default(KafkaCompressionType.lz4).as[KafkaCompressionType]
      kafkaProducerCommitBatchWithinSize <- default(10).as[PositiveInt]
      kafkaProducerCommitBatchWithinTime <- default(15.seconds).as[FiniteDuration]
      schemaRegistryUrl <- default("http://localhost:8081").as[NonEmptyString]
    yield FlinkProcessorConfiguration(
      KafkaConfiguration(
        broker = BrokerConfiguration(kafkaBrokerAddress),
        consumer = Some(ConsumerConfiguration(
          topicName = kafkaConsumerTopicName,
          autoOffsetReset = kafkaConsumerAutoOffsetReset,
          groupId = kafkaConsumerGroupId,
          maxConcurrent = kafkaConsumerMaxConcurrent
        )),
        producer = Some(ProducerConfiguration(
          topicName = kafkaProducerTopicName,
          valueSerializerClass = kafkaProducerValueSerializerClass,
          maxConcurrent = kafkaProducerMaxConcurrent,
          compressionType = kafkaProducerCompressionType,
          commitBatchWithinSize = kafkaProducerCommitBatchWithinSize,
          commitBatchWithinTime = kafkaProducerCommitBatchWithinTime
        ))
      ),
      SchemaRegistryConfiguration(schemaRegistryUrl)
    )

  override def load(): F[FlinkProcessorConfiguration] = defaultConfig().load[F]

end FlinkProcessorConfigurationLoader
