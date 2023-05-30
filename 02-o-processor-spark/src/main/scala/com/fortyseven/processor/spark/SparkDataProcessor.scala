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

package com.fortyseven.processor.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

import cats.Applicative
import cats.effect.IO
import cats.effect.kernel.Async
import com.fortyseven.coreheaders.configuration.SparkProcessorConfiguration

private[spark] final class SparkDataProcessor[F[_]: Async](sparkSession: SparkSession):

  def run(sparkProcessorConfiguration: SparkProcessorConfiguration): F[Unit] =
    val df: DataFrame = sparkSession.read
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribePattern", "data-generator-*")
      .option("startingOffsets", "earliest")
      .option("endingOffsets", "latest")
      .load()

    Async.apply.pure(
      df.write.format("console").save()
    )
