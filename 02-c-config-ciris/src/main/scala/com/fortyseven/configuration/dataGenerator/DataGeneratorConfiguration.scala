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

package com.fortyseven.configuration.dataGenerator

import cats.effect.Async
import cats.syntax.all.*
import ciris.{ConfigValue, Effect}
import com.fortyseven.coreheaders.config.ConfigurationHeader

case class DataGeneratorConfiguration (kafkaProducer: KafkaProducer)

object DataGeneratorConfiguration:
  val config: ConfigValue[Effect, DataGeneratorConfiguration] =
    KafkaProducer.config.map(DataGeneratorConfiguration.apply)

final class DataGeneratorConfigurationEffect[F[_] : Async] extends ConfigurationHeader[F, DataGeneratorConfiguration]:

  override def configuration: F[DataGeneratorConfiguration] = DataGeneratorConfiguration.config.load[F]