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

package com.fortyseven.domain.model.iot

import scala.concurrent.duration.FiniteDuration

import com.fortyseven.domain.model.types.refinedTypes.*

/**
 * Contains final case classes that represent atomic information captured by sensors.
 */
object model:

  /**
   * GPSPosition.
   *
   * @constructor
   *   Create a GPSPosition with a specified `latitude` and `longitude`.
   *
   * @param latitude
   *   Expressed in a refined type that is a subset of Double (-90.0, 90.0).
   * @param longitude
   *   Expressed in a refined type that is a subset of Double (-180.0, 180.0).
   */
  final case class GPSPosition(latitude: Latitude, longitude: Longitude)

  /**
   * WheelRotation
   *
   * @constructor
   *   Create a WheelRotation with a specified `frequency`.
   * @param frequency
   *   Expressed in a refined type, that is a subset of Double (>=0.0).
   */
  final case class WheelRotation(frequency: Hz)

  /**
   * BatteryCharge.
   *
   * @constructor
   *   Create a BatteryCharge with a specified `percentage`.
   *
   * @param percentage
   *   Expressed in a refined type that is a subset of Double (0.00, 100.00).
   */
  final case class BatteryCharge(percentage: Percentage)

  /**
   * BatteryHealth.
   *
   * @constructor
   *   Create a BatteryHealth with a specified `remaining`.
   *
   * @param remaining
   *   Expressed in a refined type that is a subset of Double (0.0, 100.0).
   */
  final case class BatteryHealth(remaining: Percentage)

  /**
   * PneumaticPressure.
   *
   * @constructor
   *   Create a PneumaticPressure with a specified `pressure`.
   *
   * @param pressure
   *   Expressed in a refined type that is a subset of Double (>=0.0).
   */
  final case class PneumaticPressure(pressure: Bar)

  /**
   * BreaksUsage.
   *
   * @constructor
   *   Create a BreaksUsage with a specified `finiteDuration`.
   *
   * @param finiteDuration
   *   Expressed in seconds [[scala.concurrent.duration.FiniteDuration]].
   */
  final case class BreaksUsage(finiteDuration: FiniteDuration)

  /**
   * BreaksHealth.
   *
   * @constructor
   *   Create a BreaksHealth with a specified `remaining`.
   *
   * @param remaining
   *   Expressed in a refined type that is a subset of Double (0.0, 100.0).
   */
  final case class BreaksHealth(remaining: Percentage)
