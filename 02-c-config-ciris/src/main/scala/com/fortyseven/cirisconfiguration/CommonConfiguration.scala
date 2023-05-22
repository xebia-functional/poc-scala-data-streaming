package com.fortyseven.cirisconfiguration

object CommonConfiguration:

  val kafkaBrokerAddress = "localhost:9092"

  val schemaRegistryUrl = "http://localhost:8081"

  enum KafkaCompressionType:

    case none, gzip, snappy, lz4, zstd

  enum KafkaAutoOffsetReset:

    case Earliest, Latest, None
