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

import org.apache.kafka.clients.producer.ProducerConfig

import cats.effect.kernel.Async
import cats.effect.{IO, IOApp}
import cats.implicits.*
import com.fortyseven.configuration.dataGenerator.{DataGeneratorConfiguration, DataGeneratorConfigurationEffect}
import com.fortyseven.core.codecs.iot.IotModel.pneumaticPressureCodec
import com.fortyseven.coreheaders.DataGeneratorHeader
import com.fortyseven.coreheaders.model.app.model.*
import com.fortyseven.coreheaders.model.iot.model.*
import com.fortyseven.coreheaders.model.iot.types.*
import fs2.kafka.*
import io.confluent.kafka.serializers.KafkaAvroSerializer

object DataGenerator extends IOApp.Simple:

  val run: IO[Unit] = new DataGenerator[IO].run

final class DataGenerator[F[_]: Async] extends DataGeneratorHeader[F]:

  override def run: F[Unit] = for
    conf <- new DataGeneratorConfigurationEffect[F].configuration
    _    <- runWithConfig(conf)
  yield ()

  import VulcanSerdes.*

  private val sourceTopic = "data-generator"

  private def runWithConfig(dg: DataGeneratorConfiguration): F[Unit] =
    val producerSettings = ProducerSettings[F, String, Array[Byte]]
      .withBootstrapServers(dg.kafkaProducer.bootstrapServers.toString)
      .withProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, dg.kafkaProducer.valueSerializerClass.toString)

    val pneumaticPressureSerializer = avroSerializer(
      Config(dg.kafkaProducer.schemaRegistryUrl.toString),
      includeKey = dg.kafkaProducer.includeKey
    )(using pneumaticPressureCodec)

    KafkaProducer
      .stream(producerSettings)
      .flatMap { producer =>
        generatePneumaticPressure
          .map { committable =>
            val key   = committable.getClass.getSimpleName
            val value = pneumaticPressureSerializer.serialize(sourceTopic, committable)
            ProducerRecords.one(ProducerRecord(sourceTopic, key, value))
          }
          .evalMap(producer.produce)
          .groupWithin(
            dg.kafkaProducer.commitBatchWithinSize.toString.toInt,
            dg.kafkaProducer.commitBatchWithinTime.toString.toInt.seconds
          )
          .evalMap(_.sequence)
      }
      .compile
      .drain

  override def generateBatteryCharge: fs2.Stream[F, BatteryCharge] = ???

  override def generateBreaksUsage: fs2.Stream[F, BreaksUsage] = ???

  override def generateGPSPosition: fs2.Stream[F, GPSPosition] =
    def emitLoop(latValue: Double, lonValue: Double): fs2.Stream[F, GPSPosition] =
      def getValue(value: Double) = value - math.random() * 1e-3
      (Latitude(getValue(latValue)), Longitude(getValue(lonValue))) match
        case (Right(lat), Right(lon)) => fs2.Stream.emit(GPSPosition(lat, lon)) ++ emitLoop(lat, lon)
        case _                        => emitLoop(latValue, lonValue)
    emitLoop(latValue = 2.0, lonValue = 2.0) // ToDo: Soft-code initial coordinate values

  override def generatePneumaticPressure: fs2.Stream[F, PneumaticPressure] =
    def emitLoop(pValue: Double): fs2.Stream[F, PneumaticPressure] =
      Bar(pValue - math.random() * 1e-3) match
        case Right(p) => fs2.Stream.emit(PneumaticPressure(p)) ++ emitLoop(p)
        case _        => emitLoop(pValue)

    emitLoop(pValue = 2.0) // ToDo: Soft-code initial value

  override def generateWheelRotation: fs2.Stream[F, WheelRotation] = ???
