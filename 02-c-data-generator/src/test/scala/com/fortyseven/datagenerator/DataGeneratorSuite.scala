package com.fortyseven.datagenerator

import cats.effect.IO
import cats.implicits.*
import munit.{CatsEffectSuite, ScalaCheckSuite}
import org.scalacheck.*
import org.scalacheck.Prop.*

class DataGeneratorSuite extends CatsEffectSuite with ScalaCheckSuite:

  private val dataGenerator = new DataGenerator[IO]

  property("generatePneumaticPressure - check no negative pressures") {
    forAll(Gen.choose(1, 20)) { (size: Int) =>
      dataGenerator.generatePneumaticPressure
        .take(size).compile.toList.unsafeRunSync().foreach(it => assert(it.pressure.value >= 0.0))
    }
  }
