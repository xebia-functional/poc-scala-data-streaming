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

package com.fortyseven.domain

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.domain.model.app.model.*
import com.fortyseven.domain.model.iot.model.*
import com.fortyseven.domain.model.types.ids.*
import com.fortyseven.domain.model.types.refinedTypes.*

import io.github.iltotore.iron.scalacheck.all.given
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import vulcan.AvroError
import vulcan.Codec

object TestUtils:

  given Arbitrary[TotalDistanceByTrip] = Arbitrary(Gen.resultOf(TotalDistanceByTrip.apply))

  given Arbitrary[TotalDistanceByUser] = Arbitrary(Gen.resultOf(TotalDistanceByUser.apply))

  given Arbitrary[CurrentSpeed] = Arbitrary(Gen.resultOf(CurrentSpeed.apply))

  given Arbitrary[TotalRange] = Arbitrary(Gen.resultOf(TotalRange.apply))

  given Arbitrary[GPSPosition] = Arbitrary(Gen.resultOf(GPSPosition.apply))

  given Arbitrary[WheelRotation] = Arbitrary(Gen.resultOf(WheelRotation.apply))

  given Arbitrary[BatteryCharge] = Arbitrary(Gen.resultOf(BatteryCharge.apply))

  given Arbitrary[BatteryHealth] = Arbitrary(Gen.resultOf(BatteryHealth.apply))

  given Arbitrary[PneumaticPressure] = Arbitrary(Gen.resultOf(PneumaticPressure.apply))

  given Arbitrary[BreaksUsage] = Arbitrary(Gen.resultOf(BreaksUsage.apply))

  given Arbitrary[BreaksHealth] = Arbitrary(Gen.resultOf(BreaksHealth.apply))

  def codeAndDecode[C: Codec](instance: C): Either[AvroError, C] = Codec.encode(instance).flatMap(Codec.decode[C])

end TestUtils
