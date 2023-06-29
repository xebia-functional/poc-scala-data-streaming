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

package com.fortyseven.core.codecs.iot

import com.fortyseven.coreheaders.model.iot.model.*
import com.fortyseven.core.TestUtils.codeAndDecode
import com.fortyseven.core.TestUtils.given
import munit.ScalaCheckSuite
import org.scalacheck.Prop.forAll
import com.fortyseven.core.codecs.iot.IotCodecs.given
import scala.concurrent.duration.*

class IotCodecsTest extends ScalaCheckSuite:

  property("GPSPosition should return the same value after encoding and decoding"):
    forAll: (gPSPosition: GPSPosition) =>
      assertEquals(codeAndDecode(gPSPosition), Right(gPSPosition))

  property("WheelRotation should return the same value after encoding and decoding"):
    forAll: (wheelRotation: WheelRotation) =>
      assertEquals(codeAndDecode(wheelRotation), Right(wheelRotation))

  property("BatteryCharge should return the same value after encoding and decoding"):
    forAll: (batteryCharge: BatteryCharge) =>
      assertEquals(codeAndDecode(batteryCharge), Right(batteryCharge))

  property("BatteryHealth should return the same value after encoding and decoding"):
    forAll: (batteryHealth: BatteryHealth) =>
      assertEquals(codeAndDecode(batteryHealth), Right(batteryHealth))

  property("PneumaticPressure should return the same value after encoding and decoding"):
    forAll: (pneumaticPressure: PneumaticPressure) =>
      assertEquals(codeAndDecode(pneumaticPressure), Right(pneumaticPressure))

  property("BreaksUsage should return the same value after encoding and decoding"):
    forAll: (breaksUsage: BreaksUsage) => //It builds finiteDuration in NanoSeconds
      assertEquals(codeAndDecode(breaksUsage), Right(BreaksUsage(breaksUsage.finiteDuration.toMillis.millis)))

  property("BreaksHealth should return the same value after encoding and decoding"):
    forAll: (breaksHealth: BreaksHealth) =>
      assertEquals(codeAndDecode(breaksHealth), Right(breaksHealth))
