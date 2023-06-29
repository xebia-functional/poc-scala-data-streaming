package com.fortyseven.datagenerator

import cats.effect.IO
import com.fortyseven.datagenerator.config.DataGeneratorConfiguration
import munit.CatsEffectSuite
import pureconfig.ConfigSource
import pureconfig.module.catseffect.syntax.*

class ConfigSpec extends CatsEffectSuite:

  test("Load data generator config"):
    assertIO_(ConfigSource.default.at("data-generator").loadF[IO, DataGeneratorConfiguration]().void)
