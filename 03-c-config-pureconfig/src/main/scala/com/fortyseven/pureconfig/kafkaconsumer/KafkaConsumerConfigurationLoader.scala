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

package com.fortyseven.pureconfig.kafkaconsumer

import cats.effect.kernel.Async

import com.fortyseven.common.api.ConfigurationAPI

import pureconfig.ConfigSource
import pureconfig.module.catseffect.syntax.*

final class KafkaConsumerConfigurationLoader[F[_]: Async] extends ConfigurationAPI[F, KafkaConsumerConfiguration]:

  /** @return
    *   An instance of the the class [Configuration] wrapped into an effect of type [Effect]. The return type of the
    *   method is mappable or flatMappable since it is wrapped into an effect. Thus, you can use it in a
    *   for-comprehension. If the value of the configuration fails to be loaded, the effect will handle the error
    *   gracefully.
    */
  override def load(): F[KafkaConsumerConfiguration] = ConfigSource
    .default
    .at("kafka-consumer")
    .loadF[F, KafkaConsumerConfiguration]()
