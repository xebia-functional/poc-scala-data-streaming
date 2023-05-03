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

package com.fortyseven.coreheaders.model.app

import java.util.UUID

object types:

  opaque type Meters <: Int = Int

  opaque type Kilometers <: Double = Double

  object Meters:

    def apply(meters: Int): Meters = meters

    extension (meters: Meters)

      def value: Int = meters

      def toKilometers: Kilometers = meters.toDouble / 1000

  object Kilometers:

    def apply(kilometers: Double): Kilometers = kilometers

    extension (kilometers: Kilometers) def value: Double = kilometers
