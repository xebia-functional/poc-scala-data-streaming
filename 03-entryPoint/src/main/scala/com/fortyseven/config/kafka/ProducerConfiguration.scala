package com.fortyseven.config.kafka

import ciris.{default, ConfigValue, Effect}
import org.apache.kafka.common.record.CompressionType

private[kafka] final case class ProducerConfiguration(
    compressionType: CompressionType
  )

private[kafka] object ProducerConfiguration:

  val config: ConfigValue[Effect, ProducerConfiguration] =
    default(CompressionType.LZ4).as[CompressionType].map(ProducerConfiguration.apply)
