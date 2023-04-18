package com.fortyseven.core.model.iot
import com.fortyseven.core.model.iot.types.{Latitude, Longitude, Percentage}

import scala.concurrent.duration.Duration

object model:
  case class GPSPosition(latitude: Latitude, longitude: Longitude)
  case class WheelRotation(s:Any)
  case class BateryCharge(percentage: Percentage)
  case class BatteryHealth(remaining: Percentage)
  case class PneumaticPressure()
  case class BreaksUsage(duration: Duration)
  case class BreaksHealth(remaining: Percentage)

