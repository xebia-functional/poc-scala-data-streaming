package com.fortyseven.datagenerator

import com.fortyseven.coreheaders.DataGenerator
import com.fortyseven.coreheaders.model.app.model.*
import com.fortyseven.coreheaders.model.iot.model.*

object DataGenerator extends DataGenerator:
  override def generateBatteryCharge: BateryCharge = ???

  override def generateBatteryHealth: BatteryHealth = ???

  override def generateBreaksHealth: BreaksHealth = ???

  override def generateBreaksUsage: BreaksUsage = ???

  override def generateGPSPosition: GPSPosition = ???

  override def generatePneumaticPressure: PneumaticPressure = ???

  override def generateWheelRotation: WheelRotation = ???

  override def generateCurrentSpeed: CurrentSpeed = ???

  override def generateTotalDistanceByTrip: TotalDistanceByTrip = ???

  override def generateTotalDistancePerUser: TotalDistanceByUser = ???

  override def generateTotalRange: TotalRange = ???