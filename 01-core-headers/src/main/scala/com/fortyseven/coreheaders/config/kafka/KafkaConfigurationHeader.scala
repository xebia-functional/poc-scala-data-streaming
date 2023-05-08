package com.fortyseven.coreheaders.config.kafka

import ciris.{ConfigValue, Effect}

trait KafkaConfigurationHeader[KC]:
  val config: ConfigValue[Effect, KC]
