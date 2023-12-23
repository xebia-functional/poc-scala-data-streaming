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

final private[flink] case class FlinkProcessorConfiguration(
    kafka: KafkaConfiguration,
    schemaRegistry: SchemaRegistryConfiguration
) extends FlinkProcessorConfigurationI

final private[flink] case class SchemaRegistryConfiguration(schemaRegistryUrl: SchemaRegistryUrl)
    extends FlinkProcessorSchemaRegistryConfigurationI

final private[flink] case class KafkaConfiguration(
    broker: BrokerConfiguration,
    consumer: Option[ConsumerConfiguration],
    producer: Option[ProducerConfiguration]
) extends FlinkProcessorKafkaConfigurationI

final private[flink] case class BrokerConfiguration(brokerAddress: BrokerAddress)
    extends FlinkProcessorBrokerConfigurationI

final private[flink] case class ConsumerConfiguration(
    topicName: TopicName,
    autoOffsetReset: KafkaAutoOffsetReset,
    groupId: GroupId,
    maxConcurrent: MaxConcurrent
) extends FlinkProcessorConsumerConfigurationI

final private[flink] case class ProducerConfiguration(
    topicName: TopicName,
    valueSerializerClass: ValueSerializerClass,
    maxConcurrent: MaxConcurrent,
    compressionType: KafkaCompressionType,
    commitBatchWithinSize: CommitBatchWithinSize,
    commitBatchWithinTime: FiniteDuration
) extends FlinkProcessorProducerConfigurationI
