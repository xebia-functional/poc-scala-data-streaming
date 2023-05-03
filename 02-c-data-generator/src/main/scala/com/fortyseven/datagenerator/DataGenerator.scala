package com.fortyseven.datagenerator

import cats.effect.{IO, IOApp}
import cats.effect.Sync
import com.fortyseven.coreheaders.DataGeneratorHeader
import com.fortyseven.coreheaders.model.app.model.*
import com.fortyseven.coreheaders.model.iot.model.*
import com.fortyseven.coreheaders.model.iot.types.*

object DataGenerator extends IOApp.Simple:

  val run: IO[Unit] = new DataGenerator[IO].run

protected class DataGenerator[F[_]: Sync] extends DataGeneratorHeader[F]:

  val run: F[Unit] = generatePneumaticPressure.compile.drain

  override def generateBatteryCharge: F[BateryCharge] = ???

  override def generateBatteryHealth: F[BatteryHealth] = ???

  override def generateBreaksHealth: F[BreaksHealth] = ???

  override def generateBreaksUsage: F[BreaksUsage] = ???

  override def generateGPSPosition: F[GPSPosition] = ???

  override def generatePneumaticPressure: fs2.Stream[F, PneumaticPressure] =
    val initValue = 2.0
    val pressureVariance: Double = (math.random() - 0.5) * 1e-1
    fs2.Stream.emit(PneumaticPressure(Bar(initValue + pressureVariance))).repeat

  override def generateWheelRotation: F[WheelRotation] = ???

  override def generateCurrentSpeed: F[CurrentSpeed] = ???

  override def generateTotalDistanceByTrip: F[TotalDistanceByTrip] = ???

  override def generateTotalDistancePerUser: F[TotalDistanceByUser] = ???

  override def generateTotalRange: F[TotalRange] = ???
