package com.fortyseven.typesafeconfiguration

import scala.concurrent.duration.FiniteDuration
import com.fortyseven.typesafeconfiguration.KafkaConfig.*
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try


class KafkaConsumerConfiguration:

  private def readConfig(path: String): Config = ConfigFactory.load(path)

  private def kafkaConf(path: String = "kafkaConsumer.conf"): Config = readConfig(path).getConfig("KafkaConf")

  private def brokerConf:Config = kafkaConf().getConfig("BrokerConf")
  private def consumerConf: Config = kafkaConf().getConfig("ConsumerConf")
  private def producerConf: Config = kafkaConf().getConfig("ProducerConf")

  private def consumerConfOP: Option[Config]  = Try(kafkaConf().getConfig("ConsumerConf")).toOption
  private def producerConfOP: Option[Config]  = Try(kafkaConf().getConfig("ProducerConf")).toOption

  def getBrokerConfig: Either[Throwable, BrokerConf] =
    for nesBrokerAddress <- NonEmptyString.from(brokerConf.getString("brokerAddress"))
        .orElse(Right(NonEmptyString.apply("nes")))
    yield BrokerConf(brokerAddress = nesBrokerAddress)

  def getConsumerConfig: Either[Throwable, ConsumerConf] =
    for {
      nesConsumerTopicName <- NonEmptyString.from(consumerConf.getString("topicName"))
        .orElse(Right(NonEmptyString.apply("nes")))
      nesAutoOffsetReset <- NonEmptyString.from(consumerConf.getString("autoOffsetReset"))
          .orElse(Right(NonEmptyString.apply("nes")))
      nesGroupId <- NonEmptyString.from(consumerConf.getString("groupId"))
        .orElse(Right(NonEmptyString.apply("nes")))
        posIntConsumerMaxConcurrent <- PositiveInt.from(producerConf.getInt("maxConcurrent"))
          .orElse(Right(PositiveInt.apply(1)))
    } yield ConsumerConf(
      topicName = nesConsumerTopicName,
      autoOffsetReset = nesAutoOffsetReset,
      groupId = nesGroupId,
      maxConcurrent = posIntConsumerMaxConcurrent
    )

  def getProducerConfig: Either[Throwable, ProducerConf] =
    for {
      nesProducerTopicName <- NonEmptyString.from(producerConf.getString("topicName"))
        .orElse(Right(NonEmptyString.apply("nes")))
      nesValueSerializerClass <- NonEmptyString.from(producerConf.getString("valueSerializerClass"))
        .orElse(Right(NonEmptyString.apply("nes")))
      posIntProducerMaxConcurrent <- PositiveInt.from(producerConf.getInt("maxConcurrent"))
        .orElse(Right(PositiveInt.apply(1)))
      nesCompressionType <- NonEmptyString.from(producerConf.getString("compressionType"))
        .orElse(Right(NonEmptyString.apply("nes")))
      posIntCommitBatchWithinSize <- PositiveInt.from(producerConf.getInt("commitBatchWithinSize"))
        .orElse(Right(PositiveInt.apply(1)))
    } yield ProducerConf(
      topicName = nesProducerTopicName,
      valueSerializerClass = nesValueSerializerClass,
      maxConcurrent = posIntProducerMaxConcurrent,
      compressionType = nesCompressionType,
      commitBatchWithinSize = posIntCommitBatchWithinSize,
      commitBatchWithinTime = FiniteDuration.apply(producerConf.getLong("commitBatchWithinTime"), "second")
    )


  def getConfig: Either[Throwable, KafkaConf] =
    for
      brokerConfig <- getBrokerConfig
      consumerConfig <- getConsumerConfig
      producerConfig <- getProducerConfig
    yield KafkaConf(brokerConfig, Try(consumerConfig).toOption, Try(producerConfig).toOption)


