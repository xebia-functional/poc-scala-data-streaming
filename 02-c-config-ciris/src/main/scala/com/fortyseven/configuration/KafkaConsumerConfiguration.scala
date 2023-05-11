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

import org.apache.kafka.common.record.CompressionType

import cats.effect.*
import cats.implicits.*
import ciris.refined.*
import ciris.{default, ConfigValue, Effect}
import com.fortyseven.configuration.CommonConfiguration.*
import com.fortyseven.coreheaders.ConfigHeader
import com.fortyseven.coreheaders.config.KafkaConsumerConfig
import com.fortyseven.coreheaders.config.internal.KafkaConfig.*
import com.fortyseven.coreheaders.config.internal.SchemaRegistryConfig.*
import eu.timepit.refined.types.all.*
import eu.timepit.refined.types.string.NonEmptyString
import fs2.kafka.AutoOffsetReset

final class KafkaConsumerConfiguration[F[_]: Async] extends ConfigHeader[F, KafkaConsumerConfig]:

  lazy val config: ConfigValue[Effect, KafkaConsumerConfig] =
    for
      brokerAddress         <- default(kafkaBrokerAddress).as[NonEmptyString]
      sourceTopicName       <- default("data-generator").as[NonEmptyString]
      sinkTopicName         <- default("input-topic").as[NonEmptyString]
      autoOffsetReset       <- default(AutoOffsetReset.Earliest).as[AutoOffsetReset]
      groupId               <- default("groupId").as[NonEmptyString]
      valueSerializerClass  <- default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString]
      consumerMaxConcurrent <- default(25).as[PosInt]
      producerMaxConcurrent <- default(Int.MaxValue).as[PosInt]
      compressionType       <- default(CompressionType.LZ4).as[CompressionType]
      commitBatchWithinSize <- default(10).as[PosInt]
      commitBatchWithinTime <- default(15.seconds).as[FiniteDuration]
    yield KafkaConsumerConfig(
      KafkaConf(
        broker = BrokerConf(brokerAddress.value),
        consumer = ConsumerConf(
          topicName = sourceTopicName.value,
          autoOffsetReset = autoOffsetReset.toString,
          groupId = groupId.value,
          maxConcurrent = consumerMaxConcurrent.value
        ).some,
        producer = ProducerConf(
          topicName = sinkTopicName.value,
          valueSerializerClass = valueSerializerClass.value,
          maxConcurrent = producerMaxConcurrent.value,
          compressionType = compressionType.name,
          commitBatchWithinSize = commitBatchWithinSize.value,
          commitBatchWithinTime = commitBatchWithinTime
        ).some
      )
    )

  override def load: F[KafkaConsumerConfig] = config.load[F]
