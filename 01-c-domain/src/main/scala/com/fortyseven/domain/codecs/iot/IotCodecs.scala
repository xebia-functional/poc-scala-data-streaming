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

import cats.implicits.*

import scala.concurrent.duration.*

import com.fortyseven.domain.codecs.types.TypesCodecs.given
import com.fortyseven.domain.model.iot.model.*

import vulcan.Codec

/** It contains the Vulcan codecs for the types (enums, case classes...) defined in the object
  * [[com.fortyseven.domain.model.iot.model]].
  */
object IotCodecs:

  private val _namespace = "iot"

  given gpsPositionCodec: Codec[GPSPosition] = Codec.record(name = "GPSPosition", namespace = _namespace) { field =>
    (field("latitude", _.latitude), field("longitude", _.longitude)).mapN(GPSPosition.apply)
  }

  given wheelRotationCodec: Codec[WheelRotation] =
    import com.fortyseven.domain.codecs.types.TypesCodecs.hertzCodec
    Codec.record(name = "WheelRotation", namespace = _namespace)(_("s", _.frequency).map(WheelRotation.apply))

  given batteryChargeCodec: Codec[BatteryCharge] = Codec
    .record(name = "BatteryCharge", namespace = _namespace)(_("percentage", _.percentage).map(BatteryCharge.apply))

  given batteryHealthCodec: Codec[BatteryHealth] = Codec
    .record(name = "BatteryHealth", namespace = _namespace)(_("remaining", _.remaining).map(BatteryHealth.apply))

  given pneumaticPressureCodec: Codec[PneumaticPressure] =
    import com.fortyseven.domain.codecs.types.TypesCodecs.barCodec
    Codec.record(name = "PneumaticPressure", namespace = _namespace)(
      _("pressure", _.pressure).map(PneumaticPressure.apply)
    )

  given finiteDurationCodec: Codec[FiniteDuration] = Codec.long.imap(_.millis)(_.toMillis)

  given breaksUsageCodec: Codec[BreaksUsage] = Codec
    .record(name = "BreaksUsage", namespace = _namespace)(_("finateDuration", _.finiteDuration).map(BreaksUsage.apply))

  given breaksHealthCodec: Codec[BreaksHealth] = Codec
    .record(name = "BreaksHealth", namespace = _namespace)(_("remaining", _.remaining).map(BreaksHealth.apply))

end IotCodecs
