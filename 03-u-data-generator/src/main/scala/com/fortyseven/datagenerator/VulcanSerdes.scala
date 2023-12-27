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

import scala.jdk.CollectionConverters.*
import scala.util.Try
import scala.util.control.NoStackTrace

import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer

import io.confluent.kafka.schemaregistry.avro.AvroSchema
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import vulcan.AvroError
import vulcan.Codec

object VulcanSerdes:

  final case class Configuration(schemaRegistryUrl: String, useMockedClient: Option[SchemaRegistryClient] = None)

  private def avroSerdesConf(configuration: Configuration) =
    Map("schema.registry.url" -> configuration.schemaRegistryUrl)

  final private case class SerializationError(msg: String) extends RuntimeException(msg) with NoStackTrace

  def avroSerializer[T](configuration: Configuration, includeKey: Boolean)(using codec: Codec[T]): Serializer[T] =
    new Serializer[T]:

      private val serializer = (codec.schema, configuration.useMockedClient) match
        case (Left(_), _) => KafkaAvroSerializer()
        case (Right(schema), Some(mockClient)) =>
          val parsedSchema = new AvroSchema(schema.toString)
          new KafkaAvroSerializer(mockClient):
            override def serialize(topic: String, record: AnyRef): Array[Byte] =
              serializeImpl(getSubjectName(topic, includeKey, record, parsedSchema), record, parsedSchema)
        case (Right(schema), None) =>
          val parsedSchema = new AvroSchema(schema.toString)
          new KafkaAvroSerializer:
            this.configure(avroSerdesConf(configuration).asJava, includeKey)
            override def serialize(topic: String, record: AnyRef): Array[Byte] =
              serializeImpl(getSubjectName(topic, includeKey, record, parsedSchema), record, parsedSchema)

      override def serialize(topic: String, data: T): Array[Byte] = serializer.serialize(
        topic,
        codec.encode(data) match
          case Left(err) => SerializationError(err.message)
          case Right(record) => record
      )

  def avroDeserializer[T](configuration: Configuration, includeKey: Boolean)(using codec: Codec[T]): Deserializer[T] =
    new Deserializer[T]:

      private val deserializer = (codec.schema, configuration.useMockedClient) match
        case (Right(_), Some(mockClient)) => new KafkaAvroDeserializer(mockClient)
        case _ => new KafkaAvroDeserializer():

            this.configure(avroSerdesConf(configuration).asJava, includeKey)

      override def deserialize(topic: String, data: Array[Byte]): T = (for
        readerSchema <- codec.schema.left.map(_.throwable)
        avro <- Try(deserializer.deserialize(topic, data, readerSchema)).toEither
        decoded <- codec.decode(avro, readerSchema).left.map(_.throwable)
      yield decoded).fold(err => throw RuntimeException(err.getMessage), identity)

  def avroSerde[T](config: Configuration, includeKey: Boolean)(using Codec[T]): Serde[T] = Serdes
    .serdeFrom(avroSerializer(config, includeKey), avroDeserializer(config, includeKey))

end VulcanSerdes
