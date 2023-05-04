package com.fortyseven.config.kafka

import ciris.{default, ConfigValue, Effect}

private[kafka] final case class ConsumerConfiguration(
    groupId: String
  )

private[kafka] object ConsumerConfiguration:

  val config: ConfigValue[Effect, ConsumerConfiguration] =
    default("").as[String].map(ConsumerConfiguration.apply)
