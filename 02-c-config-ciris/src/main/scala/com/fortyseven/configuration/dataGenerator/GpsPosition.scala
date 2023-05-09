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

package com.fortyseven.configuration.dataGenerator

import cats.syntax.all.*
import ciris.refined.*
import ciris.{ConfigValue, Effect, default}
import eu.timepit.refined.types.numeric.PosDouble

private [dataGenerator] final case class GpsPosition (
  latitudeValue: PosDouble,
  longitudeValue: PosDouble
)

private [dataGenerator] object GpsPosition:
  val config: ConfigValue[Effect, GpsPosition] =
    (
      default(2.0).as[PosDouble],
      default(2.0).as[PosDouble]
    ).parMapN(GpsPosition.apply)