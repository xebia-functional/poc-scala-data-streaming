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

package com.fortyseven.domain.model.types

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import io.github.iltotore.iron.constraint.numeric.Interval.Closed

/** Contains the types of the business logic.
  *
  * __Factories of the refined types:__
  *
  * The companion objects of the types have two constructors: `from` and `apply`.
  *
  *   - `from` is used for unknown values and it catches an exception at runtime if the values are not valid.
  *
  *   - `apply` is used for ''magic numbers'' and it verifies the validity of the value at compile time using inlining.
  *
  * @see
  *   inline internals at [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html]]
  */
object refinedTypes:

  type Latitude = Double :| Closed[-90.0, 90.0]

  object Latitude extends RefinedTypeOps[Double, Closed[-90.0, 90.0], Latitude]

  type Longitude = Double :| Closed[-180.0, 180.0]

  object Longitude extends RefinedTypeOps[Double, Closed[-180.0, 180.0], Longitude]

  type Percentage = Double :| Closed[0.0, 100.0]

  object Percentage extends RefinedTypeOps[Double, Closed[0.0, 100.0], Percentage]

  type Speed = Double :| GreaterEqual[0.0]

  object Speed extends RefinedTypeOps[Double, GreaterEqual[0.0], Speed]

  type Hz = Double :| GreaterEqual[0.0]

  object Hz extends RefinedTypeOps[Double, GreaterEqual[0.0], Hz]

  type Bar = Double :| GreaterEqual[0.0]

  object Bar extends RefinedTypeOps[Double, GreaterEqual[0.0], Bar]

  type Meters = Int :| GreaterEqual[0]

  object Meters extends RefinedTypeOps[Int, GreaterEqual[0.0], Meters]
end refinedTypes
