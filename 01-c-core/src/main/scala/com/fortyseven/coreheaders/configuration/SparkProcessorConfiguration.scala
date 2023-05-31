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

package com.fortyseven.coreheaders.configuration

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.coreheaders.configuration.internal.types.NonEmptyString
import com.fortyseven.coreheaders.configuration.internal.{KafkaConfiguration, SchemaRegistryConfiguration}

final case class SparkProcessorConfiguration(
    applicationProperties: ApplicationPropertiesConfiguration,
    sparkStreaming: SparkStreamingConfiguration,
    readerConfiguration: ReaderConfiguration,
    writerConfiguration: WriterConfiguration
  )

final case class ApplicationPropertiesConfiguration(
    appName: NonEmptyString,
    masterURL: NonEmptyString
  )

final case class SparkStreamingConfiguration(
    backpressureEnabled: Boolean,
    blockInterval: FiniteDuration,
    stopGracefullyOnShutdown: Boolean
  )

final case class KafkaStreamConfiguration(
    bootstrapServers: NonEmptyString,
    topic: NonEmptyString,
    startingOffsets: NonEmptyString,
    endingOffsets: NonEmptyString
  )

final case class ReaderConfiguration(
    kafkaStreamConfiguration: KafkaStreamConfiguration
  )

final case class WriterConfiguration(
    format: NonEmptyString
  )
