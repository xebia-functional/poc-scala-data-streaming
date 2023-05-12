package com.fortyseven.core

import munit.ScalaCheckSuite
import org.scalacheck.Prop.forAll
import vulcan.{AvroError, Codec}
import com.fortyseven.core.codecs.iot.IotCodecs.given
import com.fortyseven.coreheaders.model.iot.model.{GPSPosition, PneumaticPressure}
import com.fortyseven.coreheaders.model.iot.types.{Bar, Latitude, Longitude}
import org.scalacheck.{Arbitrary, Gen}

class CodecSpec extends ScalaCheckSuite:

  private def getOutput[C](instance: C)(using Codec[C]): Either[AvroError, C] =
    for
      encoded <- vulcan.Codec.encode[C](instance)
      result  <- vulcan.Codec.decode[C](encoded)
    yield result

  property("GPSPosition") {
    forAll(Gen.choose(-90, 90), Gen.choose(-180, 180)) { (latitude: Int, longitude: Int) =>
      (Latitude(latitude), Longitude(longitude)) match
        case (Right(lat), Right(lon)) =>
          val gpsPosition = GPSPosition(lat, lon)
          getOutput(gpsPosition) == Right(gpsPosition)
        case _                        => assert(false)
      ()
    }
  }

  property("PneumaticPressure") {
    forAll(Gen.choose(1, 100)) { (pressure: Int) =>
      Bar(pressure) match
        case Right(p) =>
          val pneumaticPressure = PneumaticPressure(p)
          getOutput(pneumaticPressure) == Right(pneumaticPressure)
        case _        => assert(false)
      ()
    }
  }
