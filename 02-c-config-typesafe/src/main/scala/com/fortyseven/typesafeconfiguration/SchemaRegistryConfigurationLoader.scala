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

package com.fortyseven.typesafeconfiguration

import com.fortyseven.coreheaders.configuration.internal.SchemaRegistryConfiguration
import com.fortyseven.coreheaders.configuration.internal.types.NonEmptyString
import com.typesafe.config.{Config, ConfigFactory}

private[typesafeconfiguration] final class SchemaRegistryConfigurationLoader(
    className: String,
    configurationPath: Option[String] = None
  ):

  private val schemaRegistry: Config = {
    if configurationPath.isEmpty then ConfigFactory.load()
    else ConfigFactory.load(configurationPath.get)
  }.getConfig(className).getConfig("SchemaRegistryConfiguration")

  private val schemaRegistryURL: Either[Throwable, NonEmptyString] =
    NonEmptyString.from(schemaRegistry.getString("schemaRegistryURL"))

  val eitherLoad: Either[Throwable, SchemaRegistryConfiguration] =
    for sru <- schemaRegistryURL yield SchemaRegistryConfiguration(sru)

object SchemaRegistryConfigurationLoader:

  def apply(className: String, configurationPath: Option[String]): Either[Throwable, SchemaRegistryConfiguration] =
    new SchemaRegistryConfigurationLoader(className, configurationPath).eitherLoad
