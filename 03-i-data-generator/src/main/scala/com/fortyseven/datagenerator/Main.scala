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

import cats.effect.{IO, IOApp}

import com.fortyseven.datagenerator.configuration.DataGeneratorConfiguration
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import pureconfig.module.catseffect.syntax.*

object Main extends IOApp.Simple:

  override def run: IO[Unit] = for
    logger      <- Slf4jLogger.create[IO]
    dataGenConf <- ConfigSource.default.at("data-generator").loadF[IO, DataGeneratorConfiguration]()
    _           <- logger.info(s"DataGeneratorConfiguration: $dataGenConf")
    _           <- logger.info("Start data generator")
    _           <- new DataGenerator[IO].generate(dataGenConf)
  yield ()
