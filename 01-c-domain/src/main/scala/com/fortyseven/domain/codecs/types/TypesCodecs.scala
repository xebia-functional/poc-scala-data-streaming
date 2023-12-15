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

package com.fortyseven.domain.codecs.types

import cats.implicits.*

import com.fortyseven.domain.model.types.refinedTypes.*

import vulcan.AvroError
import vulcan.Codec

/**
 * It contains the Vulcan codecs for the types (enums, case classes...) defined in the object [[com.fortyseven.domain.model.types.refinedTypes]].
 */
object TypesCodecs:

  given latitudeCodec: Codec[Latitude] =
    Codec.double.imapError(Latitude.from(_).leftMap(e => AvroError(s"AvroError: ${e.message}")))(_.value)

  given longitudeCodec: Codec[Longitude] =
    Codec.double.imapError(Longitude.from(_).leftMap(e => AvroError(s"AvroError: ${e.message}")))(_.value)

  given percentageCodec: Codec[Percentage] =
    Codec.double.imapError(Percentage.from(_).leftMap(e => AvroError(s"AvroError: ${e.message}")))(_.value)

  given speedCodec: Codec[Speed] =
    Codec.double.imapError(Speed.from(_).leftMap(e => AvroError(s"AvroError: ${e.message}")))(_.value)

  given hertzCodec: Codec[Hz] = Codec.double.imapError(Hz.from(_).leftMap(e => AvroError(s"AvroError: ${e.message}")))(_.value)

  given barCodec: Codec[Bar] = Codec.double.imapError(Bar.from(_).leftMap(e => AvroError(s"AvroError: ${e.message}")))(_.value)

  given metersCodec: Codec[Meters] =
    Codec.int.imapError(Meters.from(_).leftMap(e => AvroError(s"AvroError: ${e.message}")))(_.value)
