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

package com.fortyseven.core.codecs.types

import com.fortyseven.core.TestUtils.getOutput
import munit.ScalaCheckSuite
import org.scalacheck.Prop.forAll
import com.fortyseven.core.codecs.types.TypesCodecs.given
import com.fortyseven.core.codecs.iot.IotErrorCodecs.given
import com.fortyseven.coreheaders.model.iot.errors.OutOfBoundsError
import com.fortyseven.coreheaders.model.types.types.*
import org.scalacheck.Gen

class TypesCodecsTest extends ScalaCheckSuite:

  property("Latitude should return the same value after encoding and decoding"):
    forAll(Gen.double.flatMap(Latitude.apply)): (latitude: Either[OutOfBoundsError, Latitude]) =>
      latitude match
        case Left(outOfBoundsError: OutOfBoundsError) => assert(getOutput(outOfBoundsError).isLeft)
        case Right(latitude: Latitude) => assert(getOutput(latitude).isRight)

  property("Longitude should return the same value after encoding and decoding"):
    forAll(Gen.double.flatMap(Longitude.apply)): (longitude: Either[OutOfBoundsError, Longitude]) =>
      longitude match
        case Left(outOfBoundsError: OutOfBoundsError) => assert(getOutput(outOfBoundsError).isLeft)
        case Right(longitude: Longitude) => assert(getOutput(longitude).isRight)

  property("Percentage should return the same value after encoding and decoding"):
    forAll(Gen.double.flatMap(Percentage.apply)): (percentage: Either[OutOfBoundsError, Percentage]) =>
      percentage match
        case Left(outOfBoundsError: OutOfBoundsError) => assert(getOutput(outOfBoundsError).isLeft)
        case Right(percentage: Percentage) => assert(getOutput(percentage).isRight)

  property("Speed should return the same value after encoding and decoding"):
    forAll(Gen.double.flatMap(Speed.apply)): (speed: Either[OutOfBoundsError, Speed]) =>
      speed match
        case Left(outOfBoundsError: OutOfBoundsError) => assert(getOutput(outOfBoundsError).isLeft)
        case Right(speed: Speed) => assert(getOutput(speed).isRight)

  property("Hz should return the same value after encoding and decoding"):
    forAll(Gen.double.flatMap(Hz.apply)): (hertz: Either[OutOfBoundsError, Hz]) =>
      hertz match
        case Left(outOfBoundsError: OutOfBoundsError) => assert(getOutput(outOfBoundsError).isLeft)
        case Right(hertz: Hz) => assert(getOutput(hertz).isRight)

  property("Bar should return the same value after encoding and decoding"):
    forAll(Gen.double.flatMap(Bar.apply)): (bar: Either[OutOfBoundsError, Bar]) =>
      bar match
        case Left(outOfBoundsError: OutOfBoundsError) => assert(getOutput(outOfBoundsError).isLeft)
        case Right(bar: Bar) => assert(getOutput(bar).isRight)

  // TODO: this test fails but I am confused about why
  property("Meters should return the same value after encoding and decoding"):
    forAll(Gen.resultOf[Int, Either[OutOfBoundsError, Meters]](Meters.apply)):
      case Left(outOfBoundsError: OutOfBoundsError) => assert(getOutput(outOfBoundsError).isLeft)
      case Right(meters: Meters) => assert(getOutput(meters).isRight)

