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

package com.fortyseven.configuration.kafka

import cats.effect.IO
import cats.effect.kernel.Async
import cats.syntax.all.*
import ciris.{ConfigValue, Effect}
import com.fortyseven.configuration.kafka.*
import com.fortyseven.coreheaders.config.{ConfigurationHeader, KafkaConfigurationHeader}

final case class KafkaConfiguration(
    brokerConfiguration: Broker,
    consumerConfiguration: Consumer,
    producerConfiguration: Producer,
    streamConfiguration: Stream
  ) extends KafkaConfigurationHeader

object KafkaConfiguration:

  val config: ConfigValue[Effect, KafkaConfiguration] = (
    Broker.config,
    Consumer.config,
    Producer.config,
    Stream.config
  ).parMapN(KafkaConfiguration.apply)
