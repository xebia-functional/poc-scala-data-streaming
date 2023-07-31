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

package com.fortyseven.coreheaders.model.app

import com.fortyseven.coreheaders.model.types.ids.{BicycleId, TripId, UserId}
import com.fortyseven.coreheaders.model.types.types.{Meters, Speed}

/**
 * NameSpace for the following case classes:
 *
 *   - [[com.fortyseven.coreheaders.model.app.model.TotalDistanceByTrip]]
 *
 *   - [[com.fortyseven.coreheaders.model.app.model.TotalDistanceByUser]]
 *
 *   - [[com.fortyseven.coreheaders.model.app.model.CurrentSpeed]]
 *
 *   - [[com.fortyseven.coreheaders.model.app.model.TotalRange]]
 */
object model:

  /**
   * Represents the total distance by trip ID.
   *
   * @constructor
   *   Create a TotalDistanceByTrip with a specified `tripId` and `distance`.
   * @param tripId
   *   The key of the tuple that will allow joins and aggregations.
   * @param distance
   *   The distance cycled for the given trip ID, in meters.
   */
  case class TotalDistanceByTrip(tripId: TripId, distance: Meters)

  /**
   * Represents the total distance by user ID.
   *
   * @constructor
   *   Create a TotalDistanceByUser with a specified `tripId` and `distance`
   * @param userId
   *   The key of the tuple that will allow joins and aggregations.
   * @param distance
   *   The distance cycled for the given user ID, in meters.
   */
  case class TotalDistanceByUser(userId: UserId, distance: Meters)

  /**
   * Represents the current speed of the given trip ID.
   *
   * @constructor
   *   Create a CurrentSpeed with a specified `tripId` and `speed`
   * @param tripId
   *   The key of the tuple that will allow joins and aggregations.
   * @param speed
   *   The speed at the given moment for the given tripId.
   * @todo
   *   maybe a new parameter with a timestamp should be added to capture the time of the speed in the bicycle.
   */
  case class CurrentSpeed(tripId: TripId, speed: Speed)

  /**
   * Represents the total range of the given bicycle for the given trip.
   *
   * @constructor
   *   Create a TotalRange with a specified `tripId`, `bicycleId` and `remainingRange`.
   * @param tripId
   *   The key of the tuple that will allow joins and aggregations.
   * @param bicycleId
   *   The identifier of the bicycle.
   * @param remainingRange
   *   The remaining range calculated from the remaining energy in the battery.
   */
  case class TotalRange(tripId: TripId, bicycleId: BicycleId, remainingRange: Meters)
