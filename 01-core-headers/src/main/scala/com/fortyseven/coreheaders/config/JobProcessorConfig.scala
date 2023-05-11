package com.fortyseven.coreheaders.config

import com.fortyseven.coreheaders.config.internal.KafkaConfig.KafkaConf

case class JobProcessorConfig(kafkaConf: KafkaConf)
