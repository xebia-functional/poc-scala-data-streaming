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
import com.fortyseven.coreheaders.ConfigHeader
import com.fortyseven.typesafeconfiguration.configTypes.*
import com.typesafe.config.{Config, ConfigFactory}

private[typesafeconfiguration] final class KafkaConsumerConfigurationLoader[F[_]: Async]
    extends ConfigHeader[F, KafkaConsumerConfiguration]:

  override def load: F[KafkaConsumerConfiguration] =
    val eitherLoad =
      for kc <- KafkaConfigurationLoader.eitherLoad
      yield KafkaConsumerConfiguration(kc)

    eitherLoad match
      case Right(value)    => Async.apply.pure(value)
      case Left(throwable) => Async.apply.raiseError(throwable)

object KafkaConsumerConfigurationLoader:

  def apply[F[_]: Async]: KafkaConsumerConfigurationLoader[F] = new KafkaConsumerConfigurationLoader[F]
