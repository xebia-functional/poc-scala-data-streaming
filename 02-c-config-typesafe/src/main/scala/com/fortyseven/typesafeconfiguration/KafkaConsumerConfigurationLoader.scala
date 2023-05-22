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
import com.fortyseven.typesafeconfiguration.KafkaConfig.*
import com.typesafe.config.{Config, ConfigFactory}

private[typesafeconfiguration] class KafkaConsumerConfigurationLoader[F[_]: Async]
    extends ConfigHeader[F, KafkaConfiguration]:

  private val kafkaConfiguration: Config = ConfigFactory.load("kafka.conf").getConfig("KafkaConfiguration")

  private object BrokerConfigurationLoader:

    private val brokerConf: Config = kafkaConfiguration.getConfig("BrokerConfiguration")

    private val brokerAddress: Either[Throwable, NonEmptyString] =
      NonEmptyString.from(brokerConf.getString("brokerAddress"))

    val load: Either[Throwable, BrokerConfiguration] = for ba <- brokerAddress yield BrokerConfiguration(ba)

  private object ConsumerConfigurationLoader:

    private val consumerConf = kafkaConfiguration.getConfig("ConsumerConfiguration")

    private val topicName: Either[Throwable, NonEmptyString] = NonEmptyString.from(consumerConf.getString("topicName"))

    private val autoOffsetReset: Either[Throwable, NonEmptyString] =
      NonEmptyString.from(consumerConf.getString("autoOffsetReset"))

    private val groupId: Either[Throwable, NonEmptyString] = NonEmptyString.from(consumerConf.getString("groupId"))

    private val maxConcurrent: Either[Throwable, PositiveInt] = PositiveInt.from(consumerConf.getInt("maxConcurrent"))

    val load: Either[Throwable, ConsumerConfiguration] =
      for
        tn  <- topicName
        aor <- autoOffsetReset
        gi  <- groupId
        mc  <- maxConcurrent
      yield ConsumerConfiguration(tn, aor, gi, mc)

  private object ProducerConfigurationLoader:

    private val producerConf: Config = kafkaConfiguration.getConfig("ProducerConfiguration")

    private val topicName: Either[Throwable, NonEmptyString] =
      NonEmptyString.from(producerConf.getString("valueSerializerClass"))

    private val valueSerializerClass: Either[Throwable, NonEmptyString] =
      NonEmptyString.from(producerConf.getString("topicName"))

    private val maxConcurrent: Either[Throwable, PositiveInt] = PositiveInt.from(producerConf.getInt("maxConcurrent"))

    private val compressionType: Either[Throwable, NonEmptyString] =
      NonEmptyString.from(producerConf.getString("compressionType"))

    private val commitBatchWithinSize: Either[Throwable, PositiveInt] =
      PositiveInt.from(producerConf.getInt("commitBatchWithinSize"))

    private val commitBatchWithinTime: Either[Throwable, PositiveInt] =
      PositiveInt.from(producerConf.getInt("commitBatchWithinTime"))

    val load: Either[Throwable, ProducerConfiguration] =
      for
        tn   <- topicName
        vsc  <- valueSerializerClass
        mx   <- maxConcurrent
        ct   <- compressionType
        cbws <- commitBatchWithinSize
        cbwt <- commitBatchWithinTime
      yield ProducerConfiguration(tn, vsc, mx, ct, cbws, cbwt.asSeconds)

  override def load: F[KafkaConfiguration] =
    val eitherLoad: Either[Throwable, KafkaConfiguration] =
      for
        bc <- BrokerConfigurationLoader.load
        cc <- ConsumerConfigurationLoader.load
        pc <- ProducerConfigurationLoader.load
      yield KafkaConfiguration(bc, cc, pc)

    eitherLoad match
      case Right(value)    => Async.apply.pure(value)
      case Left(throwable) => Async.apply.raiseError(throwable)

object KafkaConsumerConfigurationLoader:

  def apply[F[_]: Async]: KafkaConsumerConfigurationLoader[F] = new KafkaConsumerConfigurationLoader[F]
