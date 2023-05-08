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

package com.fortyseven.coreheaders.codecs

import cats.implicits.*
import com.fortyseven.coreheaders.model.iot.errors.OutOfBoundsError
import com.fortyseven.coreheaders.model.iot.model.PneumaticPressure
import com.fortyseven.coreheaders.model.iot.types.Bar
import vulcan.{AvroError, Codec}

object Codecs:

  private val namespace = "iot"

  given barCodec: Codec[Bar] =
    Codec.double.imapError(Bar(_).leftMap(e => AvroError(s"AvroError: ${e.msg}")))(_.value)

  given pneumaticPressureCodec: Codec[PneumaticPressure] =
    Codec.record(name = "PneumaticPressure", namespace = namespace)(
      _("pressure", _.pressure).map(PneumaticPressure.apply)
    )
