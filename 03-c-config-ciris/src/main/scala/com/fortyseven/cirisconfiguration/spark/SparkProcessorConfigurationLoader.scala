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

import scala.concurrent.duration.*

import cats.effect.kernel.Async

import ciris.*
import com.fortyseven.cirisconfiguration.decoders.given
import com.fortyseven.common.api.ConfigurationAPI
import com.fortyseven.common.configuration.refinedTypes.*

class SparkProcessorConfigurationLoader[F[_]: Async] extends ConfigurationAPI[F, SparkProcessorConfiguration]:

  private def defaultConfig(): ConfigValue[Effect, SparkProcessorConfiguration] =
    for
      appName                           <- default("spark-poc").as[NonEmptyString]
      appMasterUrl                      <- default("local[*]").as[NonEmptyString]
      streamingBackpressureEnabled      <- default(true).as[Boolean]
      streamingBlockInterval            <- default(200.seconds).as[FiniteDuration]
      streamingStopGracefullyOnShutdown <- default(true).as[Boolean]
      readerKafkaBootstrapServers       <- default("localhost:9092,broker:9093").as[NonEmptyString]
      readerKafkaTopic                  <- default("data-generator-*").as[NonEmptyString]
      readerKafkaStartingOffset         <- default("spark-poc").as[NonEmptyString]
      readerKafkaEndingOffset           <- default("earliest").as[NonEmptyString]
      writerFormat                      <- default("latest").as[NonEmptyString]
    yield SparkProcessorConfiguration(
      SparkProcessorApplicationConfiguration(
        appName = appName,
        masterUrl = appMasterUrl
      ),
      SparkProcessorStreamingConfiguration(
        backpressureEnabled = streamingBackpressureEnabled,
        blockInterval = streamingBlockInterval,
        stopGracefullyOnShutdown = streamingStopGracefullyOnShutdown
      ),
      SparkProcessorReaderConfiguration(
        kafkaConfiguration = SparkProcessorKafkaConfiguration(
          bootstrapServers = readerKafkaBootstrapServers,
          topic = readerKafkaTopic,
          startingOffset = readerKafkaStartingOffset,
          endingOffset = readerKafkaEndingOffset
        )
      ),
      SparkProcessorWriterConfiguration(
        format = writerFormat
      )
    )

  override def load(): F[SparkProcessorConfiguration] = defaultConfig().load[F]
