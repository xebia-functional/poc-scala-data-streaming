package com.fortyseven.datagenerator

import cats.effect.{IO, IOApp}

import com.fortyseven.datagenerator.config.DataGeneratorConfiguration
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import pureconfig.module.catseffect.syntax.*

object Main extends IOApp.Simple:

  override def run: IO[Unit] = for
    logger <- Slf4jLogger.create[IO]
    dataGenConf <- ConfigSource.default.at("data-generator").loadF[IO, DataGeneratorConfiguration]()
    _ <- logger.info(s"DataGeneratorConfiguration: $dataGenConf")
    _ <- logger.info("Start data generator")
    _ <- new DataGenerator[IO].generate(dataGenConf)
  yield ()
