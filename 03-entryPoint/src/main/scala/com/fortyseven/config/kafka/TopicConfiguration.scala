package com.fortyseven.config.kafka

import ciris.{default, ConfigValue, Effect}
import cats.syntax.all.*
import org.apache.kafka.common.record.CompressionType

private[kafka] final case class TopicConfiguration(
    compressionType: CompressionType
  )

private[kafka] object TopicConfiguration:

  val config: ConfigValue[Effect, TopicConfiguration] =
    default(CompressionType.LZ4).as[CompressionType].map(TopicConfiguration.apply)
