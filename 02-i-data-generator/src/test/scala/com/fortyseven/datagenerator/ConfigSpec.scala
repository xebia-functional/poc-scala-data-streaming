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

import com.fortyseven.datagenerator.config.DataGeneratorConfiguration
import munit.CatsEffectSuite
import pureconfig.ConfigSource
import pureconfig.module.catseffect.syntax.*

class ConfigSpec extends CatsEffectSuite:

  test("Load data generator config"):
    assertIO_(ConfigSource.default.at("data-generator").loadF[IO, DataGeneratorConfiguration]().void)
