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

import com.fortyseven.core.codecs.iot.IotCodecs.given
import com.fortyseven.coreheaders.model.iot.model.{GPSPosition, PneumaticPressure}
import com.fortyseven.coreheaders.model.iot.types.{Bar, Latitude, Longitude}
import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import vulcan.{AvroError, Codec}

class CodecSpec extends ScalaCheckSuite:

  private def getOutput[C](instance: C)(using Codec[C]): Either[AvroError, C] =
    for
      encoded <- vulcan.Codec.encode[C](instance)
      result <- vulcan.Codec.decode[C](encoded)
    yield result

  property("GPSPosition"):
    forAll(Gen.choose(-90, 90), Gen.choose(-180, 180)) { (latitude: Int, longitude: Int) =>
      (Latitude(latitude), Longitude(longitude)) match
        case (Right(lat), Right(lon)) =>
          val gpsPosition = GPSPosition(lat, lon)
          getOutput(gpsPosition) == Right(gpsPosition)
        case _ => assert(false)
      ()
    }

  property("PneumaticPressure"):
    forAll(Gen.choose(1, 100)) { (pressure: Int) =>
      Bar(pressure) match
        case Right(p) =>
          val pneumaticPressure = PneumaticPressure(p)
          getOutput(pneumaticPressure) == Right(pneumaticPressure)
        case _ => assert(false)
      ()
    }
