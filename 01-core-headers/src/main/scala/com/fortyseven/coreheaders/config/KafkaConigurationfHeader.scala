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

package com.fortyseven.coreheaders.config

import scala.concurrent.duration.FiniteDuration

import org.apache.kafka.common.record.CompressionType

import fs2.kafka.AutoOffsetReset

trait KafkaConigurationfHeader:

  val brokerConfiguration: BrokerHeader

  val consumerConfiguration: ConsumerHeader

  val producerConfiguration: ProducerHeader

  val streamConfiguration: StreamHeader

end KafkaConigurationfHeader

trait BrokerHeader:

  val brokerAddress: String

end BrokerHeader

trait ConsumerHeader:

  val autoOffsetReset: AutoOffsetReset

  val groupId: String

end ConsumerHeader

trait ProducerHeader:

  val maxConcurrent: Int

  val compressionType: CompressionType

end ProducerHeader

trait StreamHeader:

  val inputTopic: String

  val outputTopic: String

  val maxConcurrent: Int

  val commitBatchWithinSize: Int

  val commitBatchWithinTime: FiniteDuration

end StreamHeader
