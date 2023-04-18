package com.fortyseven.core.model

import java.util.UUID

object ids:

  opaque type BicycleId <: UUID = UUID
  opaque type UserId <: UUID = UUID
  opaque type TripId <: UUID = UUID

  object BicycleId:
    def apply(id: UUID): BicycleId = id
    def unapply(arg: String): Option[BicycleId] =
      val candidate = UUID.fromString(arg)
      if candidate != null then Some(candidate) else None

    extension (bicycleId: BicycleId) def value: UUID = bicycleId

  object UserId:
    def apply(id: UUID): UserId = id

    def unapply(arg: String): Option[UserId] =
      val candidate = UUID.fromString(arg)
      if candidate != null then Some(candidate) else None

    extension (userId: UserId) def value: UUID = userId

  object TripId:
    def apply(tripID: UUID): TripId = tripID
    def unapply(arg: String): Option[BicycleId] =
      val candidate = UUID.fromString(arg)
      if candidate != null then Some(candidate) else None
    extension (tripId: TripId) def value: UUID = tripId  