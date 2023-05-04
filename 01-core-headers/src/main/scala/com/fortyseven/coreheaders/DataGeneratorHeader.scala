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

package com.fortyseven.coreheaders

import com.fortyseven.coreheaders.model.app.model.*
import com.fortyseven.coreheaders.model.iot.model.*

trait DataGeneratorHeader[F[_]]:

  def generateBatteryCharge: fs2.Stream[F, BateryCharge]

  def generateBreaksUsage: fs2.Stream[F, BreaksUsage]

  def generateGPSPosition: fs2.Stream[F, GPSPosition]

  def generatePneumaticPressure: fs2.Stream[F, PneumaticPressure]

  def generateWheelRotation: fs2.Stream[F, WheelRotation]
