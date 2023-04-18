package com.fortyseven.core.model.app

import com.fortyseven.core.model.app.types.Kilometers
import com.fortyseven.core.model.ids.{BicycleId, UserId, TripId}
import com.fortyseven.core.model.iot.types.Speed

object model:

  case class TotalDistanceByTrip(tripId: TripId, distance:Kilometers)
  case class TotalDistanceByUser(userId: UserId, distance: Kilometers)
  case class CurrentSpedd(tripId: TripId, speed: Speed)
  case class TotalRange(tripId: TripId, bicycleId: BicycleId, remainingRange: Kilometers)
