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

  given Arbitrary[UUID] = Arbitrary.apply(Gen.uuid)

  given Arbitrary[UserId] = Arbitrary.apply(Gen.resultOf[UUID, UserId](UserId.apply))

  given Arbitrary[TripId] = Arbitrary.apply(Gen.resultOf[UUID, TripId](TripId.apply))

  given Arbitrary[BicycleId] = Arbitrary.apply(Gen.uuid.map(BicycleId.apply))

  given Arbitrary[Latitude] = Arbitrary.apply(Gen.chooseNum[Double](-90.0, 90.0).map(Latitude.unsafeApply))

  given Arbitrary[Longitude] = Arbitrary.apply(Gen.chooseNum[Double](-180.0, 180.0).map(Longitude.unsafeApply))

  given Arbitrary[Percentage] = Arbitrary.apply(Gen.chooseNum[Double](0.0, 100.0).map(Percentage.unsafeApply))

  given Arbitrary[Speed] = Arbitrary.apply(Gen.posNum[Double].map(Speed.unsafeApply))

  given Arbitrary[Hz] = Arbitrary.apply(Gen.posNum[Double].map(Hz.unsafeApply))

  given Arbitrary[Bar] = Arbitrary.apply(Gen.posNum[Double].map(Bar.unsafeApply))

  given Arbitrary[Meters] = Arbitrary.apply(Gen.posNum[Int].map(Meters.unsafeApply))

  given Arbitrary[TotalDistanceByTrip] = Arbitrary.apply(
    Gen.resultOf[TripId, Meters, TotalDistanceByTrip]((tripId, meters) => TotalDistanceByTrip.apply(tripId, meters))
  )

  given Arbitrary[TotalDistanceByUser] = Arbitrary.apply(
    Gen.resultOf[UserId, Meters, TotalDistanceByUser]((userId, meters) => TotalDistanceByUser.apply(userId, meters))
  )

  given Arbitrary[CurrentSpeed] = Arbitrary.apply(Gen.resultOf[TripId, Speed, CurrentSpeed](CurrentSpeed.apply))

  given Arbitrary[TotalRange] = Arbitrary.apply(
    Gen.resultOf[TripId, BicycleId, Meters, TotalRange]((tripId, bicycleId, remainingRange) =>
      TotalRange(tripId, bicycleId, remainingRange)
    )
  )

  given Arbitrary[GPSPosition] = Arbitrary.apply(
    Gen.resultOf[Latitude, Longitude, GPSPosition]((latitude, longitude) => GPSPosition(latitude, longitude))
  )

  given Arbitrary[WheelRotation] = Arbitrary.apply(Gen.resultOf[Hz, WheelRotation](hz => WheelRotation(hz)))

  given Arbitrary[BatteryCharge] =
    Arbitrary.apply(Gen.resultOf[Percentage, BatteryCharge](percentage => BatteryCharge(percentage)))

  given Arbitrary[BatteryHealth] =
    Arbitrary.apply(Gen.resultOf[Percentage, BatteryHealth](remaining => BatteryHealth(remaining)))

  given Arbitrary[PneumaticPressure] =
    Arbitrary.apply(Gen.resultOf[Bar, PneumaticPressure](pressure => PneumaticPressure(pressure)))

  given Arbitrary[BreaksUsage] =
    Arbitrary.apply(Gen.resultOf[FiniteDuration, BreaksUsage](finiteDuration => BreaksUsage(finiteDuration)))

  given Arbitrary[BreaksHealth] =
    Arbitrary.apply(Gen.resultOf[Percentage, BreaksHealth](remaining => BreaksHealth(remaining)))

  def getOutput[C](instance: C)(using Codec[C]): Either[AvroError, C] =
    for
      encoded <- vulcan.Codec.encode[C](instance)
      result  <- vulcan.Codec.decode[C](encoded)
    yield result
