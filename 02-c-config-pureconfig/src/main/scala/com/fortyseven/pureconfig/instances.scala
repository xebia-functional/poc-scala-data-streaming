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

package com.fortyseven.pureconfig

import cats.syntax.all.*
import com.fortyseven.coreheaders.configuration.*
import com.fortyseven.coreheaders.configuration.internal.*
import com.fortyseven.coreheaders.configuration.internal.types.*
import pureconfig.ConfigReader
import pureconfig.error.ExceptionThrown

object instances:

  given ConfigReader[NonEmptyString] = ConfigReader.fromString(NonEmptyString.from(_).leftMap(ExceptionThrown.apply))

  given ConfigReader[PositiveInt] = ConfigReader[Int].emap(PositiveInt.from(_).leftMap(ExceptionThrown.apply))

  given ConfigReader[KafkaAutoOffsetReset] =
    ConfigReader.fromString(KafkaAutoOffsetReset.from(_).leftMap(ExceptionThrown.apply))

  given ConfigReader[KafkaCompressionType] =
    ConfigReader.fromString(KafkaCompressionType.from(_).leftMap(ExceptionThrown.apply))

  given ConfigReader[BrokerConfiguration] =
    ConfigReader.forProduct1("brokerAddress")(BrokerConfiguration.apply)

  given ConfigReader[ConsumerConfiguration] =
    ConfigReader.forProduct4(
      "TopicName",
      "AutoOffsetReset",
      "GroupId",
      "MaxConcurrent"
    )(ConsumerConfiguration.apply)

  given ConfigReader[ProducerConfiguration] =
    ConfigReader.forProduct6(
      "TopicName",
      "ValueSerializerClass",
      "MaxConcurrent",
      "CompressionType",
      "CommitBatchWithinSize",
      "CommitBatchWithinTime"
    )(ProducerConfiguration.apply)

  given ConfigReader[KafkaConfiguration] =
    ConfigReader.forProduct3("BrokerConfiguration", "Consumer", "Producer")(KafkaConfiguration.apply)

  given ConfigReader[SchemaRegistryConfiguration] =
    ConfigReader.forProduct1("schemaRegistryURL")(SchemaRegistryConfiguration.apply)

  given ConfigReader[DataGeneratorConfiguration] =
    ConfigReader.forProduct2("KafkaConfiguration", "SchemaRegistryConfiguration")(DataGeneratorConfiguration.apply)

  given ConfigReader[JobProcessorConfiguration] =
    ConfigReader.forProduct2("KafkaConfiguration", "SchemaRegistryConfiguration")(JobProcessorConfiguration.apply)

  given ConfigReader[KafkaConsumerConfiguration] =
    ConfigReader.forProduct1("KafkaConfiguration")(KafkaConsumerConfiguration.apply)
