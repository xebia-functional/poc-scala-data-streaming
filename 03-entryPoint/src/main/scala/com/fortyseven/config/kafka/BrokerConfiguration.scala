package com.fortyseven.config.kafka

import cats.syntax.all.*
import ciris.*
import ciris.refined.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto.*
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.types.string.NonEmptyString

private[kafka] final case class BrokerConfiguration(
    brokerId: Int Refined Greater[-2],
    logDirs: NonEmptyString,
    zookeeperConnect: String
  )

private[kafka] object BrokerConfiguration:

  val config: ConfigValue[Effect, BrokerConfiguration] =
    (
      default(-1).as[Int Refined Greater[-2]],
      default("/tmp/kafka-logs").as[NonEmptyString],
      default("null").as[String]
    ).parMapN(BrokerConfiguration.apply)
