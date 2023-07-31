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

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.coreheaders.model.types.types.*

/**
 * NameSpace for the following case classes:
 *
 *   - [[com.fortyseven.coreheaders.model.iot.model.GPSPosition]]
 *
 *   - [[com.fortyseven.coreheaders.model.iot.model.WheelRotation]]
 *
 *   - [[com.fortyseven.coreheaders.model.iot.model.BatteryCharge]]
 *
 *   - [[com.fortyseven.coreheaders.model.iot.model.BatteryHealth]]
 *
 *   - [[com.fortyseven.coreheaders.model.iot.model.PneumaticPressure]]
 *
 *   - [[com.fortyseven.coreheaders.model.iot.model.BreaksUsage]]
 *
 *   - [[com.fortyseven.coreheaders.model.iot.model.BreaksHealth]]
 */
object model:

  /**
   * Represents the GPSPosition.
   *
   * @constructor
   *   Create a GPSPosition with a specified `latitude` and `longitude`.
   *
   * @param latitude
   *   The value of the latitude expressed in a Latitude refined type, that is a valid Double.
   * @param longitude
   *   The value of the longitude expressed in a Longitude refined type, that is a valid Double.
   */
  case class GPSPosition(latitude: Latitude, longitude: Longitude)

  /**
   * Represents the WheelRotation
   *
   * @constructor
   *   Create a WheelRotation with a specified `frequency`.
   * @param frequency
   *   The value of the rotation expressed in a Hertz refined type, that is a valid Double.
   */
  case class WheelRotation(frequency: Hz)

  /**
   * Represents the BatteryCharge.
   *
   * @constructor
   *   Create a BatteryCharge with a specified `percentage`.
   *
   * @param percentage
   *   The value of the percentage expressed in a Percentage refined type, that is a valid Double.
   */
  case class BatteryCharge(percentage: Percentage)

  /**
   * Represents the BatteryHealth.
   *
   * @constructor
   *   Create a BatteryHealth with a specified `remaining`.
   *
   * @param remaining
   *   The value of the remaining battery expressed in a Percentage refined type, that is a valid Double.
   */
  case class BatteryHealth(remaining: Percentage)

  /**
   * Represents the PneumaticPressure with a cause in the message.
   *
   * @constructor
   *   Create a PneumaticPressure with a specified `pressure`.
   *
   * @param pressure
   *   The value of the pressure expressed in a Bar refined type, that is a valid Double.
   */
  case class PneumaticPressure(pressure: Bar)

  /**
   * Represents the BreaksUsage.
   *
   * @constructor
   *   Create a BreaksUsage with a specified `finiteDuration`.
   *
   * @param finiteDuration
   *   The value of how long the break was used expressed in seconds [[scala.concurrent.duration.FiniteDuration]].
   */
  case class BreaksUsage(finiteDuration: FiniteDuration)

  /**
   * Represents the BreaksHealth.
   *
   * @constructor
   *   Create a BreaksHealth with a specified `remaining`.
   *
   * @param remaining
   *   The value of how much health is left for the breaks expressed in a Percentage refined type, that is a valid Double.
   */
  case class BreaksHealth(remaining: Percentage)
