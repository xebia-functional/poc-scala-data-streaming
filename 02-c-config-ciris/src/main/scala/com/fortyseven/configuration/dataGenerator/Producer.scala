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

import cats.syntax.all.*
import ciris.refined.*
import ciris.{default, ConfigValue, Effect}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

private[dataGenerator] final case class Producer(
    bootstrapServers: NonEmptyString,
    propertyKey: NonEmptyString,
    propertyValue: NonEmptyString,
    schemaRegistryUrl: NonEmptyString,
    includeKey: Boolean,
    chunkSize: PosInt,
    timeOut: PosInt
  )

private[dataGenerator] object Producer:

  val config: ConfigValue[Effect, Producer] =
    (
      default("localhost:9092").as[NonEmptyString],
      default("value.serializer").as[NonEmptyString],
      default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString],
      default("http://localhost:8081").as[NonEmptyString],
      default(false).as[Boolean],
      default(1).as[PosInt],
      default(15).as[PosInt]
    ).parMapN(Producer.apply)
