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

import scala.compiletime.testing.{typeCheckErrors, typeChecks}

import com.fortyseven.domain.model.types.refinedTypes.*
import munit.ScalaCheckSuite
import org.scalacheck
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class refinedTypesTest extends ScalaCheckSuite:

  property("Latitudes grater than 90 are not allowed"):
    forAll(Gen.chooseNum(90.0, Double.MaxValue).suchThat(_ > 90.0)): latitude =>
      Latitude.from(latitude).isLeft

  property("Latitudes smaller than -90 are not allowed"):
    forAll(Gen.chooseNum(Double.MinValue, -90.0).suchThat(_ < -90.0)): latitude =>
      Latitude.from(latitude).isLeft

  test("Invalid Latitude value should raise a compiler error"):
    assertNoDiff(
      typeCheckErrors("Latitude.apply(91.0)").head.message,
      s"91.0d${Latitude.errorMessage}"
    )
    assertNoDiff(
      typeCheckErrors("Latitude.apply(-91.0)").head.message,
      s"-91.0d${Latitude.errorMessage}"
    )

  property("Latitude should build from values that conform with Earth latitude limits"):
    forAll(Gen.chooseNum(-90.0, 90.0)): latitude =>
      Latitude.from(latitude).isRight

  test("Valid Latitude value should compile"):
    assert(typeChecks("Latitude.apply(0.0)"))

  property("Longitude grater than 180 are not allowed"):
    forAll(Gen.chooseNum(180.0, Double.MaxValue).suchThat(_ > 180.0)): longitude =>
      Longitude.from(longitude).isLeft

  property("Longitude smaller than -180 are not allowed"):
    forAll(Gen.chooseNum(Double.MinValue, -180.0).suchThat(_ < -180.0)): longitude =>
      Longitude.from(longitude).isLeft

  test("Invalid Longitude value should raise a compiler error"):
    assertNoDiff(
      typeCheckErrors("Longitude.apply(181.0)").head.message,
      s"181.0d${Longitude.errorMessage}"
    )
    assertNoDiff(
      typeCheckErrors("Longitude.apply(-181.0)").head.message,
      s"-181.0d${Longitude.errorMessage}"
    )

  property("Longitude should build from values that conform with Earth longitude limits"):
    forAll(Gen.chooseNum(-180.0, 180.0)): longitude =>
      Longitude.from(longitude).isRight

  test("Valid Longitude value should compile"):
    assert(typeChecks("Longitude.apply(0.0)"))

  property("An invalid Percentage should have values bellow 0"):
    forAll(Gen.negNum[Double]): percentage =>
      Percentage.from(percentage).isLeft

  property("An invalid Percentage should have values above 100"):
    forAll(Gen.chooseNum(100.0, Double.MaxValue).suchThat(_ > 100.0)): percentage =>
      Percentage.from(percentage).isLeft

  test("Invalid Percentage value should raise a compiler error"):
    assertNoDiff(
      typeCheckErrors("Percentage.apply(101.0)").head.message,
      s"101.0d${Percentage.errorMessage}"
    )
    assertNoDiff(
      typeCheckErrors("Percentage.apply(-1.0)").head.message,
      s"-1.0d${Percentage.errorMessage}"
    )

  property("A valid Percentage should have values between 0 and 100"):
    forAll(Gen.chooseNum(0.0, 100.0)): percentage =>
      Percentage.from(percentage).isRight

  test("Valid Percentage value should compile"):
    assert(typeChecks("Percentage.apply(0.0)"))

  property("An invalid Speed should have a value lower than 0"):
    forAll(Gen.negNum[Double]): speed =>
      Speed.from(speed).isLeft

  test("Invalid Speed value should raise a compiler error"):
    assertNoDiff(
      typeCheckErrors("Speed.apply(-1.0)").head.message,
      s"-1.0d${Speed.errorMessage}"
    )

  property("A valid Speed should have a value equal to 0 or higher"):
    forAll(Gen.posNum[Double]): speed =>
      Speed.from(speed).isRight

  test("Valid Speed value should compile"):
    assert(typeChecks("Speed.apply(0.0)"))

  property("An invalid Hz should have a value lower than 0"):
    forAll(Gen.negNum[Double]): hz =>
      Hz.from(hz).isLeft

  test("Invalid Hz value should raise a compiler error"):
    assertNoDiff(
      typeCheckErrors("Hz.apply(-1.0)").head.message,
      s"-1.0d${Hz.errorMessage}"
    )

  property("A valid Hz should have a value equal to 0 or higher"):
    forAll(Gen.posNum[Double]): hz =>
      Hz.from(hz).isRight

  test("Valid Hz value should compile"):
    assert(typeChecks("Hz.apply(0.0)"))

  property("An invalid Bar should have a value lower than 0"):
    forAll(Gen.negNum[Double]): bar =>
      Bar.from(bar).isLeft

  test("Invalid Bar value should raise a compiler error"):
    assertNoDiff(
      typeCheckErrors("Bar.apply(-1.0)").head.message,
      s"-1.0d${Bar.errorMessage}"
    )

  property("A valid Bar should have a value equal to 0 or higher"):
    forAll(Gen.posNum[Double]): bar =>
      Bar.from(bar).isRight

  test("Valid Bar value should compile"):
    assert(typeChecks("Bar.apply(0.0)"))

  property("An invalid Meters should have a value lower than 0"):
    forAll(Gen.negNum[Int]): meters =>
      Meters.from(meters).isLeft

  test("Invalid Meters value should raise a compiler error"):
    assertNoDiff(
      typeCheckErrors("Meters.apply(-1)").head.message,
      s"-1${Meters.errorMessage}"
    )

  property("A valid Meters should have a value equal to 0 or higher"):
    forAll(Gen.posNum[Int]): meters =>
      Meters.from(meters).isRight

  test("Valid Meters value should compile"):
    assert(typeChecks("Meters.apply(0)"))
