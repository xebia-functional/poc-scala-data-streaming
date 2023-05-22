package com.fortyseven.typesafeconfiguration

import com.fortyseven.coreheaders.ConfigHeader

import scala.concurrent.duration.FiniteDuration
import com.fortyseven.typesafeconfiguration.KafkaConfig.*
import com.typesafe.config.{Config, ConfigFactory}
import cats.effect.kernel.Async


class KafkaConsumerConfigurationT[F[_]: Async] extends ConfigHeader[F, KafkaConfiguration]:

  val kafkaConf: Config = ConfigFactory.load("kafkaConsumer.conf").getConfig("KafkaConf")

  object BrokerConf:
    val brokerConf = kafkaConf.getConfig("BrokerConf")
    val brokerAddress: Either[Throwable, NonEmptyString] = NonEmptyString.from(brokerConf.getString("brokerAddress"))
    val load: Either[Throwable, BrokerConfiguration] = for ba <- brokerAddress yield  BrokerConfiguration(ba)

  object ConsumerConf:
    val consumerConf = kafkaConf.getConfig("ConsumerConf")
    val topicName: Either[Throwable, NonEmptyString] = NonEmptyString.from(consumerConf.getString("topicName"))
    val autoOffsetReset: Either[Throwable, NonEmptyString] = NonEmptyString.from(consumerConf.getString("autoOffsetReset"))
    val groupId: Either[Throwable, NonEmptyString] = NonEmptyString.from(consumerConf.getString("groupId"))
    val maxConcurrent: Either[Throwable, PositiveInt] = PositiveInt.from(consumerConf.getInt("maxConcurrent"))
    val load: Either[Throwable, ConsumerConfiguration] =
      for
        tn <- topicName
        aor <- autoOffsetReset
        gi <- groupId
        mc <- maxConcurrent
      yield ConsumerConfiguration(tn, aor, gi, mc)

  object ProducerConf:
    val producerConf = kafkaConf.getConfig("ProducerConf")
    val topicName: Either[Throwable, NonEmptyString] = NonEmptyString.from(producerConf.getString("valueSerializerClass"))
    val valueSerializerClass: Either[Throwable, NonEmptyString] = NonEmptyString.from(producerConf.getString("topicName"))
    val maxConcurrent: Either[Throwable, PositiveInt] = PositiveInt.from(producerConf.getInt("maxConcurrent"))
    val compressionType: Either[Throwable, NonEmptyString] = NonEmptyString.from(producerConf.getString("compressionType"))
    val commitBatchWithinSize: Either[Throwable, PositiveInt] = PositiveInt.from(producerConf.getInt("commitBatchWithinSize"))
    val commitBatchWithinTime: Either[Throwable, PositiveInt] = PositiveInt.from(producerConf.getInt("commitBatchWithinTime"))
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
        bc <- BrokerConf.load
        cc <- ConsumerConf.load
        pc <- ProducerConf.load
      yield KafkaConfiguration(bc, cc, pc)
    eitherLoad match
      case  Right(value) => Async.apply.pure(value)
      case Left(throwable) => Async.apply.raiseError(throwable)

