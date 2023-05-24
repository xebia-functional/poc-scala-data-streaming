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

import scala.util.{Failure, Success, Try}

import com.fortyseven.coreheaders.ConfigurationLoaderHeader
import com.fortyseven.coreheaders.configuration.internal.*
import com.fortyseven.coreheaders.configuration.internal.types.*
import com.typesafe.config.ConfigException.Missing
import com.typesafe.config.{Config, ConfigException, ConfigFactory}

private[typesafeconfiguration] final class KafkaConfigurationLoader(
    className: String,
    configurationPath: Option[String] = None
  ):

  private val kafkaConfiguration: Config = {
    if configurationPath.isEmpty then ConfigFactory.load()
    else ConfigFactory.load(configurationPath.get)
  }.getConfig(className).getConfig("KafkaConfiguration")

  private object BrokerConfigurationLoader:

    private val brokerConf = kafkaConfiguration.getConfig("BrokerConfiguration")

    private val brokerAddress: Either[Throwable, NonEmptyString] =
      NonEmptyString.from(brokerConf.getString("brokerAddress"))

    val load: Either[Throwable, BrokerConfiguration] = for ba <- brokerAddress yield BrokerConfiguration(ba)

  private object ConsumerConfigurationLoader:

    val load: Either[Throwable, Option[ConsumerConfiguration]] =
      if Try(kafkaConfiguration.getConfig("ConsumerConfiguration")).isFailure then Right(None)
      else
        val consumerConf: Config                                     = kafkaConfiguration.getConfig("ConsumerConfiguration")
        val topicName: Either[Throwable, NonEmptyString]             = NonEmptyString.from(consumerConf.getString("topicName"))
        val autoOffsetReset: Either[Throwable, KafkaAutoOffsetReset] =
          KafkaAutoOffsetReset.from(consumerConf.getString("autoOffsetReset"))
        val groupId: Either[Throwable, NonEmptyString]               = NonEmptyString.from(consumerConf.getString("groupId"))
        val maxConcurrent: Either[Throwable, PositiveInt]            = PositiveInt.from(consumerConf.getInt("maxConcurrent"))

        for
          tn  <- topicName
          aor <- autoOffsetReset
          gi  <- groupId
          mc  <- maxConcurrent
        yield Some(ConsumerConfiguration(tn, aor, gi, mc))

  private object ProducerConfigurationLoader:

    val load: Either[Throwable, Option[ProducerConfiguration]] =
      if Try(kafkaConfiguration.getConfig("ProducerConfiguration")).isFailure then Right(None)
      else
        val producerConf: Config                                     = kafkaConfiguration.getConfig("ProducerConfiguration")
        val topicName: Either[Throwable, NonEmptyString]             = NonEmptyString.from(producerConf.getString("topicName"))
        val valueSerializerClass: Either[Throwable, NonEmptyString]  =
          NonEmptyString.from(producerConf.getString("valueSerializerClass"))
        val maxConcurrent: Either[Throwable, PositiveInt]            = PositiveInt.from(producerConf.getInt("maxConcurrent"))
        val compressionType: Either[Throwable, KafkaCompressionType] =
          KafkaCompressionType.from(producerConf.getString("compressionType"))
        val commitBatchWithinSize: Either[Throwable, PositiveInt]    =
          PositiveInt.from(producerConf.getInt("commitBatchWithinSize"))
        val commitBatchWithinTime: Either[Throwable, PositiveInt]    =
          PositiveInt.from(producerConf.getInt("commitBatchWithinTime"))

        for
          tn   <- topicName
          vsc  <- valueSerializerClass
          mx   <- maxConcurrent
          ct   <- compressionType
          cbws <- commitBatchWithinSize
          cbwt <- commitBatchWithinTime
        yield Some(ProducerConfiguration(tn, vsc, mx, ct, cbws, cbwt.asSeconds))

  val eitherLoad: Either[Throwable, KafkaConfiguration] =
    for
      bc <- BrokerConfigurationLoader.load
      cc <- ConsumerConfigurationLoader.load
      pc <- ProducerConfigurationLoader.load
    yield KafkaConfiguration(bc, cc, pc)

object KafkaConfigurationLoader:

  def apply(className: String, configurationPath: Option[String]): Either[Throwable, KafkaConfiguration] =
    new KafkaConfigurationLoader(className, configurationPath).eitherLoad
