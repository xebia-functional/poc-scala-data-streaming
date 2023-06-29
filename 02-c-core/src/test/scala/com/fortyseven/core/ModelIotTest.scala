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

package com.fortyseven.core

import com.fortyseven.coreheaders.model.iot.types.*
import munit.FunSuite
import munit.ScalaCheckSuite
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class ModelIotTest extends ScalaCheckSuite:

  given Arbitrary[Double] = Arbitrary.apply(Gen.choose(-360.0, 360.0))

  property("Latitude"):
    forAll { (coordinate: Double) =>
      if coordinate > 90.0 || coordinate < -90.0 then assert(Latitude(coordinate).isLeft)
      else assert(Latitude(coordinate).isRight)
    }

  property("Longitude"):
    forAll: (coordinate: Double) =>
      if coordinate > 180.0 || coordinate < -180.0 then assert(Longitude(coordinate).isLeft)
      else assert(Longitude(coordinate).isRight)

  property("Bar"):
    forAll: (pressure: Double) =>
      if pressure < 0.0 then assert(Bar(pressure).isLeft)
      else assert(Bar(pressure).isRight)
