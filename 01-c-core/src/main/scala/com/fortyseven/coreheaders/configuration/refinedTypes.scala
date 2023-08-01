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
 * NameSpace for the following instances:
 *
 *   - [[com.fortyseven.coreheaders.configuration.refinedTypes.KafkaCompressionType]]
 *
 *   - [[com.fortyseven.coreheaders.configuration.refinedTypes.KafkaAutoOffsetReset]]
 *
 *   - [[com.fortyseven.coreheaders.configuration.refinedTypes.NonEmptyString]]
 *
 *   - [[com.fortyseven.coreheaders.configuration.refinedTypes.PositiveInt]]
 */
object refinedTypes:

  enum KafkaCompressionType:

    case none, gzip, snappy, lz4, zstd

  /**
   * Factory for [[com.fortyseven.coreheaders.configuration.refinedTypes.KafkaCompressionType]] instances.
   */
  object KafkaCompressionType:

    /**
     * Tries to build a valid value of type KafkaCompressionType from the given string.
     * @param kafkaCompressionTypeCandidate
     *   the given String that could be a KafkaCompressionType.
     * @return
     *   Either a throwable of type IllegalStateException or a valid instance of type KafkaCompressionType.
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
     * Method that builds a KafkaCompressionType from a string at compile time. This method is used when working with fixed values (''magic numbers'')
     * in the code. The compiler will warn us if the value is invalid at compile time.
     * @param kafkaCompressionType
     *   the given String.
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

  enum KafkaAutoOffsetReset:

    case Earliest, Latest, None

  /**
   * Factory for [[com.fortyseven.coreheaders.configuration.refinedTypes.KafkaAutoOffsetReset]] instances.
   */
  object KafkaAutoOffsetReset:

    /**
     * Tries to build a valid value of type KafkaAutoOffsetReset from the given string.
     *
     * @param kafkaAutoOffsetResetCandidate
     *   the given String that could be a KafkaAutoOffsetReset.
     * @return
     *   Either a throwable of type IllegalStateException or a valid instance of type KafkaAutoOffsetReset.
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

    private def safeApply(kafkaAtoOffsetReset: String): KafkaAutoOffsetReset =
      kafkaAtoOffsetReset match
        case "Earliest" => Earliest
        case "Latest"   => Latest
        case "None"     => None

    /**
     * Method that builds a KafkaAutoOffsetReset from a string at compile time. This method is used when working with fixed values (''magic numbers'')
     * in the code. The compiler will warn us if the value is invalid at compile time.
     *
     * @param kafkaAutoOffsetReset
     *   the given String.
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

  opaque type NonEmptyString = String

  /**
   * Factory for [[com.fortyseven.coreheaders.configuration.refinedTypes.NonEmptyString]] instances.
   */
  object NonEmptyString:

    /**
     * Tries to build a valid value of type NonEmptyString from the given string.
     *
     * @param nonEmptyStringCandidate
     *   the given String that could be a NonEmptyString.
     * @return
     *   Either a throwable of type IllegalStateException or a valid instance of type NonEmptyString.
     */
    def from(nonEmptyStringCandidate: String): Either[Throwable, NonEmptyString] =
      if nonEmptyStringCandidate.trim.isEmpty
      then Left(new IllegalStateException(s"The provided string $nonEmptyStringCandidate is empty."))
      else Right(nonEmptyStringCandidate)

    /**
     * Method that builds a NonEmptyString from a string at compile time. This method is used when working with fixed values (''magic numbers'') in
     * the code. The compiler will warn us if the value is invalid at compile time.
     *
     * @param nonEmptyString
     *   the given String.
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
       * Casts the types while keeping the value.
       *
       * @return
       *   value as [[java.lang.String]].
       * @see
       *   More info at [[https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html]].
       */
      def asString: String = nonEmptyString

  opaque type PositiveInt = Int

  /**
   * Factory for [[com.fortyseven.coreheaders.configuration.refinedTypes.PositiveInt]] instances.
   */
  object PositiveInt:

    /**
     * Tries to build a valid value of type PositiveInt from the given int.
     *
     * @param positiveIntCandidate
     *   the given Int that could be a PositiveInt.
     * @return
     *   Either a throwable of type [[java.lang.IllegalStateException]] or a valid instance of type PositiveInt.
     */
    def from(positiveIntCandidate: Int): Either[Throwable, PositiveInt] =
      if positiveIntCandidate < 0
      then Left(new IllegalStateException(s"The provided int $positiveIntCandidate is not positive."))
      else Right(positiveIntCandidate)

    /**
     * Method that builds a PositiveInt from a int at compile time. This method is used when working with fixed values (''magic numbers'') in the
     * code. The compiler will warn us if the value is invalid at compile time.
     *
     * @param positiveInt
     *   the given Int.
     * @return
     *   A valid PositiveInt or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(positiveInt: Int): PositiveInt =
      requireConst(positiveInt)
      inline if positiveInt >= 0
      then error("Int must be positive.")
      else positiveInt

    extension (positiveInt: PositiveInt)

      /**
       * Casts the types while keeping the value.
       * @return
       *   Value as PositiveInt as [[scala.Int]] type.
       * @see
       *   More info at [[https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html]].
       */
      def asInt: Int = positiveInt

      /**
       * Casts the types while keeping the value.
       *
       * @return
       *   value as [[scala.concurrent.duration.FiniteDuration]] seconds.
       * @see
       *   More info at [[https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html]].
       */
      def asSeconds: FiniteDuration = FiniteDuration.apply(positiveInt.asInt, "seconds")
