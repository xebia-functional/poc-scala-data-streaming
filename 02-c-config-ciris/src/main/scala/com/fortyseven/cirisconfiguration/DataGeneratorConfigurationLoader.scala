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

package com.fortyseven.cirisconfiguration

import scala.concurrent.duration.*

import cats.effect.kernel.Async
import ciris.*
import com.fortyseven.cirisconfiguration.CommonConfiguration.*
import com.fortyseven.cirisconfiguration.decoders.given
import com.fortyseven.coreheaders.ConfigurationLoaderHeader
import com.fortyseven.coreheaders.configuration.DataGeneratorConfiguration
import com.fortyseven.coreheaders.configuration.internal.*
import com.fortyseven.coreheaders.configuration.internal.types.*

final class DataGeneratorConfigurationLoader[F[_]: Async]
    extends ConfigurationLoaderHeader[F, DataGeneratorConfiguration]:

  lazy val config: ConfigValue[Effect, DataGeneratorConfiguration] =
    for
      brokerAddress         <- default(kafkaBrokerAddress).as[NonEmptyString]
      schemaRegistryUrl     <- default(schemaRegistryUrl).as[NonEmptyString]
      topicName             <- default("data-generator").as[NonEmptyString]
      valueSerializerClass  <- default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString]
      maxConcurrent         <- default(Int.MaxValue).as[PositiveInt]
      compressionType       <- default(KafkaCompressionType.lz4).as[KafkaCompressionType]
      commitBatchWithinSize <- default(1).as[PositiveInt]
      commitBatchWithinTime <- default(15.seconds).as[FiniteDuration]
    yield DataGeneratorConfiguration(
      KafkaConfiguration(
        broker = BrokerConfiguration(brokerAddress),
        consumer = None,
        producer = Some(
          ProducerConfiguration(
            topicName = topicName,
            valueSerializerClass = valueSerializerClass,
            maxConcurrent = maxConcurrent,
            compressionType = compressionType,
            commitBatchWithinSize = commitBatchWithinSize,
            commitBatchWithinTime = commitBatchWithinTime
          )
        )
      ),
      SchemaRegistryConfiguration(schemaRegistryUrl)
    )

  override def load(configurationPath: Option[String]): F[DataGeneratorConfiguration] = config.load[F]
