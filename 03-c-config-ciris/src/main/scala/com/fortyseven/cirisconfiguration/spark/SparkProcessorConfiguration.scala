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

package com.fortyseven.cirisconfiguration.spark

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.common.configuration.*
import com.fortyseven.common.configuration.refinedTypes.NonEmptyString

private[spark] final case class SparkProcessorConfiguration(
    app: SparkProcessorApplicationConfiguration,
    streaming: SparkProcessorStreamingConfiguration,
    reader: SparkProcessorReaderConfiguration,
    writer: SparkProcessorWriterConfiguration
) extends SparkProcessorConfigurationI

private[spark] final case class SparkProcessorApplicationConfiguration(
    appName: NonEmptyString,
    masterUrl: NonEmptyString
) extends SparkProcessorApplicationConfigurationI

private[spark] final case class SparkProcessorStreamingConfiguration(
    backpressureEnabled: Boolean,
    blockInterval: FiniteDuration,
    stopGracefullyOnShutdown: Boolean
) extends SparkProcessorStreamingConfigurationI

private[spark] final case class SparkProcessorReaderConfiguration(
    kafkaConfiguration: SparkProcessorKafkaConfiguration
) extends SparkProcessorReaderConfigurationI

private[spark] final case class SparkProcessorWriterConfiguration(
    format: NonEmptyString
) extends SparkProcessorWriterConfigurationI

private[spark] final case class SparkProcessorKafkaConfiguration(
    bootstrapServers: NonEmptyString,
    topic: NonEmptyString,
    startingOffset: NonEmptyString,
    endingOffset: NonEmptyString
) extends SparkProcessorKafkaConfigurationI
