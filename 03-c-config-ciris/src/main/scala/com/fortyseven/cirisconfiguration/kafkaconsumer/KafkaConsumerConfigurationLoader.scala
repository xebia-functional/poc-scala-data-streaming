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

package com.fortyseven.cirisconfiguration.kafkaconsumer

import cats.effect.kernel.Async

import scala.concurrent.duration.*

import com.fortyseven.cirisconfiguration.decoders.given
import com.fortyseven.common.api.ConfigurationAPI
import com.fortyseven.common.configuration.refinedTypes.*

import ciris.*

final class KafkaConsumerConfigurationLoader[F[_]: Async] extends ConfigurationAPI[F, KafkaConsumerConfiguration]:

  private def defaultConfig(): ConfigValue[Effect, KafkaConsumerConfiguration] =
    for
      brokerAddress <- default("localhost:9092").as[NonEmptyString]
      consumerTopicName <- default("data-generator").as[NonEmptyString]
      consumerAutoOffsetReset <- default(KafkaAutoOffsetReset.earliest).as[KafkaAutoOffsetReset]
      consumerGroupId <- default("groupId").as[NonEmptyString]
      consumerMaxConcurrent <- default(25).as[PositiveInt]
      producerTopicName <- default("input-topic").as[NonEmptyString]
      producerValueSerializerClass <- default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString]
      producerMaxConcurrent <- default(Int.MaxValue).as[PositiveInt]
      producerCompressionType <- default(KafkaCompressionType.lz4).as[KafkaCompressionType]
      producerCommitBatchWithinSize <- default(10).as[PositiveInt]
      producerCommitBatchWithinTime <- default(15.seconds).as[FiniteDuration]
    yield KafkaConsumerConfiguration(
      broker = BrokerConfiguration(brokerAddress),
      consumer = Some(ConsumerConfiguration(
        topicName = consumerTopicName,
        autoOffsetReset = consumerAutoOffsetReset,
        groupId = consumerGroupId,
        maxConcurrent = consumerMaxConcurrent
      )),
      producer = Some(ProducerConfiguration(
        topicName = producerTopicName,
        valueSerializerClass = producerValueSerializerClass,
        maxConcurrent = producerMaxConcurrent,
        compressionType = producerCompressionType,
        commitBatchWithinSize = producerCommitBatchWithinSize,
        commitBatchWithinTime = producerCommitBatchWithinTime
      ))
    )

  override def load(): F[KafkaConsumerConfiguration] = defaultConfig().load[F]

end KafkaConsumerConfigurationLoader
