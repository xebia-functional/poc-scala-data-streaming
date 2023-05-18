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

package com.fortyseven.configuration

import munit.CatsEffectSuite
import cats.effect.IO
import com.fortyseven.coreheaders.config.KafkaConsumerConfig
import org.typelevel.log4cats.slf4j.Slf4jLogger

class KafkaConsumerConfigurationTest extends CatsEffectSuite:

  val configuration: KafkaConsumerConfiguration[IO] = new KafkaConsumerConfiguration[IO]

  test("config must load and be equal") {
    for
      logger <- Slf4jLogger.create[IO]
      conf1  <- configuration.load
      _      <- logger.info(conf1.toString)
      conf2  <- configuration.config("kafka/kafkaConsumer.conf").load[IO]
      _      <- logger.info(conf2.toString)
    yield assertEquals(conf1, conf2)

  }

  test("config must load and be different") {
    for
      logger <- Slf4jLogger.create[IO]
      conf1  <- configuration.load
      _      <- logger.info(conf1.toString)
      conf2  <- configuration.config("kafka/kafkaConsumer2.conf").load[IO]
      _      <- logger.info(conf2.toString)
    yield assertNotEquals(conf1, conf2)

  }

  test("config must not load because of negative int") {

    val maybeNot: IO[Either[Throwable, KafkaConsumerConfig]] = for
      logger <- Slf4jLogger.create[IO]
      conf   <- configuration.config("kafka/kafkaConsumerPosInt.conf").load[IO].attempt
      _      <- logger.info(conf.toString)
    yield conf
    assertIOBoolean(maybeNot.map(_.isLeft))

  }

  test("config must not load because of empty string") {

    val maybeNot: IO[Either[Throwable, KafkaConsumerConfig]] = for
      logger <- Slf4jLogger.create[IO]
      conf   <- configuration.config("kafka/kafkaConsumerEmptyString.conf").load[IO].attempt
      _      <- logger.info(conf.toString)
    yield conf
    assertIOBoolean(maybeNot.map(_.isLeft))

  }
