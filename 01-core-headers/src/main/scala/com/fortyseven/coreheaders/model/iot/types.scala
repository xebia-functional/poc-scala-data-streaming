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

package com.fortyseven.coreheaders.model.iot

object types:

  opaque type Coordinate <: Double = Double // Should be limited to earth coordinates
  opaque type Latitude <: Coordinate = Coordinate
  opaque type Longitude <: Coordinate = Coordinate
  opaque type Percentage <: Double = Double //Should be limited to 0.00 and 100.00
  opaque type Speed <: Double = Double // Should be typed better. Meters/second or Km/h?
  opaque type Hz <: Double = Double // IS measure for frequency 1/60 Hz would be 1 RPM

  object Coordinate:
    def apply(coordinate: Double): Coordinate = coordinate
    extension (coordinate: Coordinate) def value: Double = coordinate

  object Percentage:
    def apply(percentage: Double): Percentage = percentage
    extension (percentage: Percentage) def value: Double = percentage

  object Speed:
    def apply(speed: Double): Speed = speed
    extension (speed: Speed) def value: Double = speed

  object Hz:
    def apply(hertz: Double): Hz = hertz
    extension (hertz: Hz) def value: Double = hertz
