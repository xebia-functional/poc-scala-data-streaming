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

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.common.configuration.FlinkProcessorBrokerConfigurationI
import com.fortyseven.common.configuration.FlinkProcessorConfigurationI
import com.fortyseven.common.configuration.FlinkProcessorConsumerConfigurationI
import com.fortyseven.common.configuration.FlinkProcessorKafkaConfigurationI
import com.fortyseven.common.configuration.FlinkProcessorProducerConfigurationI
import com.fortyseven.common.configuration.FlinkProcessorSchemaRegistryConfigurationI
import com.fortyseven.common.configuration.refinedTypes.*

private[flink] final case class FlinkProcessorConfiguration(
    kafka: KafkaConfiguration,
    schemaRegistry: SchemaRegistryConfiguration
) extends FlinkProcessorConfigurationI

private[flink] final case class SchemaRegistryConfiguration(
    schemaRegistryUrl: NonEmptyString
) extends FlinkProcessorSchemaRegistryConfigurationI

private[flink] final case class KafkaConfiguration(
    broker: BrokerConfiguration,
    consumer: Option[ConsumerConfiguration],
    producer: Option[ProducerConfiguration]
) extends FlinkProcessorKafkaConfigurationI

private[flink] final case class BrokerConfiguration(
    brokerAddress: NonEmptyString
) extends FlinkProcessorBrokerConfigurationI

private[flink] final case class ConsumerConfiguration(
    topicName: NonEmptyString,
    autoOffsetReset: KafkaAutoOffsetReset,
    groupId: NonEmptyString,
    maxConcurrent: PositiveInt
) extends FlinkProcessorConsumerConfigurationI

private[flink] final case class ProducerConfiguration(
    topicName: NonEmptyString,
    valueSerializerClass: NonEmptyString,
    maxConcurrent: PositiveInt,
    compressionType: KafkaCompressionType,
    commitBatchWithinSize: PositiveInt,
    commitBatchWithinTime: FiniteDuration
) extends FlinkProcessorProducerConfigurationI
