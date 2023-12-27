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

package com.fortyseven.pureconfig

import cats.syntax.all.*

import com.fortyseven.common.configuration.*
import com.fortyseven.common.configuration.refinedTypes.*

import pureconfig.ConfigReader
import pureconfig.error.ExceptionThrown

/** It contains the givens for the types that pureConfig does not have automatic derivation.
  */
object refinedTypesGivens:

  given ConfigReader[NonEmptyString] = ConfigReader.fromString(NonEmptyString.from(_).leftMap(ExceptionThrown.apply))

  given ConfigReader[PositiveInt] = ConfigReader[Int].emap(PositiveInt.from(_).leftMap(ExceptionThrown.apply))

  given ConfigReader[KafkaAutoOffsetReset] = ConfigReader
    .fromString(KafkaAutoOffsetReset.from(_).leftMap(ExceptionThrown.apply))

  given ConfigReader[KafkaCompressionType] = ConfigReader
    .fromString(KafkaCompressionType.from(_).leftMap(ExceptionThrown.apply))
