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

package com.fortyseven.domain.model.app

import com.fortyseven.domain.model.types.ids.BicycleId
import com.fortyseven.domain.model.types.ids.TripId
import com.fortyseven.domain.model.types.ids.UserId
import com.fortyseven.domain.model.types.refinedTypes.Meters
import com.fortyseven.domain.model.types.refinedTypes.Speed

/** Contains case classes that represent aggregated data.
  */
object model:

  /** Total distance of a given trip ID expressed in meters.
    *
    * @constructor
    *   Create a TotalDistanceByTrip with a specified `tripId` and `distance`.
    * @param tripId
    *   key of the aggregation.
    * @param distance
    *   value of the aggregation, in meters.
    */
  final case class TotalDistanceByTrip(tripId: TripId, distance: Meters)

  /** Total distance cycled by a given user.
    *
    * @constructor
    *   Create a TotalDistanceByUser with a specified `userId` and `distance`.
    * @param userId
    *   Key of the aggregation.
    * @param distance
    *   Distance cycled in meters for the given user ID.
    */
  final case class TotalDistanceByUser(userId: UserId, distance: Meters)

  /** Current speed of the given trip ID.
    *
    * @constructor
    *   Create a CurrentSpeed with a specified `tripId` and `speed`
    * @param tripId
    *   Key of the aggregation.
    * @param speed
    *   Speed at the given moment for the given tripId.
    * @todo
    *   maybe a new parameter with a timestamp should be added to capture the time of the speed in the bicycle.
    */
  final case class CurrentSpeed(tripId: TripId, speed: Speed)

  /** Total range of the given bicycle for the given trip.
    *
    * @constructor
    *   Create a TotalRange with a specified `tripId`, `bicycleId` and `remainingRange`.
    * @param tripId
    *   Key of the aggregation.
    * @param bicycleId
    *   Bicycle identifier.
    * @param remainingRange
    *   Remaining range in meters calculated from the remaining energy in the battery.
    */
  final case class TotalRange(tripId: TripId, bicycleId: BicycleId, remainingRange: Meters)

end model
