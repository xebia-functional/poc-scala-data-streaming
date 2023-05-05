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

package com.fortyseven.config.kafka

import cats.syntax.all.*
import ciris.*
import ciris.refined.*
import com.fortyseven.config.kafka.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto.*
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.types.numeric.{PosInt, PosLong}
import eu.timepit.refined.types.string.NonEmptyString
import org.apache.kafka.common.record.CompressionType

/** Full list of configurations https://kafka.apache.org/documentation/
  * @param brokerConfiguration
  *   Contains the configuration for the Broker
  * @param topicConfiguration
  *   Contains the configuration for the Topic
  * @param producerConfiguration
  *   Contains the configuration for the Producer
  * @param consumerConfiguration
  *   Contains the configuration for the Consumer
  */
final case class KafkaConfiguration(
    brokerConfiguration: BrokerConfiguration,
    consumerConfiguration: ConsumerConfiguration,
    producerConfiguration: ProducerConfiguration,
    streamConfiguration: StreamConfiguration,
    topicConfiguration: TopicConfiguration
  )

object KafkaConfiguration:

  val config: ConfigValue[Effect, KafkaConfiguration] =
    (
      BrokerConfiguration.config,
      ConsumerConfiguration.config,
      ProducerConfiguration.config,
      StreamConfiguration.config,
      TopicConfiguration.config
    ).parMapN(KafkaConfiguration.apply)
