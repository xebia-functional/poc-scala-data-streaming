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

import com.fortyseven.coreheaders.model.app.model.*
import com.fortyseven.coreheaders.model.iot.model.*
import com.fortyseven.coreheaders.model.iot.types.*
import fs2.Stream

class ModelGenerators[F[_]]:

  def generateBatteryCharge: fs2.Stream[F, BatteryCharge] = ???

  def generateBreaksUsage: fs2.Stream[F, BreaksUsage] = ???

  def generateGPSPosition: fs2.Stream[F, GPSPosition] =
    def emitLoop(latValue: Double, lonValue: Double): fs2.Stream[F, GPSPosition] =
      def getValue(value: Double) = value - math.random() * 1e-3

      (Latitude(getValue(latValue)), Longitude(getValue(lonValue))) match
        case (Right(lat), Right(lon)) => fs2.Stream.emit(GPSPosition(lat, lon)) ++ emitLoop(lat, lon)
        case _                        => emitLoop(latValue, lonValue)

    emitLoop(latValue = 2.0, lonValue = 2.0)

  def generatePneumaticPressure: fs2.Stream[F, PneumaticPressure] =

    def emitLoop(pValue: Double): fs2.Stream[F, PneumaticPressure] =
      Bar(pValue - math.random() * 1e-3) match
        case Right(p) => fs2.Stream.emit(PneumaticPressure(p)) ++ emitLoop(p)
        case _        => emitLoop(pValue)

    emitLoop(pValue = 2.0)

  def generateWheelRotation: fs2.Stream[F, WheelRotation] = ???

  def generateBatteryHealth: fs2.Stream[F, BatteryHealth] = ???

  def generateBreaksHealth: fs2.Stream[F, BreaksHealth] = ???

  def generateCurrentSpeed: fs2.Stream[F, CurrentSpeed] = ???

  def generateTotalDistanceByTrip: fs2.Stream[F, TotalDistanceByTrip] = ???

  def generateTotalDistancePerUser: fs2.Stream[F, TotalDistanceByUser] = ???

  def generateTotalRange: fs2.Stream[F, TotalRange] = ???
