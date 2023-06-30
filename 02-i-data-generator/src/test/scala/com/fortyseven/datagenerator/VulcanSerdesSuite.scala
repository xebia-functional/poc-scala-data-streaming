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

package com.fortyseven.datagenerator

import com.fortyseven.core.codecs.iot.IotCodecs.pneumaticPressureCodec
import com.fortyseven.coreheaders.model.iot.errors
import com.fortyseven.coreheaders.model.iot.model.PneumaticPressure
import com.fortyseven.coreheaders.model.types.types.Bar
import io.confluent.kafka.schemaregistry.avro.AvroSchema
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient
import munit.CatsEffectSuite
import org.apache.kafka.common.serialization.Serde

class VulcanSerdesSuite extends CatsEffectSuite:

  import VulcanSerdes.*

  private def serialize[A](topic: String, data: A)(using serde: Serde[A]): Array[Byte] =
    serde.serializer.serialize(topic, data)

  private def deserialize[A](topic: String, bytes: Array[Byte])(using serde: Serde[A]): A =
    serde.deserializer.deserialize(topic, bytes)

  test("Serialize a case class as a record"):
    val mockedClient = new MockSchemaRegistryClient()
    val topic        = "test-topic"

    pneumaticPressureCodec.schema match
      case Left(_)       => ()
      case Right(schema) => mockedClient.register(s"$topic-value", AvroSchema(schema.toString))
    val config                     = Config("useMockedClient", useMockedClient = Some(mockedClient))
    given Serde[PneumaticPressure] = avroSerde[PneumaticPressure](config, includeKey = false)

    val data: Either[errors.OutOfBoundsError, PneumaticPressure] =
      for bar <- Bar(2.0)
      yield PneumaticPressure(bar)

    val result: Either[errors.OutOfBoundsError, PneumaticPressure] =
      for
        d <- data
        serialized   = serialize(topic, d)
        deserialized = deserialize(topic, serialized)
      yield deserialized

    assertEquals(data, result)
