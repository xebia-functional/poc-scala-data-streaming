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

package com.fortyseven.configuration

import ciris.ConfigDecoder

object CommonConfiguration:

  val kafkaBrokerAddress = "localhost:9092"

  val schemaRegistryUrl = "http://localhost:8081"

  enum KafkaCompressionType:

    case none, gzip, snappy, lz4, zstd

  object KafkaCompressionType:

    def apply(s: String): KafkaCompressionType = s match
      case "lz4"      => lz4
      case "zstd"     => zstd
      case "gzip"     => gzip
      case "snappy"   => snappy
      case "none" | _ => none

    given ConfigDecoder[String, KafkaCompressionType] =
      ConfigDecoder[String, String].map(KafkaCompressionType.apply)

  enum KafkaAutoOffsetReset:

    case Earliest, Latest, None

  object KafkaAutoOffsetReset:

    def apply(s: String): KafkaAutoOffsetReset = s match
      case "Earliest" => Earliest
      case "Latest"   => Latest
      case "None" | _ => None

    given ConfigDecoder[String, KafkaAutoOffsetReset] =
      ConfigDecoder[String, String].map(KafkaAutoOffsetReset.apply)
