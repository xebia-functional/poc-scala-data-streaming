/*
 * Copyright 2023 Xebia Functional
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortyseven.core.codecs.iot

import com.fortyseven.core.TestUtils.codeAndDecode
import com.fortyseven.coreheaders.model.iot.errors.OutOfBoundsError
import munit.ScalaCheckSuite
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Prop.forAll
import com.fortyseven.core.codecs.iot.IotErrorCodecs.given

import scala.reflect.{classTag, ClassTag}

class IotErrorCodecsTest extends ScalaCheckSuite:

  given Arbitrary[OutOfBoundsError] = Arbitrary(Gen.alphaStr.map(OutOfBoundsError.apply))

  private def propCodec[A: Arbitrary: vulcan.Codec: ClassTag](): Unit =
    property(s"Encoding and decoding for ${classTag[A].runtimeClass.getSimpleName} should work"):
      forAll: (a: A) =>
        assertEquals(codeAndDecode(a), Right(a))

  propCodec[OutOfBoundsError]()
