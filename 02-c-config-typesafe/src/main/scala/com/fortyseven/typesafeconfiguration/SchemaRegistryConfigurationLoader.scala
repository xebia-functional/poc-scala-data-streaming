package com.fortyseven.typesafeconfiguration

import com.fortyseven.typesafeconfiguration.configTypes.{NonEmptyString, SchemaRegistryConfiguration}
import com.typesafe.config.{Config, ConfigFactory}

object SchemaRegistryConfigurationLoader:

  private val schemaRegistry: Config                               =
    ConfigFactory.load("schemaRegistry.conf").getConfig("SchemaRegistryConfiguration")

  private val schemaRegistryURL: Either[Throwable, NonEmptyString] =
    NonEmptyString.from(schemaRegistry.getString("schemaRegistryURL"))

  val eitherLoad: Either[Throwable, SchemaRegistryConfiguration] =
    for sru <- schemaRegistryURL yield SchemaRegistryConfiguration(sru)
