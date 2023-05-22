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

package com.fortyseven.typesafeconfiguration

import cats.effect.kernel.Async
import com.fortyseven.coreheaders.ConfigHeader
import com.fortyseven.typesafeconfiguration.configTypes.*
import com.typesafe.config.{Config, ConfigFactory}

private [typesafeconfiguration] final class DataGeneratorConfigurationLoader[F[_]: Async] extends ConfigHeader[F, DataGeneratorConfiguration]:
  
  override def load: F[DataGeneratorConfiguration] =
    val eitherLoad: Either[Throwable, DataGeneratorConfiguration] =
      for
        kc <- KafkaConfigurationLoader.eitherLoad
        src <- SchemaRegistryConfigurationLoader.eitherLoad
      yield DataGeneratorConfiguration(kc, src)

    eitherLoad match
      case  Right(value) => Async.apply.pure(value)
      case Left(throwable) => Async.apply.raiseError(throwable)

object DataGeneratorConfigurationLoader:
  
  def apply[F[_]: Async]: DataGeneratorConfigurationLoader[F] = new DataGeneratorConfigurationLoader[F]