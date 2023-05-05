package com.fortyseven.datagenerator

import cats.effect.kernel.Async
import cats.effect.{IO, IOApp}
import cats.implicits.*
import com.fortyseven.coreheaders.DataGeneratorHeader
import com.fortyseven.coreheaders.model.app.model.*
import com.fortyseven.coreheaders.model.iot.model.*
import com.fortyseven.coreheaders.model.iot.types.*
import fs2.kafka.*

import scala.concurrent.duration.*

object DataGenerator extends IOApp.Simple:

  val run: IO[Unit] = new DataGenerator[IO].run

protected class DataGenerator[F[_]: Async] extends DataGeneratorHeader[F]:

  val run: F[Unit] =
    val producerSettings = ProducerSettings[F, String, Double].withBootstrapServers("localhost:9092")

    KafkaProducer
      .stream(producerSettings)
      .flatMap { producer =>
        generatePneumaticPressure
          .map { committable =>
            val key    = committable.getClass.getSimpleName.toString // generatePneumaticPressure
            val value  = committable.pressure
            val record = ProducerRecord("data-generator", key, value)
            ProducerRecords.one(record)
          }
          .evalMap(producer.produce)
          .groupWithin(1, 15.seconds)
          .evalMap(_.sequence)
      }
      .compile
      .drain

  override def generateBatteryCharge: F[BateryCharge] = ???

  override def generateBatteryHealth: F[BatteryHealth] = ???

  override def generateBreaksHealth: F[BreaksHealth] = ???

  override def generateBreaksUsage: F[BreaksUsage] = ???

  override def generateGPSPosition: fs2.Stream[F, GPSPosition] =
    def emitLoop(latValue: Double, lonValue: Double): fs2.Stream[F, GPSPosition] =
      def getValue(value: Double) = value - math.random() * 1e-3
      (Latitude(getValue(latValue)), Longitude(getValue(lonValue))) match
        case (Right(lat), Right(lon)) => fs2.Stream.emit(GPSPosition(lat, lon)) ++ emitLoop(lat, lon)
        case _                        => emitLoop(latValue, lonValue)
    emitLoop(latValue = 2.0, lonValue = 2.0) // ToDo: Soft-code initial coordinate values

  override def generatePneumaticPressure: fs2.Stream[F, PneumaticPressure] =
    def emitLoop(pValue: Double): fs2.Stream[F, PneumaticPressure] =
      Bar(pValue - math.random() * 1e-3) match
        case Right(p) => fs2.Stream.emit(PneumaticPressure(p)) ++ emitLoop(p)
        case _        => emitLoop(pValue)

    emitLoop(pValue = 2.0) // ToDo: Soft-code initial value

  override def generateWheelRotation: F[WheelRotation] = ???

  override def generateCurrentSpeed: F[CurrentSpeed] = ???

  override def generateTotalDistanceByTrip: F[TotalDistanceByTrip] = ???

  override def generateTotalDistancePerUser: F[TotalDistanceByUser] = ???

  override def generateTotalRange: F[TotalRange] = ???
