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

package com.fortyseven.cirisconfiguration

import scala.concurrent.duration.*

import cats.effect.kernel.Async

import ciris.*
import com.fortyseven.cirisconfiguration.CommonConfiguration.*
import com.fortyseven.cirisconfiguration.configuration.KafkaConsumerConfiguration
import com.fortyseven.cirisconfiguration.configuration.internal.*
import com.fortyseven.cirisconfiguration.decoders.given
import com.fortyseven.common.api.ConfigurationAPI
import com.fortyseven.common.configuration.refinedTypes.*

final class KafkaConsumerConfigurationLoader[F[_]: Async] extends ConfigurationAPI[F, KafkaConsumerConfiguration]:

  lazy val config: ConfigValue[Effect, KafkaConsumerConfiguration] =
    for
      brokerAddress         <- default(kafkaBrokerAddress).as[NonEmptyString]
      sourceTopicName       <- default("data-generator").as[NonEmptyString]
      sinkTopicName         <- default("input-topic").as[NonEmptyString]
      autoOffsetReset       <- default(KafkaAutoOffsetReset.Earliest).as[KafkaAutoOffsetReset]
      groupId               <- default("groupId").as[NonEmptyString]
      valueSerializerClass  <- default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString]
      consumerMaxConcurrent <- default(25).as[PositiveInt]
      producerMaxConcurrent <- default(Int.MaxValue).as[PositiveInt]
      compressionType       <- default(KafkaCompressionType.lz4).as[KafkaCompressionType]
      commitBatchWithinSize <- default(10).as[PositiveInt]
      commitBatchWithinTime <- default(15.seconds).as[FiniteDuration]
    yield KafkaConsumerConfiguration(
      KafkaConfiguration(
        broker = BrokerConfiguration(brokerAddress),
        consumer = Some(
          ConsumerConfiguration(
            topicName = sourceTopicName,
            autoOffsetReset = autoOffsetReset,
            groupId = groupId,
            maxConcurrent = consumerMaxConcurrent
          )
        ),
        producer = Some(
          ProducerConfiguration(
            topicName = sinkTopicName,
            valueSerializerClass = valueSerializerClass,
            maxConcurrent = producerMaxConcurrent,
            compressionType = compressionType,
            commitBatchWithinSize = commitBatchWithinSize,
            commitBatchWithinTime = commitBatchWithinTime
          )
        )
      )
    )

  override def load(): F[KafkaConsumerConfiguration] = config.load[F]
