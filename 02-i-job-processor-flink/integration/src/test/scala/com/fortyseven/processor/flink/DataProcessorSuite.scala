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

import cats.effect.*
import cats.implicits.*
import munit.*
import com.dimafeng.testcontainers.lifecycle.and
import com.dimafeng.testcontainers.{KafkaContainer, SchemaRegistryContainer}
import com.dimafeng.testcontainers.munit.TestContainersForAll
import org.testcontainers.Testcontainers
import org.testcontainers.containers.Network
import org.testcontainers.utility.DockerImageName
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.*

class DataProcessorSuite extends CatsEffectSuite with TestContainersForAll:

  override def munitTimeout: Duration = new FiniteDuration(60, TimeUnit.SECONDS)

  override type Containers = KafkaContainer `and` SchemaRegistryContainer

  override def startContainers(): Containers =
    val hostName = "kafka-broker"
    val network  = Network.newNetwork()

    val kafkaContainer: KafkaContainer =
      KafkaContainer.Def(DockerImageName.parse(KafkaContainer.defaultDockerImageName)).createContainer()

    kafkaContainer.container.withNetwork(network).withNetworkAliases(hostName).withReuse(false)
    kafkaContainer.start()

    val schemaRegistryContainer = SchemaRegistryContainer.Def(network, hostName, KafkaContainer.defaultTag).start()

    kafkaContainer `and` schemaRegistryContainer

  test("Flink processing") {
    val sourceTopic = "input-topic"
    val sinkTopic   = "output-topic"

    withContainers { case kafka `and` _ =>
      val bootstrapServers  = kafka.bootstrapServers
      val kafkaExternalPort = kafka.container.getMappedPort(9093)

      for
        logger <- Slf4jLogger.create[IO]
        _      <- logger.debug("Create topics")
        _      <- KafkaUtils.createTopics[IO](bootstrapServers, sourceTopic, sinkTopic)
        _      <- logger.debug("Produce msgs")
        _      <- KafkaUtils.produce[IO](bootstrapServers, sourceTopic, "key1", "sensorId1")
        _      <- KafkaUtils.produce[IO](bootstrapServers, sourceTopic, "key2", "sensorId2")
        _      <- KafkaUtils.produce[IO](bootstrapServers, sourceTopic, "key1", "id3")
        _      <- logger.debug("Start flink in background")
        process = DataProcessor.runLocal(s"localhost:$kafkaExternalPort", sourceTopic, sinkTopic)
        fiber  <- IO.blocking(process).background.use { _.start }
        _      <- fiber.join
        _      <- logger.warn("Wait 30 seconds for Flink to process")
        _      <- IO.sleep(30.seconds)
        _      <- logger.debug("Consume data")
        result <- KafkaUtils.consumerStream[IO](bootstrapServers, sinkTopic).take(3).compile.toList.timeout(15.seconds)
        _      <- fiber.cancel
      yield assertEquals(result.sorted, List("id1", "id2", "id3").sorted)
    }
  }
