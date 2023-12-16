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

package com.fortyseven.pureconfig.flink

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.common.configuration.*
import com.fortyseven.common.configuration.refinedTypes.*
import com.fortyseven.pureconfig.refinedTypesGivens.given

import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.derived

final private[flink] case class FlinkProcessorConfiguration(
    kafka: KafkaConfiguration,
    schemaRegistry: SchemaRegistryConfiguration
) extends FlinkProcessorConfigurationI

object FlinkProcessorConfiguration:
  given ConfigReader[FlinkProcessorConfiguration] = ConfigReader.derived[FlinkProcessorConfiguration]

final private[flink] case class KafkaConfiguration(
    broker: BrokerConfiguration,
    consumer: Option[ConsumerConfiguration],
    producer: Option[ProducerConfiguration]
) extends FlinkProcessorKafkaConfigurationI

object KafkaConfiguration:
  given ConfigReader[KafkaConfiguration] = ConfigReader.derived[KafkaConfiguration]

final private[flink] case class BrokerConfiguration(brokerAddress: NonEmptyString)
    extends FlinkProcessorBrokerConfigurationI

object BrokerConfiguration:
  given ConfigReader[BrokerConfiguration] = ConfigReader.derived[BrokerConfiguration]

final private[flink] case class ConsumerConfiguration(
    topicName: NonEmptyString,
    autoOffsetReset: KafkaAutoOffsetReset,
    groupId: NonEmptyString,
    maxConcurrent: PositiveInt
) extends FlinkProcessorConsumerConfigurationI

object ConsumerConfiguration:
  given ConfigReader[ConsumerConfiguration] = ConfigReader.derived[ConsumerConfiguration]

final private[flink] case class ProducerConfiguration(
    topicName: NonEmptyString,
    valueSerializerClass: NonEmptyString,
    maxConcurrent: PositiveInt,
    compressionType: KafkaCompressionType,
    commitBatchWithinSize: PositiveInt,
    commitBatchWithinTime: FiniteDuration
) extends FlinkProcessorProducerConfigurationI

object ProducerConfiguration:
  given ConfigReader[ProducerConfiguration] = ConfigReader.derived[ProducerConfiguration]

final private[flink] case class SchemaRegistryConfiguration(schemaRegistryUrl: NonEmptyString)
    extends FlinkProcessorSchemaRegistryConfigurationI

object SchemaRegistryConfiguration:
  given ConfigReader[SchemaRegistryConfiguration] = ConfigReader.derived[SchemaRegistryConfiguration]
