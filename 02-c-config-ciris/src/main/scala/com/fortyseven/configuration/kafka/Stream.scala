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

import ciris.refined.*
import ciris.{default, ConfigValue, Effect}
import com.fortyseven.coreheaders.config.StreamHeader
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

private[kafka] final case class Stream(
    _inputTopic: NonEmptyString,
    _outputTopic: NonEmptyString,
    _maxConcurrent: PosInt,
    _commitBatchWithinSize: PosInt,
    _commitBatchWithinTime: FiniteDuration
  ) extends StreamHeader:

  override val inputTopic: String = _inputTopic.toString

  override val outputTopic: String = _outputTopic.toString

  override val maxConcurrent: Int = _maxConcurrent.toString.toInt

  override val commitBatchWithinSize: Int = _commitBatchWithinSize.toString.toInt

  override val commitBatchWithinTime: FiniteDuration = _commitBatchWithinTime

private[kafka] object Stream:

  val config: ConfigValue[Effect, Stream] =
    for
      _inputTopic            <- default("data-generator").as[NonEmptyString]
      _outputTopic           <- default("input-topic").as[NonEmptyString]
      _maxConcurrent         <- default(25).as[PosInt]
      _commitBatchWithinSize <- default(500).as[PosInt]
      _commitBatchWithinTime <- default(15.seconds).as[FiniteDuration]
    yield Stream.apply(
      _inputTopic,
      _outputTopic,
      _maxConcurrent,
      _commitBatchWithinSize,
      _commitBatchWithinTime
    )
