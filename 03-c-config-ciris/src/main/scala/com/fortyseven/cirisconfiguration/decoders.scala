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

package com.fortyseven.cirisconfiguration

import com.fortyseven.common.configuration.refinedTypes.NonEmptyString
import com.fortyseven.common.configuration.refinedTypes.PositiveInt

import ciris.ConfigDecoder
import ciris.ConfigError

object decoders:

  given ConfigDecoder[Int, PositiveInt] =
    ConfigDecoder[Int, Int].mapEither { (None, i) =>
      PositiveInt.from(i) match
        case Right(value) => Right(value)
        case Left(throwable) =>
          Left(ConfigError.apply(s"Failing at decoding the value $i. Error: ${throwable.getMessage}"))
    }

  given ConfigDecoder[String, NonEmptyString] =
    ConfigDecoder[String, String].mapEither { (None, s) =>
      NonEmptyString.from(s) match
        case Right(value) => Right(value)
        case Left(throwable) =>
          Left(ConfigError.apply(s"Failing at decoding the value $s. Error: ${throwable.getMessage}"))
    }
