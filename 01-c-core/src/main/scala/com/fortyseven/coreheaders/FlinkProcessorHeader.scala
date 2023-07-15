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

package com.fortyseven.coreheaders

/**
 * @tparam F
 *   The effect in with Flink is executed.
 * @tparam ConfigurationLoader
 *   The type of the Flink Processor Configurator.
 */
trait FlinkProcessorHeader[F[_], ConfigurationLoader]:
  /**
   * @param config
   *   An instance of a class that extends [[ConfigurationHeader]].
   * @return
   *   It executes the effects of the Flink Processor and returns Unit.
   */
  def process(config: ConfigurationHeader[F, ConfigurationLoader]): F[Unit]
