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

package com.fortyseven.processor.spark.configuration

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.coreheaders.configuration.refinedTypes.NonEmptyString
import com.fortyseven.pureconfig.refinedTypesGivens.given_ConfigReader_NonEmptyString
import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.*

final case class ProcessorSparkConfiguration(app: AppConfiguration,
                                             streaming: StreamingConfiguration,
                                             reader: ReaderConfiguration,
                                             writer: WriterConfiguration
) derives ConfigReader

final case class AppConfiguration(appName: NonEmptyString, masterURL: NonEmptyString) derives ConfigReader

final case class StreamingConfiguration(backpressureEnabled: Boolean, blockInterval: FiniteDuration, stopGracefullyOnShutdown: Boolean)
    derives ConfigReader

final case class KafkaConfiguration(bootstrapServers: NonEmptyString,
                                    topic: NonEmptyString,
                                    startingOffsets: NonEmptyString,
                                    endingOffsets: NonEmptyString
) derives ConfigReader

final case class ReaderConfiguration(kafka: KafkaConfiguration) derives ConfigReader

final case class WriterConfiguration(format: NonEmptyString) derives ConfigReader
