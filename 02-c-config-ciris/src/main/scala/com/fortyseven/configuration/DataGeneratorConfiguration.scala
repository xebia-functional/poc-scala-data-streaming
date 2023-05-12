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
import com.fortyseven.configuration.CommonConfiguration.*
import com.fortyseven.coreheaders.ConfigHeader
import com.fortyseven.coreheaders.config.DataGeneratorConfig
import com.fortyseven.coreheaders.config.internal.KafkaConfig.*
import com.fortyseven.coreheaders.config.internal.SchemaRegistryConfig.*
import eu.timepit.refined.*
import eu.timepit.refined.auto.*
import eu.timepit.refined.types.all.*
import eu.timepit.refined.types.string.NonEmptyString

final class DataGeneratorConfiguration[F[_]: Async] extends ConfigHeader[F, DataGeneratorConfig]:

  lazy val config: ConfigValue[Effect, DataGeneratorConfig] =
    for
      brokerAddress         <- default(kafkaBrokerAddress).as[NonEmptyString]
      schemaRegistryUrl     <- default(schemaRegistryUrl).as[NonEmptyString]
      topicName             <- default("data-generator").as[NonEmptyString]
      valueSerializerClass  <- default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString]
      maxConcurrent         <- default(Int.MaxValue).as[PosInt]
      compressionType       <- default(KafkaCompressionType.lz4).as[KafkaCompressionType]
      commitBatchWithinSize <- default(1).as[PosInt]
      commitBatchWithinTime <- default(15.seconds).as[FiniteDuration]
    yield DataGeneratorConfig(
      KafkaConf(
        broker = BrokerConf(brokerAddress.value),
        consumer = None,
        producer = Some(
          ProducerConf(
            topicName = topicName.value,
            valueSerializerClass = valueSerializerClass.value,
            maxConcurrent = maxConcurrent.value,
            compressionType = compressionType.toString,
            commitBatchWithinSize = commitBatchWithinSize.value,
            commitBatchWithinTime = commitBatchWithinTime
          )
        )
      ),
      SchemaRegistryConf(schemaRegistryUrl.value)
    )

  override def load: F[DataGeneratorConfig] = config.load[F]
