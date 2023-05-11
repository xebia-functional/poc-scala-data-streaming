package com.fortyseven.configuration

import scala.concurrent.duration.*

import org.apache.kafka.common.record.CompressionType

import cats.effect.Async
import cats.implicits.*
import ciris.refined.*
import ciris.{ConfigValue, Effect, default}
import com.fortyseven.coreheaders.ConfigHeader
import com.fortyseven.coreheaders.config.KafkaConsumerConfig
import com.fortyseven.coreheaders.config.internal.KafkaConfig.*
import com.fortyseven.coreheaders.config.internal.SchemaRegistryConfig.*
import eu.timepit.refined.types.all.*
import eu.timepit.refined.types.string.NonEmptyString
import fs2.kafka.AutoOffsetReset

final class KafkaConsumerConfiguration[F[_]: Async] extends ConfigHeader[F, KafkaConsumerConfig]:

  lazy val config: ConfigValue[Effect, KafkaConsumerConfig] =
    for
      brokerAddress         <- default("localhost:9092").as[NonEmptyString]
      sourceTopicName         <- default("data-generator").as[NonEmptyString]
      sinkTopicName         <- default("input-topic").as[NonEmptyString]
      autoOffsetReset       <- default(AutoOffsetReset.Earliest).as[AutoOffsetReset]
      groupId               <- default("groupId").as[NonEmptyString]
      valueSerializerClass  <- default("io.confluent.kafka.serializers.KafkaAvroSerializer").as[NonEmptyString]
      consumerMaxConcurrent         <- default(25).as[PosInt]
      producerMaxConcurrent         <- default(Int.MaxValue).as[PosInt]
      compressionType       <- default(CompressionType.LZ4).as[CompressionType]
      commitBatchWithinSize <- default(500).as[PosInt]
      commitBatchWithinTime <- default(15.seconds).as[FiniteDuration]
    yield KafkaConsumerConfig(
      KafkaConf(
        broker = BrokerConf(brokerAddress.toString),
        consumer = ConsumerConf(
          topicName = sourceTopicName.toString,
          autoOffsetReset = autoOffsetReset.toString,
          groupId = groupId.toString,
          maxConcurrent = consumerMaxConcurrent.toString.toInt
        ).some,
        producer = ProducerConf(
          topicName = sinkTopicName.toString,
          valueSerializerClass = valueSerializerClass.toString,
          maxConcurrent = producerMaxConcurrent.toString.toInt,
          compressionType = compressionType.toString,
          commitBatchWithinSize = commitBatchWithinSize.toString.toInt,
          commitBatchWithinTime = commitBatchWithinTime
        ).some
      )
    )

  override def load: F[KafkaConsumerConfig] = config.load[F]
