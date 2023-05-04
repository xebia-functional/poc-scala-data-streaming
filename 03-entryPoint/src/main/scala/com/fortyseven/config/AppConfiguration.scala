package com.fortyseven.config

import ciris.*
import com.fortyseven.config.kafka.KafkaConfiguration

final case class AppConfiguration(
    kafkaConfiguration: KafkaConfiguration
  )

object AppConfiguration:

  val config: ConfigValue[Effect, AppConfiguration] =
    KafkaConfiguration.config.map(AppConfiguration.apply)
