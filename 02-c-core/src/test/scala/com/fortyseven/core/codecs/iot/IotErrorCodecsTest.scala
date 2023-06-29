package com.fortyseven.core.codecs.iot

import com.fortyseven.core.TestUtils.codeAndDecode
import com.fortyseven.coreheaders.model.iot.errors.OutOfBoundsError
import munit.ScalaCheckSuite
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Prop.forAll
import com.fortyseven.core.codecs.iot.IotErrorCodecs.given

import scala.reflect.{ClassTag, classTag}

class IotErrorCodecsTest extends ScalaCheckSuite:

  given Arbitrary[OutOfBoundsError] = Arbitrary(Gen.alphaStr.map(OutOfBoundsError.apply))

  private def propCodec[A: Arbitrary : vulcan.Codec : ClassTag](): Unit =
    property(s"Encoding and decoding for ${classTag[A].runtimeClass.getSimpleName} should work"):
      forAll: (a: A) =>
        assertEquals(codeAndDecode(a), Right(a))

  propCodec[OutOfBoundsError]()

