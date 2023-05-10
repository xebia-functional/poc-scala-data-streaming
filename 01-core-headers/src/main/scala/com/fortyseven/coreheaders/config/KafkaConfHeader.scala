package com.fortyseven.coreheaders.config

import scala.concurrent.duration.FiniteDuration

import org.apache.kafka.common.record.CompressionType

import fs2.kafka.AutoOffsetReset

trait KafkaConfHeader:

  val brokerConfiguration: BrokerHeader

  val consumerConfiguration: ConsumerHeader

  val producerConfiguration: ProducerHeader

  val streamConfiguration: StreamHeader

trait BrokerHeader:
  val brokerAddress: String
end BrokerHeader

  
trait ConsumerHeader:
  val autoOffsetReset: AutoOffsetReset
  val groupId: String
end ConsumerHeader

trait ProducerHeader:
  val maxConcurrent: Int
  val compressionType: CompressionType
  
end ProducerHeader



trait StreamHeader:
  val inputTopic: String
  val outputTopic: String
  val maxConcurrent: Int
  val commitBatchWithinSize: Int
  val commitBatchWithinTime: FiniteDuration
end StreamHeader
