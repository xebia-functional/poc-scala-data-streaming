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

package com.fortyseven.core.codecs.iot

import cats.implicits.*
import com.fortyseven.coreheaders.codecs.iot.IotTypes
import com.fortyseven.coreheaders.model.iot.types.*
import vulcan.{AvroError, Codec}

object IotTypes extends IotTypes[Codec]:

  given latitudeCodec: Codec[Latitude] = ???

  given longitudeCodec: Codec[Longitude] = ???

  given percentageCodec: Codec[Percentage] = ???

  given speedCodec: Codec[Speed] = ???

  given hertzCodec: Codec[Hz] = ???

  given barCodec: Codec[Bar] = Codec.double.imapError(Bar(_).leftMap(e => AvroError(s"AvroError: ${e.msg}")))(_.value)
