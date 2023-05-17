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

package com.fortyseven.coreheaders.model

import java.util.UUID

object ids:

  opaque type BicycleId <: UUID = UUID

  opaque type UserId <: UUID = UUID

  opaque type TripId <: UUID = UUID

  object BicycleId:

    def apply(id: UUID): BicycleId = id

    extension (bicycleId: BicycleId) def value: UUID = bicycleId

  object UserId:

    def apply(id: UUID): UserId = id

    extension (userId: UserId) def value: UUID = userId

  object TripId:

    def apply(tripID: UUID): TripId = tripID

    extension (tripId: TripId) def value: UUID = tripId
