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

package com.fortyseven.domain.codecs.iot

import com.fortyseven.domain.model.iot.errors.OutOfBoundsError

import vulcan.Codec

/** It contains the Vulcan codecs for the types (enums, case classes...) defined in the object
  * [[com.fortyseven.domain.model.iot.errors]].
  */
object IotErrorCodecs:

  private val _namespace = "iot-error"

  given outOfBoundsErrorCodec: Codec[OutOfBoundsError] = Codec
    .record(name = "OutOfBoundsError", namespace = _namespace)(_("msg", _.message).map(OutOfBoundsError.apply))
