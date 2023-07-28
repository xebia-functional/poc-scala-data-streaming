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

package com.fortyseven.core.codecs.ids

import com.fortyseven.coreheaders.model.types.ids.{BicycleId, TripId, UserId}
import vulcan.Codec

/**
 * It contains the Vulcan codecs for the types (enums, case classes...) defined in the object [[com.fortyseven.coreheaders.model.types.ids]].
 */
object IdsCodecs:

  given bicycleIdCodec: Codec[BicycleId] = Codec.uuid.imap(BicycleId.apply)(_.value)

  given userIdCodec: Codec[UserId] = Codec.uuid.imap(UserId.apply)(_.value)

  given tripIdCodec: Codec[TripId] = Codec.uuid.imap(TripId.apply)(_.value)
