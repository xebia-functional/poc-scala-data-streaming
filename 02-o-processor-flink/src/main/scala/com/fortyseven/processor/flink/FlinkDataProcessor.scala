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

package com.fortyseven.processor.flink

import java.nio.charset.StandardCharsets

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.{DeserializationSchema, SimpleStringSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.connector.kafka.sink.{KafkaRecordSerializationSchema, KafkaSink}
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema
import org.apache.flink.core.execution.JobClient
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema
import org.apache.flink.util.Collector

import org.apache.kafka.clients.consumer.ConsumerRecord

import cats.Applicative
import cats.effect.*
import cats.implicits.*
import com.fortyseven.core.codecs.iot.IotCodecs.given
import com.fortyseven.coreheaders.config.JobProcessorConfig
import com.fortyseven.coreheaders.model.iot.model.{GPSPosition, PneumaticPressure}
import com.fortyseven.coreheaders.model.iot.types.Bar
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.avro.specific.SpecificRecord

final class FlinkDataProcessor[F[_]: Applicative](env: StreamExecutionEnvironment):

  def run(jpc: JobProcessorConfig): F[JobClient] =

    val consumerConfig = jpc.kafkaConf.consumer.getOrElse(
      throw new RuntimeException("No consumer config available")
    )

    val producerConfig = jpc.kafkaConf.producer.getOrElse(
      throw new RuntimeException("No producer config available")
    )

    val deserializationSchema = pneumaticPressureCodec.schema match
      case Right(s) =>
        ConfluentRegistryAvroDeserializationSchema.forGeneric(s, jpc.schemaRegistryConf.schemaRegistryUrl)
      case Left(e)  => throw new RuntimeException("No pneumaticPressureCodec schema available")

    val kafkaSource = KafkaSource
      .builder()
      .setBootstrapServers(jpc.kafkaConf.broker.brokerAddress)
      .setTopics(consumerConfig.topicName)
      .setGroupId(consumerConfig.groupId)
      .setStartingOffsets(consumerConfig.autoOffsetReset.toLowerCase match
        case "earliest" => OffsetsInitializer.earliest()
        case _          => OffsetsInitializer.latest()
      )
      .setValueOnlyDeserializer(deserializationSchema)
      .build()

    val kafkaSink = KafkaSink
      .builder()
      .setBootstrapServers(jpc.kafkaConf.broker.brokerAddress)
      .setRecordSerializer(
        KafkaRecordSerializationSchema
          .builder()
          .setTopic(producerConfig.topicName)
          .setKeySerializationSchema(new SimpleStringSchema())
          .setValueSerializationSchema(new SimpleStringSchema())
          .build()
      )
      .build()

    val stream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka-source")
    // .sinkTo(kafkaSink) // ToDo: transform and sink result

    stream.print

    env.executeAsync("Flink Streaming").pure
