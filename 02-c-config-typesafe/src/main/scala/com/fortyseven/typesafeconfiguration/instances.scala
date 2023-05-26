package com.fortyseven.typesafeconfiguration

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
