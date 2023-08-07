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

package com.fortyseven.coreheaders.configuration

import scala.compiletime.requireConst
import scala.concurrent.duration.FiniteDuration
import scala.sys.error

/**
 * Before running the whole program or a part of it, the involved configuration must be loaded. Refining the types of the configuration's values
 * reduces the cardinality and helps catching an invalid configuration's value while loading.
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

  /**
   * Set of allowed compression types for Kafka producers.
   *
   * @see
   *   [[https://docs.confluent.io/platform/current/installation/configuration/producer-configs.html#compression-type]]
   */
  enum KafkaCompressionType:

    case none, gzip, snappy, lz4, zstd

  /**
   * Factory for [[KafkaCompressionType]] instances.
   */
  object KafkaCompressionType:

    /**
     * Smart constructor for unknown strings at compile time.
     *
     * @param kafkaCompressionTypeCandidate
     *   An unknown string.
     * @return
     *   An Either with a Right KafkaCompressionType or a Left IllegalStateException.
     */
    def from(kafkaCompressionTypeCandidate: String): Either[Throwable, KafkaCompressionType] =
      if KafkaCompressionType.values.map(_.toString).contains(kafkaCompressionTypeCandidate)
      then Right(safeApply(kafkaCompressionTypeCandidate))
      else
        Left(
          new IllegalStateException(
            s"The provided value $kafkaCompressionTypeCandidate does not correspond with the valid values ${values.mkString("(", ",", ")")}"
          )
        )

    private def safeApply(kafkaCompressionType: String): KafkaCompressionType =
      kafkaCompressionType match
        case "none"   => none
        case "gzip"   => gzip
        case "snappy" => snappy
        case "lz4"    => lz4
        case "zstd"   => zstd

    /**
     * Smart constructor for known strings at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param kafkaCompressionType
     *   A known string.
     * @return
     *   A valid KafkaCompressionType or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(kafkaCompressionType: String): KafkaCompressionType =
      requireConst(kafkaCompressionType)
      inline if KafkaCompressionType.values.map(_.toString).contains(kafkaCompressionType)
      then safeApply(kafkaCompressionType)
      else error("The valid values are none, gzip, snappy, lz4 and zstd.")

  /**
   * Set of allowed auto offset reset types for Kafka consumers.
   *
   * @see
   *   [[https://docs.confluent.io/platform/current/installation/configuration/consumer-configs.html#auto-offset-reset]]
   */
  enum KafkaAutoOffsetReset:

    case Earliest, Latest, None

  /**
   * Factory for [[KafkaAutoOffsetReset]] instances.
   */
  object KafkaAutoOffsetReset:

    /**
     * Smart constructor for unknown strings at compile time.
     *
     * @param kafkaAutoOffsetResetCandidate
     *   An unknown string.
     * @return
     *   An Either with a Right KafkaAutoOffsetReset or a Left IllegalStateException.
     */
    def from(kafkaAutoOffsetResetCandidate: String): Either[Throwable, KafkaAutoOffsetReset] =
      if KafkaAutoOffsetReset.values.map(_.toString).contains(kafkaAutoOffsetResetCandidate)
      then Right(safeApply(kafkaAutoOffsetResetCandidate))
      else
        Left(
          new IllegalStateException(
            s"The provided value $kafkaAutoOffsetResetCandidate does not correspond with the valid values ${values.mkString("(", ",", ")")}"
          )
        )

    private def safeApply(kafkaAutoOffsetReset: String): KafkaAutoOffsetReset =
      kafkaAutoOffsetReset match
        case "Earliest" => Earliest
        case "Latest"   => Latest
        case "None"     => None

    /**
     * Smart constructor for known strings at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param kafkaAutoOffsetReset
     *   A known string.
     * @return
     *   A valid KafkaAutoOffsetReset or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(kafkaAutoOffsetReset: String): KafkaAutoOffsetReset =
      requireConst(kafkaAutoOffsetReset)
      inline if KafkaAutoOffsetReset.values.map(_.toString).contains(kafkaAutoOffsetReset)
      then safeApply(kafkaAutoOffsetReset)
      else error("The valid values are Earliest, Latest and None.")

  /**
   * Type alias for String. The validation happens in the factory methods of the companion object.
   */
  opaque type NonEmptyString = String

  /**
   * Factory for [[NonEmptyString]] instances.
   */
  object NonEmptyString:

    /**
     * Smart constructor for unknown strings at compile time.
     *
     * @param nonEmptyStringCandidate
     *   An unknown string.
     * @return
     *   An Either with a Right NonEmptyString or a Left IllegalStateException.
     */
    def from(nonEmptyStringCandidate: String): Either[Throwable, NonEmptyString] =
      if nonEmptyStringCandidate.trim.isEmpty
      then Left(new IllegalStateException(s"The provided string $nonEmptyStringCandidate is empty."))
      else Right(nonEmptyStringCandidate)

    /**
     * Smart constructor for known strings at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param nonEmptyString
     *   A known string.
     * @return
     *   A valid NonEmptyString or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(nonEmptyString: String): NonEmptyString =
      requireConst(nonEmptyString)
      inline if nonEmptyString == ""
      then error("Empty String is not allowed here.")
      else nonEmptyString

    extension (nonEmptyString: NonEmptyString)
      /**
       * Changes the type of the value from NonEmptyString to String.
       *
       * @return
       *   Value as String.
       */
      def asString: String = nonEmptyString

  /**
   * Type alias for Int. The validation happens in the factory methods of the companion object.
   */
  opaque type PositiveInt = Int

  /**
   * Factory for [[PositiveInt]] instances.
   */
  object PositiveInt:

    /**
     * Smart constructor for unknown integers at compile time.
     *
     * @param intCandidate
     *   An unknown integer.
     * @return
     *   An Either with a Right PositiveInt or a Left IllegalStateException.
     */
    def from(intCandidate: Int): Either[Throwable, PositiveInt] =
      if intCandidate < 0
      then Left(new IllegalStateException(s"The provided int $intCandidate is not positive."))
      else Right(intCandidate)

    /**
     * Smart constructor for known integers at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param int
     *   A known integer.
     * @return
     *   A valid PositiveInt or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(int: Int): PositiveInt =
      requireConst(int)
      inline if int >= 0
      then error("Int must be positive.")
      else int

    extension (int: PositiveInt)

      /**
       * Changes the type of the value from PositiveInt to Int.
       *
       * @return
       *   Value as Int.
       */
      def asInt: Int = int

      /**
       * Changes the type of the value from PositiveInt to FiniteDuration.
       *
       * @return
       *   Value as FiniteDuration in seconds.
       */
      def asSeconds: FiniteDuration = FiniteDuration.apply(int.asInt, "seconds")
