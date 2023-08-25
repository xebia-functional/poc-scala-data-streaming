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

import scala.compiletime.{error, requireConst}

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

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param coordinateCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Latitude or a Left OutOfBoundsError.
     */
    def from(coordinateCandidate: Double): Either[OutOfBoundsError, Latitude] = coordinateCandidate match
      case c if c < -90.0 || c > 90.0 => Left(OutOfBoundsError(s"Invalid latitude value $c"))
      case c                          => Right(c)

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
      requireConst(coordinate)
      inline if coordinate < -90.0 || coordinate > 90.0
      then error("Invalid latitude value. Accepted coordinate values are between -90.0 and 90.0.")
      else coordinate

    extension (coordinate: Latitude) def value: Double = coordinate

  /**
   * Factory for [[Longitude]] instances.
   */
  object Longitude:

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param coordinateCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Longitude or a Left OutOfBoundsError.
     */
    def from(coordinateCandidate: Double): Either[OutOfBoundsError, Longitude] = coordinateCandidate match
      case c if c < -180.0 || c > 180.0 => Left(OutOfBoundsError(s"Invalid longitude value $c"))
      case c                            => Right(c)

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
      requireConst(coordinate)
      inline if coordinate < -180.0 || coordinate > 180.0
      then error("Invalid longitude value. Accepted coordinate values are between -180.0 and 180.0.")
      else coordinate

    extension (coordinate: Longitude) def value: Double = coordinate

  /**
   * Factory for [[Percentage]] instances.
   */
  object Percentage:

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param percentageCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Percentage or a Left OutOfBoundsError.
     */
    def from(percentageCandidate: Double): Either[OutOfBoundsError, Percentage] = percentageCandidate match
      case p if p < 0.0 || p > 100.0 => Left(OutOfBoundsError(s"Invalid percentage value $p"))
      case percentage                => Right(percentage)

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
      requireConst(percentage)
      inline if percentage < 0.0 || percentage > 100.0
      then error("Invalid percentage value. Accepted percentage values are between -0.0 and 100.0.")
      else percentage

    extension (percentage: Percentage) def value: Double = percentage

  /**
   * Factory for [[Speed]] instances.
   */
  object Speed:

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param speedCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Speed or a Left OutOfBoundsError.
     */
    def from(speedCandidate: Double): Either[OutOfBoundsError, Speed] = speedCandidate match
      case speed if speed < 0.0 => Left(OutOfBoundsError(s"Invalid speed value $speed"))
      case speed                => Right(speed)

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
      requireConst(speed)
      inline if speed < 0.0
      then error("Invalid speed value. Accepted speed values are greater than 0.0.")
      else speed

    extension (speed: Speed) def value: Double = speed

  /**
   * Factory for [[Hz]] instances.
   */
  object Hz:

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param hertzCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Hz or a Left OutOfBoundsError.
     */
    def from(hertzCandidate: Double): Either[OutOfBoundsError, Hz] = hertzCandidate match
      case hz if hz < 0.0 => Left(OutOfBoundsError(s"Invalid frequency value $hz"))
      case hz             => Right(hz)

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
      then error("Invalid frequency value. Accepted hertz values are greater than 0.0.")
      else hertz

    extension (hertz: Hz) def value: Double = hertz

  /**
   * Factory for [[Bar]] instances.
   */
  object Bar:

    /**
     * Smart constructor for unknown doubles at compile time.
     *
     * @param barCandidate
     *   An unknown double.
     * @return
     *   An Either with a Right Bar or a Left OutOfBoundsError.
     */
    def from(barCandidate: Double): Either[OutOfBoundsError, Bar] = barCandidate match
      case p if p < 0.0 => Left(OutOfBoundsError(s"Invalid pressure value $p"))
      case p            => Right(p)

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
      requireConst(bar)
      inline if bar < 0.0
      then error("Invalid pressure value. Accepted bar values are greater than 0.0.")
      else bar

    extension (bar: Bar) def value: Double = bar

  /**
   * Factory for [[Meters]] instances.
   */
  object Meters:

    /**
     * Smart constructor for unknown integers at compile time.
     *
     * @param metersCandidate
     *   An unknown integer.
     * @return
     *   An Either with a Right Meters or a Left OutOfBoundsError.
     */
    def from(metersCandidate: Int): Either[OutOfBoundsError, Meters] = metersCandidate match
      case meters if meters < 0 => Left(OutOfBoundsError(s"Invalid meters value $meters"))
      case meters               => Right(meters)

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
      requireConst(meters)
      inline if meters < 0
      then error("Invalid meters value. Accepted bar values are greater than 0.")
      else meters

    extension (meters: Meters) def value: Int = meters
