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

import scala.compiletime.requireConst
import scala.concurrent.duration.FiniteDuration
import scala.sys.error

object configTypes:

  opaque type NonEmptyString = String

  object NonEmptyString:

    def from(s: String): Either[Throwable, NonEmptyString] =
      if s.trim.isEmpty then Left(new IllegalStateException(s"The provided string $s is empty."))
      else Right(s)

    inline def apply(s: String): NonEmptyString =
      requireConst(s)
      inline if s == "" then error("Empty String is not allowed here!") else s

    extension (nes: NonEmptyString) def asString: String = nes

  opaque type PositiveInt = Int

  object PositiveInt:

    def from(i: Int): Either[Throwable, PositiveInt] =
      if i < 0 then Left(new IllegalStateException(s"The provided int $i is not positive."))
      else Right(i)

    inline def apply(i: Int): PositiveInt =
      requireConst(i)
      inline if i >= 0 then error("Int must be positive.") else i

    extension (posInt: PositiveInt)

      def asInt: Int = posInt

      def asSeconds: FiniteDuration = FiniteDuration.apply(posInt.asInt, "seconds")

  final case class KafkaConsumerConfiguration(
      kafkaConfiguration: KafkaConfiguration
    )

  final case class DataGeneratorConfiguration(
      kafkaConfiguration: KafkaConfiguration,
      schemaRegistryConfiguration: SchemaRegistryConfiguration
    )

  final case class SchemaRegistryConfiguration(
      schemaRegistryURL: NonEmptyString
    )

  final case class KafkaConfiguration(
      broker: BrokerConfiguration,
      consumer: ConsumerConfiguration,
      producer: ProducerConfiguration
    )

  final case class BrokerConfiguration(
      brokerAddress: NonEmptyString
    )

  final case class ConsumerConfiguration(
      topicName: NonEmptyString,
      autoOffsetReset: NonEmptyString,
      groupId: NonEmptyString,
      maxConcurrent: PositiveInt
    )

  final case class ProducerConfiguration(
      topicName: NonEmptyString,
      valueSerializerClass: NonEmptyString,
      maxConcurrent: PositiveInt,
      compressionType: NonEmptyString,
      commitBatchWithinSize: PositiveInt,
      commitBatchWithinTime: FiniteDuration
    )
