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

package com.fortyseven.main

import cats.effect.IO
import cats.effect.IOApp

import com.fortyseven.kafkaconsumer.KafkaConsumer
import com.fortyseven.processor.flink.FlinkProcessor
import com.fortyseven.pureconfig.flink.FlinkProcessorConfigurationLoader
import com.fortyseven.pureconfig.kafkaconsumer.KafkaConsumerConfigurationLoader

import org.typelevel.log4cats.slf4j.Slf4jLogger

object FlinkPureconfigMain extends IOApp.Simple:

  override def run: IO[Unit] =
    for
      logger <- Slf4jLogger.create[IO]
      kafkaConf <- new KafkaConsumerConfigurationLoader[IO].load()
      flinkConf <- new FlinkProcessorConfigurationLoader[IO].load()
      _ <- logger.info("Start kafka consumer")
      consumerFiber <- new KafkaConsumer[IO].consume(kafkaConf).start
      _ <- logger.info("Start flink processor")
      processorFiber <- new FlinkProcessor[IO].process(flinkConf).start
      _ <- consumerFiber.join
      _ <- processorFiber.join
    yield ()

end FlinkPureconfigMain
