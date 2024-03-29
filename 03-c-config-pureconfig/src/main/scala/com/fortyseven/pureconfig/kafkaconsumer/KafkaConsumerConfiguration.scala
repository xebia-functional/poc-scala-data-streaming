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

package com.fortyseven.pureconfig.kafkaconsumer

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.common.configuration.*
import com.fortyseven.common.configuration.refinedTypes.*
import com.fortyseven.pureconfig.*
import com.fortyseven.pureconfig.refinedTypesGivens.given

import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.derived

private[kafkaconsumer] case class KafkaConsumerConfiguration(
    broker: BrokerConfiguration,
    consumer: Option[ConsumerConfiguration],
    producer: Option[ProducerConfiguration]
) extends KafkaConsumerConfigurationI

object KafkaConsumerConfiguration:
  given ConfigReader[KafkaConsumerConfiguration] = ConfigReader.derived[KafkaConsumerConfiguration]

private[kafkaconsumer] case class ConsumerConfiguration(
    topicName: NonEmptyString,
    autoOffsetReset: KafkaAutoOffsetReset,
    groupId: NonEmptyString,
    maxConcurrent: PositiveInt
) extends KafkaConsumerConsumerConfigurationI

object ConsumerConfiguration:
  given ConfigReader[ConsumerConfiguration] = ConfigReader.derived[ConsumerConfiguration]

private[kafkaconsumer] case class BrokerConfiguration(brokerAddress: NonEmptyString)
    extends KafkaConsumerBrokerConfigurationI

object BrokerConfiguration:
  given ConfigReader[BrokerConfiguration] = ConfigReader.derived[BrokerConfiguration]

private[kafkaconsumer] case class ProducerConfiguration(
    topicName: NonEmptyString,
    valueSerializerClass: NonEmptyString,
    maxConcurrent: PositiveInt,
    compressionType: KafkaCompressionType,
    commitBatchWithinSize: PositiveInt,
    commitBatchWithinTime: FiniteDuration
) extends KafkaConsumerProducerConfigurationI

object ProducerConfiguration:
  given ConfigReader[ProducerConfiguration] = ConfigReader.derived[ProducerConfiguration]
