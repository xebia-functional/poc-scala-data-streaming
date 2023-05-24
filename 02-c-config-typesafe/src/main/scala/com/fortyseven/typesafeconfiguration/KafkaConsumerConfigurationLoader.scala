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

import scala.concurrent.duration.FiniteDuration

import cats.effect.kernel.Async
import com.fortyseven.coreheaders.ConfigurationLoaderHeader
import com.fortyseven.coreheaders.configuration.KafkaConsumerConfiguration
import com.typesafe.config.{Config, ConfigFactory}

private[typesafeconfiguration] final class KafkaConsumerConfigurationLoader[F[_]: Async]
    extends ConfigurationLoaderHeader[F, KafkaConsumerConfiguration]:

  override def load(configurationPath: Option[String]): F[KafkaConsumerConfiguration] =
    val eitherLoad =
      for kc <- KafkaConfigurationLoader("KafkaConsumerConfiguration", configurationPath)
      yield KafkaConsumerConfiguration(kc)

    eitherLoad match
      case Right(value)    => Async.apply.pure(value)
      case Left(throwable) => Async.apply.raiseError(throwable)

object KafkaConsumerConfigurationLoader:

  def apply[F[_]: Async]: KafkaConsumerConfigurationLoader[F] = new KafkaConsumerConfigurationLoader[F]
