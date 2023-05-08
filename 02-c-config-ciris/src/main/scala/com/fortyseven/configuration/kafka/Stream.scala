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

package com.fortyseven.configuration.kafka

import scala.concurrent.duration.{DurationInt, FiniteDuration}

import cats.syntax.all.*
import ciris.refined.*
import ciris.{default, ConfigValue, Effect}
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

case class Stream(
    inputTopic: NonEmptyString,
    outputTopic: NonEmptyString,
    maxConcurrent: PosInt,
    commitBatchWithinSize: PosInt,
    commitBatchWithinTime: FiniteDuration
  )

object Stream:

  val config: ConfigValue[Effect, Stream] =
    (
      default("input-topic").as[NonEmptyString],
      default("output-topic").as[NonEmptyString],
      default(Int.MaxValue).as[PosInt],
      default(500).as[PosInt],
      default(15.seconds).as[FiniteDuration]
    ).parMapN(Stream.apply)

end Stream
