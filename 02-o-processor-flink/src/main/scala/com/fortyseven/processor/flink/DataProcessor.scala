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

package com.fortyseven.processor.flink

import cats.*
import cats.effect.kernel.Async
import cats.implicits.*

import com.fortyseven.coreheaders.{ConfigurationHeader, FlinkProcessorHeader}
import com.fortyseven.processor.flink.configuration.FlinkProcessorConfiguration
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

class DataProcessor[F[_]: Async] extends FlinkProcessorHeader[F, FlinkProcessorConfiguration]:

  /**
   * @param configuration
   *   An instance of [[FlinkProcessorConfiguration]] class that extends [[ConfigurationHeader]].
   * @return
   *   It executes the effects of the Flink Processor and returns Unit.
   */
  override def process(configuration: ConfigurationHeader[F, FlinkProcessorConfiguration]): F[Unit] = for
    conf <- configuration.loadConfiguration()
    _    <- runWithConfiguration(conf)
  yield ()

  private def setAndGetEnvironment(): StreamExecutionEnvironment =
    val env = StreamExecutionEnvironment.getExecutionEnvironment()
    env.setParallelism(Runtime.getRuntime.availableProcessors())
    env.getConfig.enableForceAvro()
    env

  private def runWithConfiguration(jpc: FlinkProcessorConfiguration): F[Unit] =
    new FlinkDataProcessor(setAndGetEnvironment()).run(jpc).void
