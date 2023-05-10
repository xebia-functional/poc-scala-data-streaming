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

package com.fortyseven.configuration.dataGenerator

import ciris.refined.*
import ciris.{default, ConfigValue, Effect}
import com.fortyseven.coreheaders.config.KafkaProducerHeader
import eu.timepit.refined.api.Refined
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

private[dataGenerator] final case class KafkaProducer(
    _bootstrapServers: NonEmptyString,
    _valueSerializerClass: NonEmptyString,
    _schemaRegistryUrl: NonEmptyString,
    includeKey: Boolean,
    _commitBatchWithinSize: PosInt,
    _commitBatchWithinTime: PosInt
  ) extends KafkaProducerHeader:

  override val bootstrapServers: String = _bootstrapServers.toString

  override val valueSerializerClass: String = _valueSerializerClass.toString

  override val schemaRegistryUrl: String = _schemaRegistryUrl.toString

  override val commitBatchWithinSize: Int = _commitBatchWithinSize.toString.toInt

  override val commitBatchWithinTime: Int = _commitBatchWithinTime.toString.toInt

private[dataGenerator] object KafkaProducer:

  val config: ConfigValue[Effect, KafkaProducer] =
    for
      _bootstrapServers      <- default("localhost:9092").as[NonEmptyString]
      _valueSerializerClass  <- default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString]
      _schemaRegistryUrl     <- default("http://localhost:8081").as[NonEmptyString]
      includeKey             <- default(false).as[Boolean]
      _commitBatchWithinSize <- default(1).as[PosInt]
      _commitBatchWithinTime <- default(15).as[PosInt]
    yield KafkaProducer.apply(
      _bootstrapServers,
      _valueSerializerClass,
      _schemaRegistryUrl,
      includeKey,
      _commitBatchWithinSize,
      _commitBatchWithinTime
    )
