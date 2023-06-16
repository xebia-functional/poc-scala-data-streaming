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
import com.fortyseven.core.codecs.types.TypesCodecs.given
import com.fortyseven.coreheaders.model.iot.model.*
import com.fortyseven.coreheaders.model.types.types.*
import vulcan.Codec

object IotCodecs:

  private val _namespace = "iot"

  given gpsPositionCodec: Codec[GPSPosition] =
    Codec.record(name = "GPSPosition", namespace = _namespace) { field =>
      (
        field("latitude", _.latitude),
        field("longitude", _.longitude)
      ).mapN(GPSPosition.apply)
    }

  given wheelRotationCodec: Codec[WheelRotation] =
    Codec.record(name = "WheelRotation", namespace = _namespace)(_("s", _.s).map(WheelRotation.apply))

  given batteryChargeCodec: Codec[BatteryCharge] =
    Codec.record(name = "BatteryCharge", namespace = _namespace)(_("percentage", _.percentage).map(BatteryCharge.apply))

  given batteryHealthCodec: Codec[BatteryHealth] =
    Codec.record(name = "BatteryHealth", namespace = _namespace)(_("remaining", _.remaining).map(BatteryHealth.apply))

  given pneumaticPressureCodec: Codec[PneumaticPressure] =
    Codec.record(name = "PneumaticPressure", namespace = _namespace)(
      _("pressure", _.pressure).map(PneumaticPressure.apply)
    )

  given breaksUsageCodec: Codec[BreaksUsage] = ???
  // TODO: missing argument for parameter codec of method apply in class FieldBuilder: (implicit codec: vulcan.Codec[scala.concurrent.duration.Duration])
  // Codec.record(name = "BreaksUsage", namespace = _namespace)(_("duration", _.duration).map(BreaksUsage.apply))

  given breaksHealthCodec: Codec[BreaksHealth] =
    Codec.record(name = "BreaksHealth", namespace = _namespace)(_("remaining", _.remaining).map(BreaksHealth.apply))
