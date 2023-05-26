package com.fortyseven.typesafeconfiguration

import cats.effect.IO
import munit.CatsEffectSuite

class ConfigSpec extends CatsEffectSuite:

  test("Load data generator config") {
    assertIO_(DataGeneratorConfigurationLoader.apply[IO].load().void)
  }

  test("Load job processor config") {
    assertIO_(JobProcessorConfigurationLoader.apply[IO].load().void)
  }

  test("Load kafka configuration loader config") {
    assertIO_(KafkaConsumerConfigurationLoader.apply[IO].load().void)
  }
