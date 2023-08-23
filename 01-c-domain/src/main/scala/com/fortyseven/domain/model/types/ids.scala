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

package com.fortyseven.domain.model.types

import java.util.UUID

/**
 * Contains the ids for the business domain.
 */
object ids:

  opaque type BicycleId = UUID

  opaque type UserId = UUID

  opaque type TripId = UUID

  /**
   * Factory for [[BicycleId]] instances.
   */
  object BicycleId:

    /**
     * Builds a BicycleId from an UUID value.
     *
     * @param id
     *   an UUID.
     * @return
     *   the id typed as BicycleId.
     */
    def apply(id: UUID): BicycleId = id

    extension (bicycleId: BicycleId)
      /**
       * Exposes the bicycle ID as the underlying UUID type.
       *
       * @return
       *   an UUID.
       * @see
       *   [[https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html]].
       */
      def value: UUID = bicycleId

  /**
   * Factory for [[UserId]] instances.
   */
  object UserId:

    /**
     * Builds a UserId from an UUID value.
     *
     * @param id
     *   an UUID.
     * @return
     *   the id typed as UserId.
     */
    def apply(id: UUID): UserId = id

    extension (userId: UserId)
      /**
       * Exposes the user ID as the underlying UUID type.
       *
       * @return
       *   un UUID.
       * @see
       *   [[https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html]].
       */
      def value: UUID = userId

  /**
   * Factory for [[TripId]] instances.
   */
  object TripId:

    /**
     * Builds a TripId from an UUID value.
     *
     * @param tripID
     *   an UUID.
     * @return
     *   the trip ID typed as TripId.
     */
    def apply(tripID: UUID): TripId = tripID

    extension (tripId: TripId)
      /**
       * Exposes the trip ID as the underlying UUID type.
       *
       * @return
       *   an UUID.
       * @see
       *   [[https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html]].
       */
      def value: UUID = tripId
