package com.fortyseven.datagenerator

import cats.effect.IO
import com.fortyseven.coreheaders.{DataGenerator as DataGeneratorCore}
import com.fortyseven.coreheaders.model.app.model.*
import com.fortyseven.coreheaders.model.iot.model.*

class DataGenerator[F[_]] extends DataGeneratorCore[F]:
  override def generateBatteryCharge: F[BateryCharge] = ???

  override def generateBatteryHealth: F[BatteryHealth] = ???

  override def generateBreaksHealth: F[BreaksHealth] = ???

  override def generateBreaksUsage: F[BreaksUsage] = ???

  override def generateGPSPosition: F[GPSPosition] = ???

  override def generatePneumaticPressure: F[PneumaticPressure] = ???

  override def generateWheelRotation: F[WheelRotation] = ???

  override def generateCurrentSpeed: F[CurrentSpeed] = ???

  override def generateTotalDistanceByTrip: F[TotalDistanceByTrip] = ???

  override def generateTotalDistancePerUser: F[TotalDistanceByUser] = ???

  override def generateTotalRange: F[TotalRange] = ???