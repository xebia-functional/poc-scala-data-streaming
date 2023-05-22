package com.fortyseven.typesafeconfiguration

import com.fortyseven.coreheaders.ConfigHeader

import scala.concurrent.duration.FiniteDuration
import com.fortyseven.typesafeconfiguration.KafkaConfig.*
import com.typesafe.config.{Config, ConfigFactory}
import cats.effect.kernel.Async


final class KafkaConsumerConfiguration[F[_]: Async] extends ConfigHeader[F, KafkaConfiguration]:

  private val kafkaConfiguration: Config = ConfigFactory.load("kafka.conf").getConfig("KafkaConfiguration")

  private object BrokerConfigurationLoader:
    private val brokerConf: Config = kafkaConfiguration.getConfig("BrokerConfiguration")
    private val brokerAddress: Either[Throwable, NonEmptyString] = NonEmptyString.from(brokerConf.getString("brokerAddress"))
    val load: Either[Throwable, BrokerConfiguration] = for ba <- brokerAddress yield  BrokerConfiguration(ba)

  private object ConsumerConfigurationLoader:
    private val consumerConf = kafkaConfiguration.getConfig("ConsumerConfiguration")
    private val topicName: Either[Throwable, NonEmptyString] = NonEmptyString.from(consumerConf.getString("topicName"))
    private val autoOffsetReset: Either[Throwable, NonEmptyString] = NonEmptyString.from(consumerConf.getString("autoOffsetReset"))
    private val groupId: Either[Throwable, NonEmptyString] = NonEmptyString.from(consumerConf.getString("groupId"))
    private val maxConcurrent: Either[Throwable, PositiveInt] = PositiveInt.from(consumerConf.getInt("maxConcurrent"))
    val load: Either[Throwable, ConsumerConfiguration] =
      for
        tn <- topicName
        aor <- autoOffsetReset
        gi <- groupId
        mc <- maxConcurrent
      yield ConsumerConfiguration(tn, aor, gi, mc)

  private object ProducerConfigurationLoader:
    private val producerConf: Config = kafkaConfiguration.getConfig("ProducerConfiguration")
    private val topicName: Either[Throwable, NonEmptyString] = NonEmptyString.from(producerConf.getString("valueSerializerClass"))
    private val valueSerializerClass: Either[Throwable, NonEmptyString] = NonEmptyString.from(producerConf.getString("topicName"))
    private val maxConcurrent: Either[Throwable, PositiveInt] = PositiveInt.from(producerConf.getInt("maxConcurrent"))
    private val compressionType: Either[Throwable, NonEmptyString] = NonEmptyString.from(producerConf.getString("compressionType"))
    private val commitBatchWithinSize: Either[Throwable, PositiveInt] = PositiveInt.from(producerConf.getInt("commitBatchWithinSize"))
    private val commitBatchWithinTime: Either[Throwable, PositiveInt] = PositiveInt.from(producerConf.getInt("commitBatchWithinTime"))
    val load: Either[Throwable, ProducerConfiguration] =
      for
        tn <- topicName
        vsc <- valueSerializerClass
        mx <- maxConcurrent
        ct <- compressionType
        cbws <- commitBatchWithinSize
        cbwt <- commitBatchWithinTime
      yield ProducerConfiguration(tn, vsc, mx, ct, cbws, FiniteDuration.apply(cbwt.asInt, "seconds"))

  override def load: F[KafkaConfiguration] =
    val eitherLoad: Either[Throwable, KafkaConfiguration] =
      for
        bc <- BrokerConfigurationLoader.load
        cc <- ConsumerConfigurationLoader.load
        pc <- ProducerConfigurationLoader.load
      yield KafkaConfiguration(bc, cc, pc)

    eitherLoad match
      case  Right(value) => Async.apply.pure(value)
      case Left(throwable) => Async.apply.raiseError(throwable)

