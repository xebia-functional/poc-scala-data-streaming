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

package com.fortyseven.config.kafka

import ciris.*
import ciris.refined.*
import eu.timepit.refined.types.string.NonEmptyString
import cats.syntax.all.*
import eu.timepit.refined.types.numeric.PosInt

import scala.concurrent.duration.*


private [kafka] final case class StreamConfiguration (
  inputStream: NonEmptyString,
  outputStream: NonEmptyString,
  maxConcurrent: PosInt,
  commitBatchWithin: (Int, FiniteDuration)
)

private [kafka] object StreamConfiguration:
  val config: ConfigValue[Effect, StreamConfiguration] =
    (
      default("input-topic").as[NonEmptyString],
      default("output-topic").as[NonEmptyString],
      default(Int.MaxValue).as[PosInt],
      default((500, 15.seconds)).as[(Int, FiniteDuration)]
    ).parMapN(StreamConfiguration.apply)