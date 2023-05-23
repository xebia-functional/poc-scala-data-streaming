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
import cats.implicits.*
import com.fortyseven.configuration.{DataGeneratorConfiguration, JobProcessorConfiguration, KafkaConsumerConfiguration}
import com.fortyseven.core.codecs.iot.IotCodecs
import com.fortyseven.datagenerator.DataGenerator
import com.fortyseven.kafkaconsumer.KafkaConsumer
import com.fortyseven.processor.flink.DataProcessor
import org.typelevel.log4cats.slf4j.Slf4jLogger
import vulcan.Codec

object Main extends IOApp.Simple:

  override def run: IO[Unit] = for
    logger         <- Slf4jLogger.create[IO]
    dataGenConf    <- new DataGeneratorConfiguration[IO].load
    _              <- logger.info(s"DataGeneratorConfiguration: $dataGenConf")
    consumerConf   <- new KafkaConsumerConfiguration[IO].load
    _              <- logger.info(s"KafkaConsumerConfiguration: $consumerConf")
    processorConf  <- new JobProcessorConfiguration[IO].load
    _              <- logger.info(s"JobProcessorConfiguration: $processorConf")
    _              <- logger.info("Start data generator")
    dataGenFiber   <- new DataGenerator[IO].generate(DataGeneratorConfiguration[IO]).start
    _              <- logger.info("Start kafka consumer")
    consumerFiber  <- new KafkaConsumer[IO].consume(KafkaConsumerConfiguration[IO]).start
    _              <- logger.info("Start flink processor")
    processorFiber <- new DataProcessor[IO].process(JobProcessorConfiguration[IO]).start
    _              <- dataGenFiber.join
    _              <- consumerFiber.join
    _              <- processorFiber.join
  yield ()
