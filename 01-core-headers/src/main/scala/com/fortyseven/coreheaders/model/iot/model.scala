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

package com.fortyseven.coreheaders.model.iot
import com.fortyseven.coreheaders.model.iot.types.{Latitude, Longitude, Percentage}

import scala.concurrent.duration.Duration

object model:
  case class GPSPosition(latitude: Latitude, longitude: Longitude)
  case class WheelRotation(s:Any)
  case class BateryCharge(percentage: Percentage)
  case class BatteryHealth(remaining: Percentage)
  case class PneumaticPressure()
  case class BreaksUsage(duration: Duration)
  case class BreaksHealth(remaining: Percentage)

