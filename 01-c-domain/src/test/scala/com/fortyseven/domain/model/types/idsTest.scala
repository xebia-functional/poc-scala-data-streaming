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

import java.util.UUID

import com.fortyseven.domain.model.types.ids.BicycleId
import com.fortyseven.domain.model.types.ids.TripId
import com.fortyseven.domain.model.types.ids.UserId

import munit.ScalaCheckSuite
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class idsTest extends ScalaCheckSuite:

  given Arbitrary[UUID] = Arbitrary(Gen.uuid)

  property("BicycleId should build from a valid UUID and method call value should return the same UUID"):
    forAll: (uuid: UUID) =>
      assertEquals(BicycleId(uuid).value, uuid)

  property("UserId should build from a valid UUID and method call value should return the same UUID"):
    forAll: (uuid: UUID) =>
      assertEquals(UserId(uuid).value, uuid)

  property("TripId should build from a valid UUID and method call value should return the same UUID"):
    forAll: (uuid: UUID) =>
      assertEquals(TripId(uuid).value, uuid)

end idsTest
