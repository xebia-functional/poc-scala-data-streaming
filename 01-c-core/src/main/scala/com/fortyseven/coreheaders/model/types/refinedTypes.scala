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

package com.fortyseven.coreheaders.model.types

import scala.compiletime.{error, requireConst}

import com.fortyseven.coreheaders.model.iot.errors.*

/**
 * NameSpace for the following instances:
 *
 *   - [[com.fortyseven.coreheaders.model.types.refinedTypes.Bar]]
 *
 *   - [[com.fortyseven.coreheaders.model.types.refinedTypes.Hz]]
 *
 *   - [[com.fortyseven.coreheaders.model.types.refinedTypes.Latitude]]
 *
 *   - [[com.fortyseven.coreheaders.model.types.refinedTypes.Longitude]]
 *
 *   - [[com.fortyseven.coreheaders.model.types.refinedTypes.Meters]]
 *
 *   - [[com.fortyseven.coreheaders.model.types.refinedTypes.Percentage]]
 *
 *   - [[com.fortyseven.coreheaders.model.types.refinedTypes.Speed]]
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
   * Factory for [[com.fortyseven.coreheaders.model.types.refinedTypes.Latitude]] instances.
   */
  object Latitude:

    /**
     * Tries to build a valid value of type Latitude from the given double.
     *
     * @param coordinateCandidate
     *   the given Double that could be a Latitude.
     * @return
     *   Either a throwable of type OutOfBoundsError or a valid instance of type Latitude.
     */
    def from(coordinateCandidate: Double): Either[OutOfBoundsError, Latitude] = coordinateCandidate match
      case c if c < -90.0 || c > 90.0 => Left(OutOfBoundsError(s"Invalid latitude value $c"))
      case c                          => Right(c)

    /**
     * Method that builds a Latitude from a double at compile time. This method is used when working with fixed values (''magic numbers'') in the
     * code. The compiler will warn us if the value is invalid at compile time.
     *
     * @param coordinate
     *   the given Double.
     * @return
     *   A valid Latitude or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(coordinate: Double): Latitude =
      requireConst(coordinate)
      inline if coordinate < -90.0 || coordinate > 90.0
      then error("Invalid latitude value. Accepted coordinate values are between -90.0 and 90.0.")
      else coordinate

    extension (coordinate: Latitude) def value: Double = coordinate

  /**
   * Factory for [[com.fortyseven.coreheaders.model.types.refinedTypes.Longitude]] instances.
   */
  object Longitude:

    /**
     * Tries to build a valid value of type Longitude from the given double.
     *
     * @param coordinateCandidate
     *   the given Double that could be a Longitude.
     * @return
     *   Either a throwable of type OutOfBoundsError or a valid instance of type Longitude.
     */
    def from(coordinateCandidate: Double): Either[OutOfBoundsError, Longitude] = coordinateCandidate match
      case c if c < -180.0 || c > 180.0 => Left(OutOfBoundsError(s"Invalid longitude value $c"))
      case c                            => Right(c)

    /**
     * Method that builds a Longitude from a double at compile time. This method is used when working with fixed values (''magic numbers'') in the
     * code. The compiler will warn us if the value is invalid at compile time.
     *
     * @param coordinate
     *   the given Double.
     * @return
     *   A valid Longitude or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(coordinate: Double): Longitude =
      requireConst(coordinate)
      inline if coordinate < -180.0 || coordinate > 180.0
      then error("Invalid longitude value. Accepted coordinate values are between -180.0 and 180.0.")
      else coordinate

    extension (coordinate: Longitude) def value: Double = coordinate

  /**
   * Factory for [[com.fortyseven.coreheaders.model.types.refinedTypes.Percentage]] instances.
   */
  object Percentage:

    /**
     * Tries to build a valid value of type Percentage from the given double.
     *
     * @param percentageCandidate
     *   the given Double that could be a Percentage.
     * @return
     *   Either a throwable of type OutOfBoundsError or a valid instance of type Percentage.
     */
    def from(percentageCandidate: Double): Either[OutOfBoundsError, Percentage] = percentageCandidate match
      case p if p < 0.0 || p > 100.0 => Left(OutOfBoundsError(s"Invalid percentage value $p"))
      case percentage                => Right(percentage)

    /**
     * Method that builds a Percentage from a double at compile time. This method is used when working with fixed values (''magic numbers'') in the
     * code. The compiler will warn us if the value is invalid at compile time.
     *
     * @param percentage
     *   the given Double.
     * @return
     *   A valid Percentage or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(percentage: Double): Percentage =
      requireConst(percentage)
      inline if percentage < 0.0 || percentage > 100.0
      then error("Invalid percentage value. Accepted percentage values are between -0.0 and 100.0.")
      else percentage

    extension (percentage: Percentage) def value: Double = percentage

  /**
   * Factory for [[com.fortyseven.coreheaders.model.types.refinedTypes.Speed]] instances.
   */
  object Speed:

    /**
     * Tries to build a valid value of type Speed from the given double.
     *
     * @param speedCandidate
     *   the given Double that could be a Speed.
     * @return
     *   Either a throwable of type OutOfBoundsError or a valid instance of type Speed.
     */
    def from(speedCandidate: Double): Either[OutOfBoundsError, Speed] = speedCandidate match
      case speed if speed < 0.0 => Left(OutOfBoundsError(s"Invalid speed value $speed"))
      case speed                => Right(speed)

    /**
     * Method that builds a Speed from a double at compile time. This method is used when working with fixed values (''magic numbers'') in the code.
     * The compiler will warn us if the value is invalid at compile time.
     *
     * @param speed
     *   the given Double.
     * @return
     *   A valid Speed or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(speed: Double): Speed =
      requireConst(speed)
      inline if speed < 0.0
      then error("Invalid speed value. Accepted speed values are greater than 0.0.")
      else speed

    extension (speed: Speed) def value: Double = speed

  /**
   * Factory for [[com.fortyseven.coreheaders.model.types.refinedTypes.Hz]] instances.
   */
  object Hz:

    /**
     * Tries to build a valid value of type Hz from the given double.
     *
     * @param hertzCandidate
     *   the given Double that could be a Hz.
     * @return
     *   Either a throwable of type OutOfBoundsError or a valid instance of type Hz.
     */
    def from(hertzCandidate: Double): Either[OutOfBoundsError, Hz] = hertzCandidate match
      case hz if hz < 0.0 => Left(OutOfBoundsError(s"Invalid frequency value $hz"))
      case hz             => Right(hz)

    /**
     * Method that builds a Hz from a double at compile time. This method is used when working with fixed values (''magic numbers'') in the code. The
     * compiler will warn us if the value is invalid at compile time.
     *
     * @param hertz
     *   the given Double.
     * @return
     *   A valid Hz or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(hertz: Double): Hz =
      inline if hertz < 0.0
      then error("Invalid frequency value. Accepted hertz values are greater than 0.0.")
      else hertz

    extension (hertz: Hz) def value: Double = hertz

  /**
   * Factory for [[com.fortyseven.coreheaders.model.types.refinedTypes.Bar]] instances.
   */
  object Bar:

    /**
     * Tries to build a valid value of type Bar from the given double.
     *
     * @param barCandidate
     *   the given Double that could be a Bar.
     * @return
     *   Either a throwable of type OutOfBoundsError or a valid instance of type Bar.
     */
    def from(barCandidate: Double): Either[OutOfBoundsError, Bar] = barCandidate match
      case p if p < 0.0 => Left(OutOfBoundsError(s"Invalid pressure value $p"))
      case p            => Right(p)

    /**
     * Method that builds a Bar from a double at compile time. This method is used when working with fixed values (''magic numbers'') in the code. The
     * compiler will warn us if the value is invalid at compile time.
     *
     * @param bar
     *   the given Double.
     * @return
     *   A valid Bar or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(bar: Double): Bar =
      requireConst(bar)
      inline if bar < 0.0
      then error("Invalid pressure value. Accepted bar values are greater than 0.0.")
      else bar

    extension (bar: Bar) def value: Double = bar

  /**
   * Factory for [[com.fortyseven.coreheaders.model.types.refinedTypes.Meters]] instances.
   */
  object Meters:

    /**
     * Tries to build a valid value of type Meters from the given int.
     *
     * @param metersCandidate
     *   the given Int that could be a Meters.
     * @return
     *   Either a throwable of type OutOfBoundsError or a valid instance of type Meters.
     */
    def from(metersCandidate: Int): Either[OutOfBoundsError, Meters] = metersCandidate match
      case meters if meters < 0 => Left(OutOfBoundsError(s"Invalid meters value $meters"))
      case meters               => Right(meters)

    /**
     * Method that builds a Meters from an int at compile time. This method is used when working with fixed values (''magic numbers'') in the code.
     * The compiler will warn us if the value is invalid at compile time.
     *
     * @param meters
     *   the given Int.
     * @return
     *   A valid Meters or a compiler error.
     * @see
     *   More info at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
     */
    inline def apply(meters: Int): Meters =
      requireConst(meters)
      inline if meters < 0
      then error("Invalid meters value. Accepted bar values are greater than 0.")
      else meters

    extension (meters: Meters) def value: Int = meters
