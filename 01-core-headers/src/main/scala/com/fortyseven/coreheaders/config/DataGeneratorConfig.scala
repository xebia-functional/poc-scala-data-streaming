package com.fortyseven.coreheaders.config

import com.fortyseven.coreheaders.config.internal.KafkaConfig.KafkaConf
import com.fortyseven.coreheaders.config.internal.SchemaRegistryConfig.SchemaRegistryConf

case class DataGeneratorConfig(kafkaConf: KafkaConf, schemaRegistryConf: SchemaRegistryConf)
