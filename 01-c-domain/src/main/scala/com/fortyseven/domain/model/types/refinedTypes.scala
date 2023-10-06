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

package com.fortyseven.domain.model.types

import scala.compiletime.{codeOf, error}

import com.fortyseven.domain.model.iot.errors.*

/**
 * Contains the types of the business logic.
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

  opaque type Latitude = Double

  opaque type Longitude = Double

  opaque type Percentage = Double

  opaque type Speed = Double // Should be typed better. Meters/second or Km/h?

  opaque type Hz = Double // IS measure for frequency 1/60 Hz would be 1 RPM

  opaque type Bar = Double

  opaque type Meters = Int

  /**
   * Factory for [[Latitude]] instances.
   */
  object Latitude:

    inline val errorMessage = " invalid latitude value. Accepted coordinate values are between -90.0 and 90.0."

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param coordinateCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Latitude or a Left OutOfBoundsError.
     */
    def from(coordinateCandidate: Double): Either[OutOfBoundsError, Latitude] =
      if coordinateCandidate < -90.0 || coordinateCandidate > 90.0
      then Left(OutOfBoundsError(coordinateCandidate.toString + errorMessage))
      else Right(coordinateCandidate)

    /**
     * Smart constructor for known doubles at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param coordinate
     *   A known double.
     * @return
     *   A valid Latitude or a compiler error.
     * @see
     *   [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(coordinate: Double): Latitude =
      inline if coordinate < -90.0 || coordinate > 90.0
      then error(codeOf(coordinate) + errorMessage)
      else coordinate

    given Conversion[Latitude, Double] with
      override def apply(x: Latitude): Double = x

  /**
   * Factory for [[Longitude]] instances.
   */
  object Longitude:

    inline val errorMessage = " invalid longitude value. Accepted coordinate values are between -180.0 and 180.0."

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param coordinateCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Longitude or a Left OutOfBoundsError.
     */
    def from(coordinateCandidate: Double): Either[OutOfBoundsError, Longitude] =
      if coordinateCandidate < -180.0 || coordinateCandidate > 180.0
      then Left(OutOfBoundsError(coordinateCandidate.toString + errorMessage))
      else Right(coordinateCandidate)

    /**
     * Smart constructor for known doubles at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param coordinate
     *   A known double.
     * @return
     *   A valid Longitude or a compiler error.
     * @see
     *   [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(coordinate: Double): Longitude =
      inline if coordinate < -180.0 || coordinate > 180.0
      then error(codeOf(coordinate) + errorMessage)
      else coordinate

    given Conversion[Longitude, Double] with
      override def apply(x: Longitude): Double = x

  /**
   * Factory for [[Percentage]] instances.
   */
  object Percentage:

    inline val errorMessage = " invalid percentage value. Accepted percentage values are between -0.0 and 100.0."

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param percentageCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Percentage or a Left OutOfBoundsError.
     */
    def from(percentageCandidate: Double): Either[OutOfBoundsError, Percentage] =
      if percentageCandidate < 0.0 || percentageCandidate > 100.0
      then Left(OutOfBoundsError(percentageCandidate.toString + errorMessage))
      else Right(percentageCandidate)

    /**
     * Smart constructor for known doubles at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param percentage
     *   A known double.
     * @return
     *   A valid Percentage or a compiler error.
     * @see
     *   [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(percentage: Double): Percentage =
      inline if percentage < 0.0 || percentage > 100.0
      then error(codeOf(percentage) + errorMessage)
      else percentage

  given Conversion[Percentage, Double] with
    override def apply(x: Percentage): Double = x

  /**
   * Factory for [[Speed]] instances.
   */
  object Speed:

    inline val errorMessage = " invalid speed value. Accepted speed values are greater than 0.0."

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param speedCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Speed or a Left OutOfBoundsError.
     */
    def from(speedCandidate: Double): Either[OutOfBoundsError, Speed] =
      if speedCandidate < 0.0
      then Left(OutOfBoundsError(speedCandidate.toString + errorMessage))
      else Right(speedCandidate)

    /**
     * Smart constructor for known doubles at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param speed
     *   A known double.
     * @return
     *   A valid Speed or a compiler error.
     * @see
     *   [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(speed: Double): Speed =
      inline if speed < 0.0
      then error(codeOf(speed) + errorMessage)
      else speed

    given Conversion[Speed, Double] with
      override def apply(x: Speed): Double = x

  /**
   * Factory for [[Hz]] instances.
   */
  object Hz:

    inline val errorMessage = " invalid frequency value. Accepted hertz values are greater than 0.0."

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param hertzCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Hz or a Left OutOfBoundsError.
     */
    def from(hertzCandidate: Double): Either[OutOfBoundsError, Hz] =
      if hertzCandidate < 0.0
      then Left(OutOfBoundsError(hertzCandidate.toString + errorMessage))
      else Right(hertzCandidate)

    /**
     * Smart constructor for known doubles at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param hertz
     *   A known double.
     * @return
     *   A valid Hz or a compiler error.
     * @see
     *   [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(hertz: Double): Hz =
      inline if hertz < 0.0
      then error(codeOf(hertz) + errorMessage)
      else hertz

    given Conversion[Hz, Double] with
      override def apply(x: Hz): Double = x

  /**
   * Factory for [[Bar]] instances.
   */
  object Bar:

    inline val errorMessage = " invalid pressure value. Accepted bar values are greater than 0.0."

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param barCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Bar or a Left OutOfBoundsError.
     */
    def from(barCandidate: Double): Either[OutOfBoundsError, Bar] =
      if barCandidate < 0.0
      then Left(OutOfBoundsError(barCandidate.toString + errorMessage))
      else Right(barCandidate)

    /**
     * Smart constructor for known doubles at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param bar
     *   A known double.
     * @return
     *   A valid Bar or a compiler error.
     * @see
     *   [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(bar: Double): Bar =
      inline if bar < 0.0
      then error(codeOf(bar) + errorMessage)
      else bar

  given Conversion[Bar, Double] with
    override def apply(x: Bar): Double = x

  /**
   * Factory for [[Meters]] instances.
   */
  object Meters:

    inline val errorMessage = " invalid meters value. Accepted bar values are greater than 0."

    /**
     * Smart constructor for unknown integers at compile time.
     *
     * @param metersCandidate
     *   An unknown integer.
     * @return
     *   An Either with a Right Meters or a Left OutOfBoundsError.
     */
    def from(metersCandidate: Int): Either[OutOfBoundsError, Meters] =
      if metersCandidate < 0
      then Left(OutOfBoundsError(metersCandidate.toString + errorMessage))
      else Right(metersCandidate)

    /**
     * Smart constructor for known integers at compile time. Use this method and not [[from]] when working with fixed values (''magic numbers'').
     *
     * @param meters
     *   A known integer.
     * @return
     *   A valid Meters or a compiler error.
     * @see
     *   [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(meters: Int): Meters =
      inline if meters < 0
      then error(codeOf(meters) + errorMessage)
      else meters

    given Conversion[Meters, Int] with
      override def apply(x: Meters): Int = x
