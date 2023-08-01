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

object refinedTypes:

  opaque type Latitude = Double

  opaque type Longitude = Double

  opaque type Percentage = Double

  opaque type Speed = Double // Should be typed better. Meters/second or Km/h?

  opaque type Hz = Double // IS measure for frequency 1/60 Hz would be 1 RPM

  opaque type Bar = Double

  opaque type Meters = Int

  object Latitude:

    def from(coordinateCandidate: Double): Either[OutOfBoundsError, Latitude] = coordinateCandidate match
      case c if c < -90.0 || c > 90.0 => Left(OutOfBoundsError(s"Invalid latitude value $c"))
      case c                          => Right(c)

    inline def apply(coordinate: Double): Latitude =
      requireConst(coordinate)
      inline if coordinate < -90.0 || coordinate > 90.0
      then error("Invalid latitude value. Accepted coordinate values are between -90.0 and 90.0.")
      else coordinate

    extension (coordinate: Latitude) def value: Double = coordinate

  object Longitude:

    def from(coordinateCandidate: Double): Either[OutOfBoundsError, Longitude] = coordinateCandidate match
      case c if c < -180.0 || c > 180.0 => Left(OutOfBoundsError(s"Invalid longitude value $c"))
      case c                            => Right(c)

    inline def apply(coordinate: Double): Longitude =
      requireConst(coordinate)
      inline if coordinate < -180.0 || coordinate > 180.0
      then error("Invalid longitude value. Accepted coordinate values are between -180.0 and 180.0.")
      else coordinate

    extension (coordinate: Longitude) def value: Double = coordinate

  object Percentage:

    def from(percentageCandidate: Double): Either[OutOfBoundsError, Percentage] = percentageCandidate match
      case p if p < 0.0 || p > 100.0 => Left(OutOfBoundsError(s"Invalid percentage value $p"))
      case percentage                => Right(percentage)

    inline def apply(percentage: Double): Percentage =
      requireConst(percentage)
      inline if percentage < 0.0 || percentage > 100.0
      then error("Invalid percentage value. Accepted percentage values are between -0.0 and 100.0.")
      else percentage

    extension (percentage: Percentage) def value: Double = percentage

  object Speed:

    def from(speedCandidate: Double): Either[OutOfBoundsError, Speed] = speedCandidate match
      case speed if speed < 0.0 => Left(OutOfBoundsError(s"Invalid speed value $speed"))
      case speed                => Right(speed)

    inline def apply(speed: Double): Speed =
      requireConst(speed)
      inline if speed < 0.0
      then error("Invalid speed value. Accepted speed values are greater than 0.0.")
      else speed

    extension (speed: Speed) def value: Double = speed

  object Hz:

    def from(hertzCandidate: Double): Either[OutOfBoundsError, Hz] = hertzCandidate match
      case hz if hz < 0.0 => Left(OutOfBoundsError(s"Invalid frequency value $hz"))
      case hz             => Right(hz)

    inline def apply(hertz: Double): Hz =
      inline if hertz < 0.0
      then error("Invalid frequency value. Accepted hertz values are greater than 0.0.")
      else hertz

    extension (hertz: Hz) def value: Double = hertz

  object Bar:

    def from(barCandidate: Double): Either[OutOfBoundsError, Bar] = barCandidate match
      case p if p < 0.0 => Left(OutOfBoundsError(s"Invalid pressure value $p"))
      case p            => Right(p)

    inline def apply(bar: Double): Bar =
      requireConst(bar)
      inline if bar < 0.0
      then error("Invalid pressure value. Accepted bar values are greater than 0.0.")
      else bar

    extension (bar: Bar) def value: Double = bar

  object Meters:

    def from(metersCandidate: Int): Either[OutOfBoundsError, Meters] = metersCandidate match
      case meters if meters < 0 => Left(OutOfBoundsError(s"Invalid meters value $meters"))
      case meters               => Right(meters)

    inline def apply(meters: Int): Meters =
      requireConst(meters)
      inline if meters < 0
      then error("Invalid meters value. Accepted bar values are greater than 0.")
      else meters

    extension (meters: Meters) def value: Int = meters
