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

package com.fortyseven.coreheaders.codecs.iot

import com.fortyseven.coreheaders.model.iot.model.*

trait IotModel[Codec[_]]:

  given gpsPositionCodec: Codec[GPSPosition]

  given wheelRotationCodec: Codec[WheelRotation]

  given batteryChargeCodec: Codec[BatteryCharge]

  given batteryHealthCodec: Codec[BatteryHealth]

  given pneumaticPressureCodec: Codec[PneumaticPressure]

  given breaksUsageCodec: Codec[BreaksUsage]

  given breaksHealthCodec: Codec[BreaksHealth]
