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

package com.fortyseven.common.configuration

import scala.compiletime.codeOf
import scala.compiletime.constValue
import scala.compiletime.error
import scala.util.Try

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/** Before running the whole program or a part of it, the involved configuration must be loaded. Refining the types of
  * the configuration's values reduces the cardinality and helps catching an invalid configuration's value while
  * loading.
  *
  * __Factories of the refined types:__
  *
  * The companion objects of the types have two constructors: `from` and `apply`.
  *
  *   - `from` is used for unknown values and it catches an exception at runtime if the values are not valid.
  *
  *   - `apply` is used for ''magic numbers'' and it verifies the validity of the value at compile time using inlining.
  *
  * @see
  *   inline internals at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
  */
object refinedTypes:

  opaque type SchemaRegistryUrl = String :| Not[Empty]
  object SchemaRegistryUrl extends RefinedTypeOps[String, Not[Empty], SchemaRegistryUrl]

  opaque type BootstrapServers = String :| Not[Empty]
  object BootstrapServers extends RefinedTypeOps[String, Not[Empty], BootstrapServers]

  opaque type TopicName = String :| Not[Empty]

  object TopicName extends RefinedTypeOps[String, Not[Empty], TopicName]

  opaque type ValueSerializerClass = String :| Not[Empty]

  object ValueSerializerClass extends RefinedTypeOps[String, Not[Empty], ValueSerializerClass]

  opaque type MaxConcurrent = Int :| Positive
  object MaxConcurrent extends RefinedTypeOps[Int, Positive, MaxConcurrent]

  opaque type CommitBatchWithinSize = Int :| Positive

  object CommitBatchWithinSize extends RefinedTypeOps[Int, Positive, CommitBatchWithinSize]

  opaque type BrokerAddress = String :| Not[Empty]

  object BrokerAddress extends RefinedTypeOps[String, Not[Empty], BrokerAddress]

  opaque type AppName = String :| Not[Empty]

  object AppName extends RefinedTypeOps[String, Not[Empty], AppName]

  opaque type MasterUrl = String :| ValidURL
  object MasterUrl extends RefinedTypeOps[String, ValidURL, MasterUrl]

  opaque type GroupId = String :| Not[Empty]
  object GroupId extends RefinedTypeOps[String, Not[Empty], GroupId]

  opaque type Offset = String :| ForAll[Digit]
  object Offset extends RefinedTypeOps[String, ForAll[Digit], Offset]

  enum Format:
    case csv, delta, avro, parquet

  /** Set of allowed compression types for Kafka producers.
    *
    * @see
    *   [[https://docs.confluent.io/platform/current/installation/configuration/producer-configs.html#compression-type]]
    */
  enum KafkaCompressionType:

    case none, gzip, snappy, lz4, zstd

  /** Factory for [[KafkaCompressionType]] instances.
    */
  object KafkaCompressionType:

    /** Smart constructor for unknown strings at compile time.
      *
      * @param kafkaCompressionTypeCandidate
      *   An unknown string.
      * @return
      *   An Either with a Right KafkaCompressionType or a Left Throwable.
      */
    def from(kafkaCompressionTypeCandidate: String): Either[Throwable, KafkaCompressionType] =
      Try(valueOf(kafkaCompressionTypeCandidate)).toEither

    /** Smart constructor for known strings at compile time. Use this method and not [[from]] when working with fixed
      * values (''magic numbers'').
      *
      * @param kafkaCompressionType
      *   A known string.
      * @return
      *   A valid KafkaCompressionType or a compiler error.
      * @see
      *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
      */
    inline def apply(kafkaCompressionType: String): KafkaCompressionType = inline kafkaCompressionType match
      case "none" => none
      case "gzip" => gzip
      case "snappy" => snappy
      case "lz4" => lz4
      case "zstd" => zstd
      case _: String => error:
          codeOf(kafkaCompressionType) + " is invalid.\nValid values are none, gzip, snappy, lz4 and zstd."

  end KafkaCompressionType

  /** Set of allowed auto offset reset types for Kafka consumers.
    *
    * @see
    *   [[https://docs.confluent.io/platform/current/installation/configuration/consumer-configs.html#auto-offset-reset]]
    */
  enum KafkaAutoOffsetReset:

    case earliest, latest, none

  /** Factory for [[KafkaAutoOffsetReset]] instances.
    */
  object KafkaAutoOffsetReset:

    /** Smart constructor for unknown strings at compile time.
      *
      * @param kafkaAutoOffsetResetCandidate
      *   An unknown string.
      * @return
      *   An Either with a Right KafkaAutoOffsetReset or a Left IllegalStateException.
      */
    def from(kafkaAutoOffsetResetCandidate: String): Either[Throwable, KafkaAutoOffsetReset] =
      Try(valueOf(kafkaAutoOffsetResetCandidate)).toEither

    /** Smart constructor for known strings at compile time. Use this method and not [[from]] when working with fixed
      * values (''magic numbers'').
      *
      * @param kafkaAutoOffsetReset
      *   A known string.
      * @return
      *   A valid KafkaAutoOffsetReset or a compiler error.
      * @see
      *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
      */
    inline def apply(kafkaAutoOffsetReset: String): KafkaAutoOffsetReset = inline kafkaAutoOffsetReset match
      case "earliest" => earliest
      case "latest" => latest
      case "none" => none
      case _: String => error:
          codeOf(kafkaAutoOffsetReset) + " is invalid.\nValid values are earliest, latest and none."

  end KafkaAutoOffsetReset

  /** Type alias for String. The validation happens in the factory methods of the companion object.
    */

end refinedTypes
