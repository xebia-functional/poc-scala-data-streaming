package com.fortyseven.typesafeconfiguration

import scala.compiletime.requireConst
import scala.concurrent.duration.FiniteDuration
import scala.sys.error

object KafkaConfig:

  opaque type NonEmptyString = String
  object NonEmptyString:
    def from(s: String): Either[Throwable, NonEmptyString] =
      if s.trim.isEmpty then Left(new IllegalStateException(s"The provided string $s is empty."))
      else Right(s)
    inline def apply(s: String): NonEmptyString =
      requireConst(s)
      inline if s == "" then error("Empty String is not allowed here!") else s
    extension(nes: NonEmptyString)
      def asString: String = nes

  opaque type PositiveInt = Int
  object PositiveInt:
    def from(i: Int): Either[Throwable, PositiveInt] =
      if i < 0 then Left(new IllegalStateException(s"The provided int $i is negative."))
      else Right(i)
    inline def apply(i: Int): PositiveInt =
      requireConst(i)
      inline if i >= 0 then error("Int must be positive!") else i
    extension(posInt: PositiveInt)
      def asInt: Int = posInt
      

  
  
  final case class KafkaConf(
    broker: BrokerConf,
    consumer: Option[ConsumerConf],
    producer: Option[ProducerConf]
  )

  final case class BrokerConf(
    brokerAddress: NonEmptyString
  )

  final case class ConsumerConf(
    topicName: NonEmptyString,
    autoOffsetReset: NonEmptyString,
    groupId: NonEmptyString,
    maxConcurrent: PositiveInt
  )

  final case class ProducerConf(
    topicName: NonEmptyString,
    valueSerializerClass: NonEmptyString,
    maxConcurrent: PositiveInt,
    compressionType: NonEmptyString,
    commitBatchWithinSize: PositiveInt,
    commitBatchWithinTime: FiniteDuration
  )
