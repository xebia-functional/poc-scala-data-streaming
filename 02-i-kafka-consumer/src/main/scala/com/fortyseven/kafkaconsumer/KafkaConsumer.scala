package com.fortyseven.kafkaconsumer

import cats.effect.{IO, IOApp}
import fs2.kafka.*

import com.fortyseven.coreheaders.KafkaConsumer

import scala.concurrent.duration.*

object KafkaConsumer extends KafkaConsumer with IOApp.Simple:
  override def consume(): IO[Unit] = run

  val run: IO[Unit] =
    def processRecord(record: ConsumerRecord[String, String]): IO[(String, String)] =
      IO.pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[IO, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers("localhost:9092")
        .withGroupId("group")

    val producerSettings =
      ProducerSettings[IO, String, String].withBootstrapServers("localhost:9092")

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