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

import com.fortyseven.coreheaders.model.iot.errors.*

object types:

  opaque type Latitude = Double

  opaque type Longitude = Double

  opaque type Percentage = Double

  opaque type Speed = Double // Should be typed better. Meters/second or Km/h?

  opaque type Hz  = Double // IS measure for frequency 1/60 Hz would be 1 RPM

  opaque type Bar = Double

  opaque type Meters = Int

  object Latitude:

    def apply(coordinate: Double): Either[OutOfBoundsError, Latitude] = coordinate match
      case c if c < -90.0 || c > 90.0 => Left(OutOfBoundsError(s"Invalid latitude value $c"))
      case c                          => Right(c)

    extension (coordinate: Latitude) def value: Double = coordinate

  object Longitude:

    def apply(coordinate: Double): Either[OutOfBoundsError, Longitude] = coordinate match
      case c if c < -180.0 || c > 180.0 => Left(OutOfBoundsError(s"Invalid longitude value $c"))
      case c                            => Right(c)

    extension (coordinate: Longitude) def value: Double = coordinate

  object Percentage:

    def apply(percentage: Double): Either[OutOfBoundsError, Percentage] = percentage match
      case p if p < 0.0 || p > 100.0 => Left(OutOfBoundsError(s"Invalid percentage value $p"))
      case percentage                => Right(percentage)

    extension (percentage: Percentage) def value: Double = percentage

  object Speed:

    def apply(speed: Double): Either[OutOfBoundsError, Speed] = speed match
      case speed if speed < 0.0 => Left(OutOfBoundsError(s"Invalid speed value $speed"))
      case speed                => Right(speed)

    extension (speed: Speed) def value: Double = speed

  object Hz:

    def apply(hertz: Double): Either[OutOfBoundsError, Hz] = hertz match
      case hz if hz < 0.0 => Left(OutOfBoundsError(s"Invalid frequency value $hz"))
      case hz             => Right(hz)

    extension (hertz: Hz) def value: Double = hertz

  object Bar:

    def apply(bar: Double): Either[OutOfBoundsError, Bar] = bar match
      case p if p < 0.0 => Left(OutOfBoundsError(s"Invalid pressure value $p"))
      case p            => Right(p)

    extension (bar: Bar) def value: Double = bar

  object Meters:

    def apply(meters: Int): Either[OutOfBoundsError, Meters] = meters match
      case meters if meters < 0 => Left(OutOfBoundsError(s"Invalid meters value $meters"))
      case meters               => Right(meters)

    extension (meters: Meters) def value: Int = meters
