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

package com.fortyseven.datagenerator

import cats.effect.IO

import scala.concurrent.duration.*

import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite
import org.scalacheck.*
import org.scalacheck.effect.PropF

class DataGeneratorSuite extends CatsEffectSuite with ScalaCheckEffectSuite:

  private val dataGenerator = new ModelGenerators[IO](1.millisecond)

  test("generateGPSPosition"):
    PropF.forAllF(Gen.choose(1, 20)) { (sampleSize: Int) =>
      for
        sample <- dataGenerator.generateGPSPosition.take(sampleSize).compile.toList
        _ = sample.foreach(it => assert(it.latitude.value >= -90.0 && it.latitude.value <= 90.0))
        _ = sample.foreach(it => assert(it.longitude.value >= -180.0 && it.longitude.value <= 180.0))
        _ = sample
          .sliding(2)
          .map(l =>
            (
              math.abs(l.head.latitude.value - l.last.latitude.value),
              math.abs(l.head.longitude.value - l.last.longitude.value)
            )
          )
          .foreach(it => assert(it._1 <= 1e-3 && it._2 <= 1e-3))
      yield ()
    }

  test("generatePneumaticPressure"):
    PropF.forAllF(Gen.choose(1, 20)) { (sampleSize: Int) =>
      for
        sample <- dataGenerator.generatePneumaticPressure.take(sampleSize).compile.toList
        _ = sample.foreach(it => assert(it.pressure.value > 0.0))
        _ = sample
          .sliding(2)
          .map(l => l.head.pressure.value - l.last.pressure.value)
          .foreach(it => assert(it >= 0 && it <= 1e-3))
      yield ()
    }

end DataGeneratorSuite
