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

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.core.execution.JobClient
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

import cats.Applicative
import cats.implicits.*
import com.fortyseven.core.codecs.iot.IotCodecs.given
import com.fortyseven.coreheaders.configuration.JobProcessorConfiguration
import org.apache.avro.Schema

final class FlinkDataProcessor[F[_]: Applicative](env: StreamExecutionEnvironment):

  def run(jpc: JobProcessorConfiguration): F[JobClient] =

    val consumerConfig = jpc.kafkaConfiguration.consumer.getOrElse(
      throw new RuntimeException("No consumer config available")
    )

    // val producerConfig = jpc.kafkaConfiguration.producer.getOrElse(
    //  throw new RuntimeException("No producer config available")
    // )

    val deserializationSchema = pneumaticPressureCodec.schema match
      case Right(s) =>
        ConfluentRegistryAvroDeserializationSchema.forGeneric(
          s,
          jpc.schemaRegistryConfiguration.schemaRegistryURL.asString
        )
      case Left(e)  => throw new RuntimeException("No pneumaticPressureCodec schema available")

    val kafkaSource = KafkaSource
      .builder()
      .setBootstrapServers(jpc.kafkaConfiguration.broker.brokerAddress.asString)
      .setTopics(consumerConfig.topicName.asString)
      .setGroupId(consumerConfig.groupId.asString)
      .setStartingOffsets(consumerConfig.autoOffsetReset.toString.toLowerCase match
        case "earliest" => OffsetsInitializer.earliest()
        case _          => OffsetsInitializer.latest()
      )
      .setValueOnlyDeserializer(deserializationSchema)
      .build()

    // val kafkaSink = KafkaSink
    //  .builder()
    //  .setBootstrapServers(jpc.kafkaConfiguration.broker.brokerAddress.asString)
    //  .setRecordSerializer(
    //    KafkaRecordSerializationSchema
    //      .builder()
    //      .setTopic(producerConfig.topicName.asString)
    //      .setKeySerializationSchema(new SimpleStringSchema())
    //      .setValueSerializationSchema(new SimpleStringSchema())
    //      .build()
    //  )
    //  .build()

    val stream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka-source")
    // .sinkTo(kafkaSink) // ToDo: transform and sink result

    stream.print

    env.executeAsync("Flink Streaming").pure
