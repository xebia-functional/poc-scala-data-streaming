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

package com.fortyseven.cirisconfiguration.datagenerator

import cats.effect.kernel.Async

import scala.concurrent.duration.*

import com.fortyseven.cirisconfiguration.decoders.given
import com.fortyseven.common.api.ConfigurationAPI
import com.fortyseven.common.configuration.refinedTypes.*

import ciris.*

final class DataGeneratorConfigurationLoader[F[_]: Async] extends ConfigurationAPI[F, DataGeneratorConfiguration]:

  private def defaultConfig(): ConfigValue[Effect, DataGeneratorConfiguration] =
    for
      kafkaBrokerBoostrapServers         <- default("localhost:9092").as[NonEmptyString]
      kafkaProducerTopicName             <- default("data-generator").as[NonEmptyString]
      kafkaProducerValueSerializerClass  <- default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString]
      kafkaProducerMaxConcurrent         <- default(Int.MaxValue).as[PositiveInt]
      kafkaProducerCompressionType       <- default(KafkaCompressionType.lz4).as[KafkaCompressionType]
      kafkaProducerCommitBatchWithinSize <- default(10).as[PositiveInt]
      kafkaProducerCommitBatchWithinTime <- default(15.seconds).as[FiniteDuration]
      schemaRegistryUrl                  <- default("http://localhost:8081").as[NonEmptyString]
    yield DataGeneratorConfiguration(
      kafka = DataGeneratorKafkaConfiguration(
        broker = DataGeneratorBrokerConfiguration(
          bootstrapServers = kafkaBrokerBoostrapServers
        ),
        producer = DataGeneratorProducerConfiguration(
          topicName = kafkaProducerTopicName,
          valueSerializerClass = kafkaProducerValueSerializerClass,
          maxConcurrent = kafkaProducerMaxConcurrent,
          compressionType = kafkaProducerCompressionType,
          commitBatchWithinSize = kafkaProducerCommitBatchWithinSize,
          commitBatchWithinTime = kafkaProducerCommitBatchWithinTime
        )
      ),
      schemaRegistry = DataGeneratorSchemaRegistryConfiguration(
        schemaRegistryUrl = schemaRegistryUrl
      )
    )

  override def load(): F[DataGeneratorConfiguration] = defaultConfig().load[F]
