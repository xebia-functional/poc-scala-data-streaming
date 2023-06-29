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

import scala.concurrent.duration.*

import cats.Parallel
import cats.effect.kernel.Async
import cats.implicits.*

import com.fortyseven.core.codecs.iot.IotCodecs.given
import com.fortyseven.coreheaders.model.iot.model.{GPSPosition, PneumaticPressure}
import com.fortyseven.datagenerator.config.DataGeneratorConfiguration
import fs2.kafka.*
import org.apache.kafka.clients.producer.ProducerConfig

final class DataGenerator[F[_]: Async: Parallel]:

  def generate(dgc: DataGeneratorConfiguration): F[Unit] =

    import VulcanSerdes.*

    val generators = new ModelGenerators[F](100.milliseconds)

    val gpsPositionTopicName = s"${dgc.kafka.producer.topicName}-gps"
    val pneumaticPressureTopicName = s"${dgc.kafka.producer.topicName}-pp"

    val producerSettings = ProducerSettings[F, String, Array[Byte]]
      .withBootstrapServers(dgc.kafka.broker.bootstrapServers.asString)
      .withProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, dgc.kafka.producer.compressionType.toString)
      .withProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, dgc.kafka.producer.valueSerializerClass.asString)

    val pneumaticPressureSerializer = avroSerializer[PneumaticPressure](
      Config(dgc.schemaRegistry.schemaRegistryURL.asString),
      includeKey = false
    )

    val gpsPositionSerializer = avroSerializer[GPSPosition](
      Config(dgc.schemaRegistry.schemaRegistryURL.asString),
      includeKey = false
    )

    KafkaProducer
      .stream(producerSettings)
      .flatMap { producer =>
        val gpsPositionStream =
          generators.generateGPSPosition.mapRecords(gpsPositionTopicName)(using gpsPositionSerializer)
        val pneumaticPressureStream =
          generators.generatePneumaticPressure.mapRecords(pneumaticPressureTopicName)(using pneumaticPressureSerializer)
        gpsPositionStream
          .parZip(pneumaticPressureStream)
          .flatMap { case (s1, s2) => fs2.Stream.emit(s1) ++ fs2.Stream.emit(s2) }
          .evalMap(producer.produce)
          .groupWithin(
            dgc.kafka.producer.commitBatchWithinSize.asInt,
            dgc.kafka.producer.commitBatchWithinTime
          )
          .evalMap(_.sequence)
      }
      .compile
      .drain

  extension [T](inputStream: fs2.Stream[F, T])
    private def mapRecords(
        topic: String
    )(using serializer: KafkaSerializer[T]): fs2.Stream[F, ProducerRecords[String, Array[Byte]]] =
      inputStream.map { committable =>
        val key = committable.getClass.getSimpleName
        val value = serializer.serialize(topic, committable)
        ProducerRecords.one(ProducerRecord(topic, key, value))
      }
