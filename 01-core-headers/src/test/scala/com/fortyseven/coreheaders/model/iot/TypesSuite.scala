package com.fortyseven.coreheaders.model.iot

import com.fortyseven.coreheaders.model.iot.errors.OutOfBoundsError
import com.fortyseven.coreheaders.model.iot.types.*
import munit.{FunSuite, ScalaCheckSuite}
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalatest.matchers.must.Matchers

class TypesSuite extends ScalaCheckSuite:

  property("Latitude") {
    forAll(Gen.choose(-360.0, 360.0)) { (coordinate: Double) =>
      if coordinate > 90.0 || coordinate < -90.0 then assert(Latitude(coordinate).isLeft)
      else assert(Latitude(coordinate).isRight)
    }
  }

  property("Longitude") {
    forAll(Gen.choose(-360.0, 360.0)) { (coordinate: Double) =>
      if coordinate > 180.0 || coordinate < -180.0 then assert(Longitude(coordinate).isLeft)
      else assert(Longitude(coordinate).isRight)
    }
  }

  property("Bar") {
    forAll(Gen.choose(-360.0, 360.0)) { (pressure: Double) =>
      if pressure < 0.0 then assert(Bar(pressure).isLeft)
      else assert(Bar(pressure).isRight)
    }
  }
