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

package com.fortyseven.configuration

import scala.concurrent.duration.*

import cats.effect.kernel.Async
import ciris.refined.*
import ciris.{default, ConfigValue, Effect}
import com.fortyseven.configuration.CommonConfiguration.KafkaAutoOffsetReset.given
import com.fortyseven.configuration.CommonConfiguration.KafkaCompressionType.given
import com.fortyseven.configuration.CommonConfiguration.*
import com.fortyseven.coreheaders.ConfigHeader
import com.fortyseven.coreheaders.config.KafkaConsumerConfig
import com.fortyseven.coreheaders.config.internal.KafkaConfig.*
import com.fortyseven.coreheaders.config.internal.SchemaRegistryConfig.*
import com.typesafe.config.ConfigFactory
import eu.timepit.refined.types.all.*
import eu.timepit.refined.types.string.NonEmptyString
import lt.dvim.ciris.Hocon
import lt.dvim.ciris.Hocon.{hoconAt, stringHoconDecoder}

final class KafkaConsumerConfiguration[F[_]: Async] extends ConfigHeader[F, KafkaConsumerConfig]:

  def config(hoconConfigPath: String = "kafkaConsumer.conf"): ConfigValue[Effect, KafkaConsumerConfig] =

    val hocon: Hocon.HoconAt = hoconAt(ConfigFactory.load(hoconConfigPath))("KafkaConf")

    for
      brokerAddress         <- hocon("BrokerConf.brokerAddress").as[String].default(kafkaBrokerAddress).as[NonEmptyString]
      sourceTopicName       <- hocon("ConsumerConf.topicName").as[String].default("data-generator").as[NonEmptyString]
      autoOffsetReset       <- hocon("ConsumerConf.autoOffsetReset")
                                 .as[String]
                                 .default(KafkaAutoOffsetReset.Earliest.toString).as[KafkaAutoOffsetReset]
      groupId               <- hocon("ConsumerConf.groupId").as[String].default("groupId").as[NonEmptyString]
      consumerMaxConcurrent <- hocon("ConsumerConf.maxConcurrent").as[String].map(_.toInt).default(-25).as[PosInt]
      sinkTopicName         <- hocon("ProducerConf.topicName").as[String].default("input-topic").as[NonEmptyString]
      valueSerializerClass  <- hocon("ProducerConf.valueSerializerClass")
                                 .as[String]
                                 .default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString]
      producerMaxConcurrent <-
        hocon("ProducerConf.maxConcurrent").as[String].map(_.toInt).default(Int.MaxValue).as[PosInt]
      compressionType       <- hocon("ProducerConf.compressionType")
                                 .as[String]
                                 .default(KafkaCompressionType.lz4.toString).as[KafkaCompressionType]
      commitBatchWithinSize <-
        hocon("ProducerConf.commitBatchWithinSize").as[String].map(_.toInt).default(10).as[PosInt]
      commitBatchWithinTime <- hocon("ProducerConf.commitBatchWithinTime")
                                 .as[String].map(_.toInt.seconds)
                                 .default(15.seconds).as[FiniteDuration]
    yield KafkaConsumerConfig(
      KafkaConf(
        broker = BrokerConf(brokerAddress.value),
        consumer = Some(
          ConsumerConf(
            topicName = sourceTopicName.value,
            autoOffsetReset = autoOffsetReset.toString,
            groupId = groupId.value,
            maxConcurrent = consumerMaxConcurrent.value
          )
        ),
        producer = Some(
          ProducerConf(
            topicName = sinkTopicName.value,
            valueSerializerClass = valueSerializerClass.value,
            maxConcurrent = producerMaxConcurrent.value,
            compressionType = compressionType.toString,
            commitBatchWithinSize = commitBatchWithinSize.value,
            commitBatchWithinTime = commitBatchWithinTime
          )
        )
      )
    )

  override def load: F[KafkaConsumerConfig] = config().load[F]
