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

package com.fortyseven.pureconfig.spark

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.common.configuration.*
import com.fortyseven.common.configuration.refinedTypes.*
import com.fortyseven.pureconfig.refinedTypesGivens.given

import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.derived

final private[spark] case class SparkProcessorConfiguration(
    app: SparkProcessorApplicationConfiguration,
    streaming: SparkProcessorStreamingConfiguration,
    reader: SparkProcessorReaderConfiguration,
    writer: SparkProcessorWriterConfiguration
) extends SparkProcessorConfigurationI

object SparkProcessorConfiguration:
  given ConfigReader[SparkProcessorConfiguration] = ConfigReader.derived[SparkProcessorConfiguration]

final private[spark] case class SparkProcessorApplicationConfiguration(
    appName: NonEmptyString,
    masterUrl: NonEmptyString
) extends SparkProcessorApplicationConfigurationI

object SparkProcessorApplicationConfiguration:

  given ConfigReader[SparkProcessorApplicationConfiguration] = ConfigReader
    .derived[SparkProcessorApplicationConfiguration]

final private[spark] case class SparkProcessorStreamingConfiguration(
    backpressureEnabled: Boolean,
    blockInterval: FiniteDuration,
    stopGracefullyOnShutdown: Boolean
) extends SparkProcessorStreamingConfigurationI

object SparkProcessorStreamingConfiguration:
  given ConfigReader[SparkProcessorStreamingConfiguration] = ConfigReader.derived[SparkProcessorStreamingConfiguration]

final private[spark] case class SparkProcessorReaderConfiguration(kafka: SparkProcessorKafkaConfiguration)
    extends SparkProcessorReaderConfigurationI

object SparkProcessorReaderConfiguration:
  given ConfigReader[SparkProcessorReaderConfiguration] = ConfigReader.derived[SparkProcessorReaderConfiguration]

final private[spark] case class SparkProcessorWriterConfiguration(format: NonEmptyString)
    extends SparkProcessorWriterConfigurationI

object SparkProcessorWriterConfiguration:
  given ConfigReader[SparkProcessorWriterConfiguration] = ConfigReader.derived[SparkProcessorWriterConfiguration]

final private[spark] case class SparkProcessorKafkaConfiguration(
    bootstrapServers: NonEmptyString,
    topic: NonEmptyString,
    startingOffsets: NonEmptyString,
    endingOffsets: NonEmptyString
) extends SparkProcessorKafkaConfigurationI

object SparkProcessorKafkaConfiguration:
  given ConfigReader[SparkProcessorKafkaConfiguration] = ConfigReader.derived[SparkProcessorKafkaConfiguration]
