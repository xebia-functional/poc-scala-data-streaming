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

import munit.ScalaCheckSuite
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Prop.forAll
import com.fortyseven.coreheaders.model.types.types.*
import org.scalacheck

class typesTest extends ScalaCheckSuite:

  given Arbitrary[Double] = Arbitrary.apply(Gen.double)

  property("Latitude should conform with Earth latitude limits"):
    forAll: (latitude: Double) =>
      if latitude > 90.0 || latitude < -90.00 then Latitude.apply(latitude).isLeft
      else Latitude.apply(latitude).isRight

  property("Longitude should conform with Earth longitude limits"):
    forAll: (longitude: Double) =>
      if longitude > 180.0 || longitude < -180.00 then Longitude.apply(longitude).isLeft
      else Longitude.apply(longitude).isRight

  property("A valid Percentage should have values between 0 and 100"):
    forAll: (percentage: Double) =>
      if percentage > 100.0 || percentage < 0.0 then Percentage.apply(percentage).isLeft
      else Percentage.apply(percentage).isRight

  property("A valid Speed should have a value equal to 0 or higher"):
    forAll: (speed: Double) =>
      if speed < 0.0 then Speed.apply(speed).isLeft
      else Speed.apply(speed).isRight

  property("A valid Hz should have a value equal to 0 or higher"):
    forAll: (hz: Double) =>
      if hz < 0.0 then Hz.apply(hz).isLeft
      else Hz.apply(hz).isRight

  property("A valid Bar should have a value equal to 0 or higher"):
    forAll: (bar: Double) =>
      if bar < 0.0 then Bar.apply(bar).isLeft
      else Bar.apply(bar).isRight

  property("A valid Meters should have a value equal to 0 or higher"):
    forAll: (meters: Int) =>
      if meters < 0 then Meters.apply(meters).isLeft
      else Meters.apply(meters).isRight