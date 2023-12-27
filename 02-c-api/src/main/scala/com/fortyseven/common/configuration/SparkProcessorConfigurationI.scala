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

import com.fortyseven.common.configuration.refinedTypes.NonEmptyString

trait SparkProcessorConfigurationI:
  val app: SparkProcessorApplicationConfigurationI
  val streaming: SparkProcessorStreamingConfigurationI
  val reader: SparkProcessorReaderConfigurationI
  val writer: SparkProcessorWriterConfigurationI

trait SparkProcessorApplicationConfigurationI:
  val appName: NonEmptyString
  val masterUrl: NonEmptyString

trait SparkProcessorStreamingConfigurationI:
  val backpressureEnabled: Boolean
  val blockInterval: FiniteDuration
  val stopGracefullyOnShutdown: Boolean

trait SparkProcessorReaderConfigurationI:
  val kafka: SparkProcessorKafkaConfigurationI

trait SparkProcessorWriterConfigurationI:
  val format: NonEmptyString

trait SparkProcessorKafkaConfigurationI:
  val bootstrapServers: NonEmptyString
  val topic: NonEmptyString
  val startingOffsets: NonEmptyString
  val endingOffsets: NonEmptyString
