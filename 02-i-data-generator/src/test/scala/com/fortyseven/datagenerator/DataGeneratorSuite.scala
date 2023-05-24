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

import scala.concurrent.duration.*

import cats.effect.syntax.temporal.*
import cats.effect.{IO, Temporal}
import cats.implicits.*
import munit.{CatsEffectSuite, ScalaCheckEffectSuite}
import org.scalacheck.Prop.*
import org.scalacheck.*
import org.scalacheck.effect.PropF
import org.typelevel.log4cats.slf4j.Slf4jLogger

class DataGeneratorSuite extends CatsEffectSuite with ScalaCheckEffectSuite:

  private val dataGenerator = new ModelGenerators[IO](1.millisecond)

  test("generateGPSPosition") {
    PropF.forAllF(Gen.choose(1, 20)) { (sampleSize: Int) =>
      for
        logger <- Slf4jLogger.create[IO]
        sample <- dataGenerator.generateGPSPosition.take(sampleSize).compile.toList
        _      <- logger.debug("Assert coordinates are in desired bounds")
        _       = sample.foreach(it => assert(it.latitude >= -90.0 && it.latitude <= 90.0))
        _       = sample.foreach(it => assert(it.longitude >= -180.0 && it.longitude <= 180.0))
        _      <- logger.debug("Assert small coordinate changes")
        _       = sample
                    .sliding(2).map(l =>
                      (math.abs(l.head.latitude - l.last.latitude), math.abs(l.head.longitude - l.last.longitude))
                    ).foreach(it => assert(it._1 <= 1e-3 && it._2 <= 1e-3))
      yield ()
    }
  }

  test("generatePneumaticPressure") {
    PropF.forAllF(Gen.choose(1, 20)) { (sampleSize: Int) =>
      for
        logger <- Slf4jLogger.create[IO]
        sample <- dataGenerator.generatePneumaticPressure.take(sampleSize).compile.toList
        _      <- logger.debug("Assert pressures are positive")
        _       = sample.foreach(it => assert(it.pressure > 0.0))
        _      <- logger.debug("Assert small pressure decrements")
        _       = sample.sliding(2).map(l => l.head.pressure - l.last.pressure).foreach(it => assert(it >= 0 && it <= 1e-3))
      yield ()
    }
  }
