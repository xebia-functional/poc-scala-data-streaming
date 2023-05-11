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

import org.apache.kafka.common.record.CompressionType

import ciris.refined.*
import ciris.{default, ConfigValue, Effect}
import com.fortyseven.coreheaders.config.ProducerHeader
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

private[kafka] final case class Producer(
    _maxConcurrent: PosInt,
    compressionType: CompressionType
  ) extends ProducerHeader:

  override val maxConcurrent: Int = _maxConcurrent.toString.toInt

private[kafka] object Producer:

  val config: ConfigValue[Effect, Producer] =
    for
      _maxConcurrent  <- default(Int.MaxValue).as[PosInt]
      compressionType <- default(CompressionType.LZ4).as[CompressionType]
    yield Producer.apply(_maxConcurrent, compressionType)
