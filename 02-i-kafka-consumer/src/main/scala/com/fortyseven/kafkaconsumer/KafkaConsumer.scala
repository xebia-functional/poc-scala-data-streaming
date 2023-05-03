package com.fortyseven.kafkaconsumer

import cats.*
import cats.effect.kernel.{Async, Sync}
import cats.effect.{IO, IOApp}
import cats.implicits.*
import com.fortyseven.coreheaders.KafkaConsumerHeader
import fs2.kafka.*

import scala.concurrent.duration.*

object KafkaConsumer extends IOApp.Simple:

  val run: IO[Unit] = new KafkaConsumer[IO].run

private class KafkaConsumer[F[_]: Async] extends KafkaConsumerHeader[F]:

  override def consume(): F[Unit] = run

  val run: F[Unit] =
    def processRecord(record: ConsumerRecord[String, String]): F[(String, String)] =
      Applicative[F].pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[F, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers("localhost:9092")
        .withGroupId("group")

    val producerSettings =
      ProducerSettings[F, String, String].withBootstrapServers("localhost:9092")

    val stream =
      fs2.kafka.KafkaConsumer
        .stream(consumerSettings)
        .subscribeTo("input-topic")
        .records
        .mapAsync(25) { committable =>
          processRecord(committable.record).map { case (key, value) =>
            val record = ProducerRecord("output-topic", key, value)
            committable.offset -> ProducerRecords.one(record)
          }
        }.through { offsetsAndProducerRecords =>
          KafkaProducer.stream(producerSettings).flatMap { producer =>
            offsetsAndProducerRecords
              .evalMap { case (offset, producerRecord) =>
                producer.produce(producerRecord).map(_.as(offset))
              }.parEvalMap(Int.MaxValue)(identity)
          }
        }.through(commitBatchWithin(500, 15.seconds))

    stream.compile.drain
