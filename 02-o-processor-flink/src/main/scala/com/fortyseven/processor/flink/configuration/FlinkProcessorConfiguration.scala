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

package com.fortyseven.processor.flink.configuration

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.coreheaders.configuration.refinedTypes.*
import com.fortyseven.pureconfig.refinedTypesGivens.given
import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.*

final case class FlinkProcessorConfiguration(
    kafka: KafkaConfiguration,
    schemaRegistry: SchemaRegistryConfiguration
) derives ConfigReader

final case class KafkaConfiguration(
    broker: BrokerConfiguration,
    consumer: Option[ConsumerConfiguration],
    producer: Option[ProducerConfiguration]
) derives ConfigReader

final case class BrokerConfiguration(brokerAddress: NonEmptyString) derives ConfigReader

final case class ConsumerConfiguration(
    topicName: NonEmptyString,
    autoOffsetReset: KafkaAutoOffsetReset,
    groupId: NonEmptyString,
    maxConcurrent: PositiveInt
) derives ConfigReader

final case class ProducerConfiguration(
    topicName: NonEmptyString,
    valueSerializerClass: NonEmptyString,
    maxConcurrent: PositiveInt,
    compressionType: KafkaCompressionType,
    commitBatchWithinSize: PositiveInt,
    commitBatchWithinTime: FiniteDuration
) derives ConfigReader

final case class SchemaRegistryConfiguration(
    schemaRegistryURL: NonEmptyString
) derives ConfigReader
