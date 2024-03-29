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

package com.fortyseven.datagenerator

import cats.effect.Temporal
import fs2.Stream

import scala.concurrent.duration.*

import com.fortyseven.domain.model.app.model.*
import com.fortyseven.domain.model.iot.model.*
import com.fortyseven.domain.model.types.refinedTypes.*

final class ModelGenerators[F[_]: Temporal](meteredInterval: FiniteDuration):

  def generateBatteryCharge: fs2.Stream[F, BatteryCharge] = ???

  def generateBreaksUsage: fs2.Stream[F, BreaksUsage] = ???

  def generateGPSPosition: fs2.Stream[F, GPSPosition] =
    def emitLoop(latValue: Double, lonValue: Double): fs2.Stream[F, GPSPosition] =
      def getValue(value: Double) = value - math.random() * 1e-3

      (Latitude.from(getValue(latValue)), Longitude.from(getValue(lonValue))) match
        case (Right(lat), Right(lon)) => fs2.Stream.emit(GPSPosition(lat, lon)).metered(meteredInterval) ++
            emitLoop(lat.value, lon.value)
        case _ => emitLoop(latValue, lonValue)

    emitLoop(latValue = 2.0, lonValue = 2.0)

  def generatePneumaticPressure: fs2.Stream[F, PneumaticPressure] =

    def emitLoop(pValue: Double): fs2.Stream[F, PneumaticPressure] = Bar.from(pValue - math.random() * 1e-3) match
      case Right(p) => fs2.Stream.emit(PneumaticPressure(p)).metered(meteredInterval) ++ emitLoop(p.value)
      case _ => emitLoop(pValue)

    emitLoop(pValue = 2.0)

  def generateWheelRotation: fs2.Stream[F, WheelRotation] = ???

  def generateBatteryHealth: fs2.Stream[F, BatteryHealth] = ???

  def generateBreaksHealth: fs2.Stream[F, BreaksHealth] = ???

  def generateCurrentSpeed: fs2.Stream[F, CurrentSpeed] = ???

  def generateTotalDistanceByTrip: fs2.Stream[F, TotalDistanceByTrip] = ???

  def generateTotalDistancePerUser: fs2.Stream[F, TotalDistanceByUser] = ???

  def generateTotalRange: fs2.Stream[F, TotalRange] = ???

end ModelGenerators
