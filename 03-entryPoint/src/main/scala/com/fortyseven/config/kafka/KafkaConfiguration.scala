package com.fortyseven.config.kafka

import cats.syntax.all.*
import ciris.*
import ciris.refined.*
import com.fortyseven.config.kafka.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto.*
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.types.numeric.{PosInt, PosLong}
import eu.timepit.refined.types.string.NonEmptyString
import org.apache.kafka.common.record.CompressionType

/** Full list of configurations https://kafka.apache.org/documentation/
  * @param brokerConfiguration
  *   Contains the configuration for the Broker
  * @param topicConfiguration
  *   Contains the configuration for the Topic
  * @param producerConfiguration
  *   Contains the configuration for the Producer
  * @param consumerConfiguration
  *   Contains the configuration for the Consumer
  */
final case class KafkaConfiguration(
    brokerConfiguration: BrokerConfiguration,
    topicConfiguration: TopicConfiguration,
    producerConfiguration: ProducerConfiguration,
    consumerConfiguration: ConsumerConfiguration
  )

object KafkaConfiguration:

  val config: ConfigValue[Effect, KafkaConfiguration] =
    (
      BrokerConfiguration.config,
      TopicConfiguration.config,
      ProducerConfiguration.config,
      ConsumerConfiguration.config
    ).parMapN(KafkaConfiguration.apply)
