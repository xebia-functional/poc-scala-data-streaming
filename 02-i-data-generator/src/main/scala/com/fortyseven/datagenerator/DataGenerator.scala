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

import org.apache.kafka.clients.producer.ProducerConfig

import cats.effect.kernel.Async
import cats.implicits.*
import com.fortyseven.core.codecs.iot.IotCodecs.given
import com.fortyseven.coreheaders.config.DataGeneratorConfig
import com.fortyseven.coreheaders.model.iot.model.{GPSPosition, PneumaticPressure}
import com.fortyseven.coreheaders.{ConfigHeader, DataGeneratorHeader}
import fs2.kafka.*

final class DataGenerator[F[_]: Async] extends DataGeneratorHeader[F]:

  override def generate(conf: ConfigHeader[F, DataGeneratorConfig]): F[Unit] =
    for
      dgc <- conf.load
      _   <- runWithConfiguration(dgc)
    yield ()

  private def runWithConfiguration(dgc: DataGeneratorConfig): F[Unit] =

    import VulcanSerdes.*

    val generators = new ModelGenerators[F]

    val producerConfig = dgc.kafkaConf.producer.getOrElse(
      throw new RuntimeException("No producer config available")
    )

    val producerSettings = ProducerSettings[F, String, Array[Byte]]
      .withBootstrapServers(dgc.kafkaConf.broker.brokerAddress)
      .withProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, producerConfig.compressionType)
      .withProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerConfig.valueSerializerClass)

    val pneumaticPressureSerializer = avroSerializer[PneumaticPressure](
      Config(dgc.schemaRegistryConf.schemaRegistryUrl),
      includeKey = false
    )

    val gpsPositionSerializer = avroSerializer[GPSPosition](
      Config(dgc.schemaRegistryConf.schemaRegistryUrl),
      includeKey = false
    )

    KafkaProducer
      .stream(producerSettings)
      .flatMap { producer =>
        (generators.generateGPSPosition.mapRecords(producerConfig.topicName)(using gpsPositionSerializer) ++
          generators.generatePneumaticPressure.mapRecords(producerConfig.topicName)(using pneumaticPressureSerializer))
          .evalMap(producer.produce)
          .groupWithin(
            producerConfig.commitBatchWithinSize,
            producerConfig.commitBatchWithinTime
          )
          .evalMap(_.sequence)
      }
      .compile
      .drain

  extension [T](inputStream: fs2.Stream[F, T])

    private def mapRecords(
        sourceTopic: String
      )(using serializer: KafkaSerializer[T]): fs2.Stream[F, ProducerRecords[String, Array[Byte]]] =
      inputStream.map { committable =>
        val key   = committable.getClass.getSimpleName
        val value = serializer.serialize(sourceTopic, committable)
        ProducerRecords.one(ProducerRecord(sourceTopic, key, value))
      }
