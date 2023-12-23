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

package com.fortyseven.pureconfig

import cats.syntax.all.*

import com.fortyseven.common.configuration.*
import com.fortyseven.common.configuration.refinedTypes.*

import pureconfig.ConfigReader
import pureconfig.error.ExceptionThrown

/** It contains the givens for the types that pureConfig does not have automatic derivation.
  */
object refinedTypesGivens:

  given ConfigReader[KafkaAutoOffsetReset] = ConfigReader
    .fromString(KafkaAutoOffsetReset.from(_).leftMap(ExceptionThrown.apply))

  given ConfigReader[KafkaCompressionType] = ConfigReader
    .fromString(KafkaCompressionType.from(_).leftMap(ExceptionThrown.apply))

  given ConfigReader[SchemaRegistryUrl] = ConfigReader.fromString(
    SchemaRegistryUrl
      .either(_)
      .leftMap { error =>
        val throwable = new Throwable(error)
        ExceptionThrown(throwable)
      }
  )

  given ConfigReader[BootstrapServers] = ConfigReader
    .fromString(BootstrapServers.either(_).leftMap(error => ExceptionThrown(new Throwable(error))))

  given ConfigReader[TopicName] = ConfigReader
    .fromString(TopicName.either(_).leftMap(error => ExceptionThrown(new Throwable(error))))

  given ConfigReader[ValueSerializerClass] = ConfigReader
    .fromString(ValueSerializerClass.either(_).leftMap(error => ExceptionThrown(new Throwable(error))))

  given ConfigReader[MaxConcurrent] = ConfigReader[Int]
    .emap(MaxConcurrent.either(_).leftMap(error => ExceptionThrown(new Throwable(error))))

  given ConfigReader[CommitBatchWithinSize] = ConfigReader[Int]
    .emap(CommitBatchWithinSize.either(_).leftMap(error => ExceptionThrown(new Throwable(error))))

  given ConfigReader[BrokerAddress] = ConfigReader
    .fromString(BrokerAddress.either(_).leftMap(error => ExceptionThrown(new Throwable(error))))

  given ConfigReader[GroupId] = ConfigReader
    .fromString(GroupId.either(_).leftMap(error => ExceptionThrown(new Throwable(error))))

  given ConfigReader[AppName] = ConfigReader
    .fromString(AppName.either(_).leftMap(error => ExceptionThrown(new Throwable(error))))

  given ConfigReader[MasterUrl] = ConfigReader
    .fromString(MasterUrl.either(_).leftMap(error => ExceptionThrown(new Throwable(error))))

  given ConfigReader[Offset] = ConfigReader
    .fromString(Offset.either(_).leftMap(error => ExceptionThrown(new Throwable(error))))

end refinedTypesGivens
