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

package com.fortyseven.common.configuration

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.common.configuration.refinedTypes.KafkaAutoOffsetReset
import com.fortyseven.common.configuration.refinedTypes.KafkaCompressionType
import com.fortyseven.common.configuration.refinedTypes.NonEmptyString
import com.fortyseven.common.configuration.refinedTypes.PositiveInt

trait FlinkProcessorConfigurationI:
  val kafka: FlinkProcessorKafkaConfigurationI
  val schemaRegistry: FlinkProcessorSchemaRegistryConfigurationI

trait FlinkProcessorKafkaConfigurationI:
  val broker: FlinkProcessorBrokerConfigurationI
  val consumer: Option[FlinkProcessorConsumerConfigurationI]
  val producer: Option[FlinkProcessorProducerConfigurationI]

trait FlinkProcessorBrokerConfigurationI:
  val brokerAddress: NonEmptyString

trait FlinkProcessorConsumerConfigurationI:
  val topicName: NonEmptyString
  val autoOffsetReset: KafkaAutoOffsetReset
  val groupId: NonEmptyString
  val maxConcurrent: PositiveInt

trait FlinkProcessorProducerConfigurationI:
  val topicName: NonEmptyString
  val valueSerializerClass: NonEmptyString
  val maxConcurrent: PositiveInt
  val compressionType: KafkaCompressionType
  val commitBatchWithinSize: PositiveInt
  val commitBatchWithinTime: FiniteDuration

trait FlinkProcessorSchemaRegistryConfigurationI:
  val schemaRegistryUrl: NonEmptyString
