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

package com.fortyseven.core.codecs.app

import cats.implicits.*

import com.fortyseven.core.codecs.ids.IdsCodecs.given
import com.fortyseven.core.codecs.types.TypesCodecs.given
import com.fortyseven.coreheaders.model.app.model.*
import vulcan.Codec

/**
 * It contains the Vulcan codecs for the types (enums, case classes...) defined in the object [[com.fortyseven.coreheaders.model.app.model]].
 */
object AppCodecs:

  private val _namespace = "app"

  given totalDistanceByTripCodec: Codec[TotalDistanceByTrip] =
    Codec.record("TotalDistanceByTrip", _namespace) { totalDistanceByTrip =>
      (
        totalDistanceByTrip("tripId", _.tripId),
        totalDistanceByTrip("distance", _.distance)
      ).mapN(TotalDistanceByTrip.apply)
    }

  given totalDistanceByUserCodec: Codec[TotalDistanceByUser] =
    Codec.record("TotalDistanceByUser", _namespace) { totalDistanceByUser =>
      (
        totalDistanceByUser("userId", _.userId),
        totalDistanceByUser("distance", _.distance)
      ).mapN(TotalDistanceByUser.apply)
    }

  given currentSpeedCodec: Codec[CurrentSpeed] =
    Codec.record("CurrentSpeed", _namespace) { currentSpeed =>
      (
        currentSpeed("tripId", _.tripId),
        currentSpeed("speed", _.speed)
      ).mapN(CurrentSpeed.apply)
    }

  given totalRangeCodec: Codec[TotalRange] =
    Codec.record("TotalRange", _namespace) { totalRange =>
      (
        totalRange("tripId", _.tripId),
        totalRange("bicycleId", _.bicycleId),
        totalRange("remainingRange", _.remainingRange)
      ).mapN(TotalRange.apply)
    }
