package com.fortyseven.common.configuration

import com.fortyseven.common.configuration.refinedTypes.{KafkaAutoOffsetReset, KafkaCompressionType, NonEmptyString, PositiveInt}
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

  property("Any string that is not empty or blanck should return a valid NonEmptyString"):
    forAll(Gen.alphaStr.suchThat(string => string.nonEmpty)): string =>
       NonEmptyString.from(string).isRight

  property("Any Integer that is negative should return Left"):
    forAll(Gen.negNum[Int]): negative =>
      PositiveInt.from(negative).isLeft

  property("Any Integer that is zero or positive should return a valid PositiveInt"):
    forAll(Gen.posNum[Int]): positive =>
      PositiveInt.from(positive).isRight