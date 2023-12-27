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

package com.fortyseven.main

import cats.effect.IO
import cats.effect.IOApp

import com.fortyseven.cirisconfiguration.datagenerator.DataGeneratorConfigurationLoader
import com.fortyseven.datagenerator.DataGenerator

import org.typelevel.log4cats.slf4j.Slf4jLogger

object DataGeneratorCirisMain extends IOApp.Simple:

  override def run: IO[Unit] =
    for
      logger <- Slf4jLogger.create[IO]
      _ <- logger.info("Loading Data Generator Configuration")
      dataGenConf <- DataGeneratorConfigurationLoader[IO].load()
      _ <- logger.info("Start data generator")
      _ <- new DataGenerator[IO].generate(dataGenConf)
    yield ()
