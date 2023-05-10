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

import cats.syntax.all.*
import ciris.refined.*
import ciris.{default, ConfigValue, Effect}
import com.fortyseven.coreheaders.config.ConsumerHeader
import eu.timepit.refined.types.string.NonEmptyString
import fs2.kafka.AutoOffsetReset

private[kafka] final case class Consumer(
    autoOffsetReset: AutoOffsetReset,
    _groupId: NonEmptyString
  ) extends ConsumerHeader:

  override val groupId: String = _groupId.toString

private[kafka] object Consumer:

  val config: ConfigValue[Effect, Consumer] = (
    default(AutoOffsetReset.Earliest).as[AutoOffsetReset],
    default("groupId").as[NonEmptyString]
  ).parMapN(Consumer.apply)
