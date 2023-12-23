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

import com.fortyseven.common.api.ConfigurationAPI
import com.fortyseven.common.configuration.refinedTypes.*

import ciris.*

class FlinkProcessorConfigurationLoader[F[_]: Async] extends ConfigurationAPI[F, FlinkProcessorConfiguration]:

  private def defaultConfig(): ConfigValue[Effect, FlinkProcessorConfiguration] =
    for
      kafkaBrokerAddress <- default(BrokerAddress.assume("localhost:9092")).as[BrokerAddress]
      kafkaConsumerTopicName <- default(TopicName.assume("input-topic-pp")).as[TopicName]
      kafkaConsumerAutoOffsetReset <- default(KafkaAutoOffsetReset.earliest).as[KafkaAutoOffsetReset]
      kafkaConsumerGroupId <- default(GroupId.assume("groupId")).as[GroupId]
      kafkaConsumerMaxConcurrent <- default(MaxConcurrent.assume(25)).as[MaxConcurrent]
      kafkaProducerTopicName <- default(TopicName.assume("output-topic")).as[TopicName]
      kafkaProducerValueSerializerClass <- default(
        ValueSerializerClass.assume("io.confluent.kafka.serializers.KafkaAvroSerializer")
      ).as[ValueSerializerClass]
      kafkaProducerMaxConcurrent <- default(MaxConcurrent.assume(Int.MaxValue)).as[MaxConcurrent]
      kafkaProducerCompressionType <- default(KafkaCompressionType.lz4).as[KafkaCompressionType]
      kafkaProducerCommitBatchWithinSize <- default(CommitBatchWithinSize.assume(10)).as[CommitBatchWithinSize]
      kafkaProducerCommitBatchWithinTime <- default(15.seconds).as[FiniteDuration]
      schemaRegistryUrl <- default(SchemaRegistryUrl.assume("http://localhost:8081")).as[SchemaRegistryUrl]
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
