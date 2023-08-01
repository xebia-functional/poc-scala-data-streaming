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

package com.fortyseven.coreheaders.model.types

import java.util.UUID

/**
 * NameSpace for the following types:
 *
 *   - [[com.fortyseven.coreheaders.model.types.ids.BicycleId]]
 *
 *   - [[com.fortyseven.coreheaders.model.types.ids.UserId]]
 *
 *   - [[com.fortyseven.coreheaders.model.types.ids.TripId]]
 */
object ids:

  opaque type BicycleId = UUID

  opaque type UserId = UUID

  opaque type TripId = UUID

  /**
   * Factory for [[com.fortyseven.coreheaders.model.types.ids.BicycleId]] instances.
   */
  object BicycleId:
    /**
     * Method that builds a BicycleId from an UUID value.
     *
     * @param id
     *   an UUID value to be returned as a BicycleId type.
     * @return
     *   a BicycleId value.
     */
    def apply(id: UUID): BicycleId = id

    extension (bicycleId: BicycleId)
      /**
       * Casts the types while keeping the value.
       *
       * @return
       *   Value of type BicycleId as [[java.util.UUID]] type.
       * @see
       *   More info at [[https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html]].
       */
      def value: UUID = bicycleId

  /**
   * Factory for [[com.fortyseven.coreheaders.model.types.ids.UserId]] instances.
   */
  object UserId:
    /**
     * Method that builds a UserId from an UUID value.
     *
     * @param id
     *   an UUID value to be returned as a UserId type.
     * @return
     *   a UserId value.
     */
    def apply(id: UUID): UserId = id

    extension (userId: UserId)
      /**
       * Casts the types while keeping the value.
       *
       * @return
       *   Value of type UserId as [[java.util.UUID]] type.
       * @see
       *   More info at [[https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html]].
       */
      def value: UUID = userId

  /**
   * Factory for [[com.fortyseven.coreheaders.model.types.ids.TripId]] instances.
   */
  object TripId:
    /**
     * Method that builds a TripId from an UUID value.
     *
     * @param tripID
     *   an UUID value to be returned as a TripId type.
     * @return
     *   a TripId value.
     */
    def apply(tripID: UUID): TripId = tripID

    extension (tripId: TripId)
      /**
       * Casts the types while keeping the value.
       *
       * @return
       *   Value of type TripId as [[java.util.UUID]] type.
       * @see
       *   More info at [[https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html]].
       */
      def value: UUID = tripId
