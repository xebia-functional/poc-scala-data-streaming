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

import com.fortyseven.coreheaders.ConfigurationLoaderHeader
import com.fortyseven.coreheaders.FlinkProcessorHeader
import com.fortyseven.coreheaders.configuration.FlinkProcessorConfiguration
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

class DataProcessor[F[_]: Async] extends FlinkProcessorHeader[F]:

  override def process(conf: ConfigurationLoaderHeader[F, FlinkProcessorConfiguration]): F[Unit] = for
    kc <- conf.load()
    _ <- runWithConfiguration(kc)
  yield ()

  private def runWithConfiguration(jpc: FlinkProcessorConfiguration): F[Unit] =
    val env = StreamExecutionEnvironment.getExecutionEnvironment()

    env.setParallelism(Runtime.getRuntime.availableProcessors())
    env.getConfig.enableForceAvro()

    new FlinkDataProcessor(env).run(jpc).void
