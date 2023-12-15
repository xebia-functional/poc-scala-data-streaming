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

import org.apache.kafka.common.serialization.Serde

import com.fortyseven.domain.codecs.iot.IotCodecs.pneumaticPressureCodec
import com.fortyseven.domain.model.iot.model.PneumaticPressure
import com.fortyseven.domain.model.types.refinedTypes.Bar

import io.confluent.kafka.schemaregistry.avro.AvroSchema
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient
import munit.CatsEffectSuite

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
    val config                     = Configuration("useMockedClient", useMockedClient = Some(mockedClient))
    given Serde[PneumaticPressure] = avroSerde[PneumaticPressure](config, includeKey = false)

    val data: PneumaticPressure = PneumaticPressure(Bar(2.0))

    val result: PneumaticPressure =
      val serialized   = serialize(topic, data)
      val deserialized = deserialize(topic, serialized)
      deserialized

    assertEquals(data, result)
