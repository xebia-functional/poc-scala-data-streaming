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

import scala.reflect.ClassTag

import cats.effect.kernel.Async
import com.fortyseven.coreheaders.ConfigurationLoaderHeader
import com.fortyseven.coreheaders.configuration.JobProcessorConfiguration
import pureconfig.module.catseffect.syntax.*
import pureconfig.{ConfigReader, ConfigSource}

abstract class PureConfiguration[F[_]: Async, A: ConfigReader: ClassTag](path: String)
    extends ConfigurationLoaderHeader[F, A]:

  override def load(configurationPath: Option[String]): F[A] =
    configurationPath.fold(ConfigSource.default)(ConfigSource.resources).at(path).loadF[F, A]()
