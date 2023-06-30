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

import com.fortyseven.coreheaders.model.app.model.*
import com.fortyseven.coreheaders.model.iot.model.*
import com.fortyseven.coreheaders.model.types.ids.*
import com.fortyseven.coreheaders.model.types.types.*
import org.scalacheck.{Arbitrary, Gen}
import vulcan.{AvroError, Codec}

import java.util.UUID
import scala.concurrent.duration.FiniteDuration

object TestUtils:

  given Arbitrary[UUID] = Arbitrary(Gen.uuid)

  given Arbitrary[UserId] = Arbitrary(Gen.resultOf[UUID, UserId](UserId.apply))

  given Arbitrary[TripId] = Arbitrary(Gen.resultOf[UUID, TripId](TripId.apply))

  given Arbitrary[BicycleId] = Arbitrary(Gen.uuid.map(BicycleId.apply))

  given Arbitrary[Latitude] = Arbitrary(
    Gen.chooseNum[Double](-90.0, 90.0).flatMap(Latitude(_).fold(_ => Gen.fail, Gen.const))
  )

  given Arbitrary[Longitude] = Arbitrary(
    Gen.chooseNum[Double](-180.0, 180.0).flatMap(Longitude(_).fold(_ => Gen.fail, Gen.const))
  )

  given Arbitrary[Percentage] = Arbitrary(
    Gen.chooseNum[Double](0.0, 100.0).flatMap(Percentage(_).fold(_ => Gen.fail, Gen.const))
  )

  given Arbitrary[Speed] = Arbitrary(Gen.posNum[Double].flatMap(Speed(_).fold(_ => Gen.fail, Gen.const)))

  given Arbitrary[Hz] = Arbitrary(Gen.posNum[Double].flatMap(Hz(_).fold(_ => Gen.fail, Gen.const)))

  given Arbitrary[Bar] = Arbitrary(Gen.posNum[Double].flatMap(Bar(_).fold(_ => Gen.fail, Gen.const)))

  given Arbitrary[Meters] = Arbitrary(Gen.posNum[Int].flatMap(Meters(_).fold(_ => Gen.fail, Gen.const)))

  given Arbitrary[TotalDistanceByTrip] = Arbitrary(
    Gen.resultOf(TotalDistanceByTrip.apply)
  )

  given Arbitrary[TotalDistanceByUser] = Arbitrary(
    Gen.resultOf(TotalDistanceByUser.apply)
  )

  given Arbitrary[CurrentSpeed] = Arbitrary(Gen.resultOf(CurrentSpeed.apply))

  given Arbitrary[TotalRange] = Arbitrary(
    Gen.resultOf(TotalRange.apply)
  )

  given Arbitrary[GPSPosition] = Arbitrary(
    Gen.resultOf(GPSPosition.apply)
  )

  given Arbitrary[WheelRotation] = Arbitrary(Gen.resultOf(WheelRotation.apply))

  given Arbitrary[BatteryCharge] =
    Arbitrary(Gen.resultOf(BatteryCharge.apply))

  given Arbitrary[BatteryHealth] = Arbitrary(Gen.resultOf(BatteryHealth.apply))

  given Arbitrary[PneumaticPressure] = Arbitrary(Gen.resultOf(PneumaticPressure.apply))

  given Arbitrary[BreaksUsage] = Arbitrary(Gen.resultOf(BreaksUsage.apply))

  given Arbitrary[BreaksHealth] = Arbitrary(Gen.resultOf(BreaksHealth.apply))

  def codeAndDecode[C: Codec](instance: C): Either[AvroError, C] = Codec.encode(instance).flatMap(Codec.decode[C])
