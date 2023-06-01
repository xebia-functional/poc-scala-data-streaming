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

package com.fortyseven

import cats.effect.{IO, IOApp}
import com.fortyseven.datagenerator.DataGenerator
import com.fortyseven.kafkaconsumer.KafkaConsumer
import com.fortyseven.processor.spark.DataProcessor
import com.fortyseven.pureconfig.*
import org.typelevel.log4cats.slf4j.Slf4jLogger

object SparkMain extends IOApp.Simple:

  override def run: IO[Unit] = for
    logger         <- Slf4jLogger.create[IO]
    dataGenConf    <- DataGeneratorConfigurationLoader[IO].load()
    _              <- logger.info(s"DataGeneratorConfiguration: $dataGenConf")
    consumerConf   <- KafkaConsumerConfigurationLoader[IO].load()
    _              <- logger.info(s"KafkaConsumerConfiguration: $consumerConf")
    processorConf  <- SparkProcessorConfigurationLoader[IO].load()
    _              <- logger.info(s"SparkProcessorConfiguration: $processorConf")
    _              <- logger.info("Start data generator")
    dataGenFiber   <- new DataGenerator[IO].generate(DataGeneratorConfigurationLoader[IO]).start
    _              <- logger.info("Start kafka consumer")
    consumerFiber  <- new KafkaConsumer[IO].consume(KafkaConsumerConfigurationLoader[IO]).start
    _              <- logger.info("Start spark processor")
    processorFiber <- new DataProcessor[IO].process(SparkProcessorConfigurationLoader[IO]).start
    _              <- dataGenFiber.join
    _              <- consumerFiber.join
    _              <- processorFiber.join
  yield ()
