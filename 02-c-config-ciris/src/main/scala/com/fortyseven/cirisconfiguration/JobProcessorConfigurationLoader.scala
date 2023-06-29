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

import ciris.{ConfigValue, Effect, default}
import com.fortyseven.cirisconfiguration.CommonConfiguration.{kafkaBrokerAddress, schemaRegistryUrl}
import com.fortyseven.cirisconfiguration.decoders.given
import com.fortyseven.coreheaders.ConfigurationLoaderHeader
import com.fortyseven.coreheaders.configuration.FlinkProcessorConfiguration
import com.fortyseven.coreheaders.configuration.internal.*
import com.fortyseven.coreheaders.configuration.internal.types.*

class JobProcessorConfigurationLoader[F[_]: Async] extends ConfigurationLoaderHeader[F, FlinkProcessorConfiguration]:

  lazy private val config: ConfigValue[Effect, FlinkProcessorConfiguration] =
    for
      brokerAddress          <- default(kafkaBrokerAddress).as[NonEmptyString]
      schemaRegistryUrl      <- default(schemaRegistryUrl).as[NonEmptyString]
      sourceTopicName        <- default("input-topic-pp").as[NonEmptyString]
      sinkTopicName          <- default("output-topic").as[NonEmptyString]
      autoOffsetReset        <- default(KafkaAutoOffsetReset.Earliest).as[KafkaAutoOffsetReset]
      groupId                <- default("groupId").as[NonEmptyString]
      valueSerializerClass   <- default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString]
      valueDeserializerClass <- default("io.confluent.kafka.serializers.KafkaAvroDeserializer").as[NonEmptyString]
      consumerMaxConcurrent  <- default(25).as[PositiveInt]
      producerMaxConcurrent  <- default(Int.MaxValue).as[PositiveInt]
      compressionType        <- default(KafkaCompressionType.lz4).as[KafkaCompressionType]
      commitBatchWithinSize  <- default(10).as[PositiveInt]
      commitBatchWithinTime  <- default(15.seconds).as[FiniteDuration]
    yield FlinkProcessorConfiguration(
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
      ),
      SchemaRegistryConfiguration(schemaRegistryUrl)
    )

  override def load(configurationPath: Option[String]): F[FlinkProcessorConfiguration] = config.load[F]
