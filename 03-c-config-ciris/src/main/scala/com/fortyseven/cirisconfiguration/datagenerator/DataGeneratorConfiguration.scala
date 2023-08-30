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

package com.fortyseven.cirisconfiguration.datagenerator

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.common.configuration.*
import com.fortyseven.common.configuration.refinedTypes.*

private[datagenerator] final case class DataGeneratorConfiguration(
    kafka: DataGeneratorKafkaConfiguration,
    schemaRegistry: DataGeneratorSchemaRegistryConfiguration
) extends DataGeneratorConfigurationI

private[datagenerator] final case class DataGeneratorKafkaConfiguration(
    broker: DataGeneratorBrokerConfiguration,
    producer: DataGeneratorProducerConfiguration
) extends DataGeneratorKafkaConfigurationI

private[datagenerator] final case class DataGeneratorBrokerConfiguration(
    bootstrapServers: NonEmptyString
) extends DataGeneratorBrokerConfigurationI

private[datagenerator] final case class DataGeneratorProducerConfiguration(
    topicName: NonEmptyString,
    valueSerializerClass: NonEmptyString,
    maxConcurrent: PositiveInt,
    compressionType: KafkaCompressionType,
    commitBatchWithinSize: PositiveInt,
    commitBatchWithinTime: FiniteDuration
) extends DataGeneratorProducerConfigurationI

private[datagenerator] final case class DataGeneratorSchemaRegistryConfiguration(
    schemaRegistryUrl: NonEmptyString
) extends DataGeneratorSchemaRegistryConfigurationI
