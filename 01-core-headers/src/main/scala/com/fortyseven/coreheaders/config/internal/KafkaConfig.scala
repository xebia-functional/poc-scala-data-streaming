package com.fortyseven.coreheaders.config.internal

import scala.concurrent.duration.FiniteDuration

import org.apache.kafka.common.config.ConfigDef.NonEmptyString

object KafkaConfig:

  final case class KafkaConf(broker: BrokerConf, consumer: Option[ConsumerConf], producer: Option[ProducerConf])

  final case class BrokerConf(brokerAddress: String)

  final case class ConsumerConf(topicName: String, autoOffsetReset: String, groupId: String, maxConcurrent: Int)

  final case class ProducerConf(
      topicName: String,
      valueSerializerClass: String,
      maxConcurrent: Int,
      compressionType: String,
      commitBatchWithinSize: Int,
      commitBatchWithinTime: FiniteDuration
    )
