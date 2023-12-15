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

package com.fortyseven.common.configuration

import com.fortyseven.common.configuration.refinedTypes.KafkaAutoOffsetReset
import com.fortyseven.common.configuration.refinedTypes.KafkaCompressionType
import com.fortyseven.common.configuration.refinedTypes.NonEmptyString
import com.fortyseven.common.configuration.refinedTypes.PositiveInt

import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class refinedTypesTest extends ScalaCheckSuite:

  property("A valid value of KafkaCompressionType should return Right"):
    forAll(Gen.oneOf(KafkaCompressionType.values.toIndexedSeq).map(_.toString)): kct =>
      KafkaCompressionType.from(kct).isRight

  property("An invalid KafkaCompressionType should return Left"):
    forAll(Gen.alphaStr.suchThat(randomString => !KafkaCompressionType.values.map(_.toString).contains(randomString))): kct =>
      KafkaCompressionType.from(kct).isLeft

  property("A valid value of KafkaAutoOffsetReset should return Right"):
    forAll(Gen.oneOf(KafkaAutoOffsetReset.values.toIndexedSeq).map(_.toString)): kct =>
      KafkaAutoOffsetReset.from(kct).isRight

  property("An invalid KafkaAutoOffsetReset should return Left"):
    forAll(Gen.alphaStr.suchThat(randomString => !KafkaAutoOffsetReset.values.map(_.toString).contains(randomString))): kct =>
      KafkaAutoOffsetReset.from(kct).isLeft

  property("Any String that is empty or has only spaces should return Left"):
    forAll(Gen.oneOf(IndexedSeq("", " ", "  ", "    "))): string =>
      NonEmptyString.from(string).isLeft

  property("Any string that is not empty or blank should return a valid NonEmptyString"):
    forAll(Gen.alphaStr.suchThat(string => string.nonEmpty)): string =>
      NonEmptyString.from(string).isRight

  property("Any Integer that is negative should return Left"):
    forAll(Gen.negNum[Int]): negative =>
      PositiveInt.from(negative).isLeft

  property("Any Integer that is zero or positive should return a valid PositiveInt"):
    forAll(Gen.posNum[Int]): positive =>
      PositiveInt.from(positive).isRight
