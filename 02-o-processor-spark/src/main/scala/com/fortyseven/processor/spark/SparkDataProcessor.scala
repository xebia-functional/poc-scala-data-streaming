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
import org.apache.spark.internal.Logging
import org.apache.spark.sql.streaming.DataStreamReader
import org.apache.spark.sql.{DataFrame, DataFrameReader, DataFrameWriter, Row, SparkSession}

import cats.Applicative
import cats.effect.IO
import cats.effect.kernel.Async
import com.fortyseven.coreheaders.configuration.{ReaderConfiguration, SparkProcessorConfiguration, WriterConfiguration}

private[spark] final class SparkDataProcessor[F[_]: Async](sparkSession: SparkSession):

  def run(sparkProcessorConfiguration: SparkProcessorConfiguration): F[Unit] =

    def setReader(readerConfiguration: ReaderConfiguration): DataFrameReader =
      sparkSession.read
        .format("kafka")
        .option(
          "kafka.bootstrap.servers",
          readerConfiguration.kafkaStreamConfiguration.bootstrapServers.asString
        )
        .option(
          "subscribePattern",
          readerConfiguration.kafkaStreamConfiguration.topic.asString
        )
        .option(
          "startingOffsets",
          readerConfiguration.kafkaStreamConfiguration.startingOffsets.asString
        )
        .option(
          "endingOffsets",
          readerConfiguration.kafkaStreamConfiguration.endingOffsets.asString
        )

    def setLogic(dataFrameReader: DataFrameReader): DataFrame = dataFrameReader.load()

    def setWriter(writerConfiguration: WriterConfiguration, dataFrame: DataFrame): DataFrameWriter[Row] =
      dataFrame.write.format(writerConfiguration.format.asString)

    Async.apply.delay(
      setWriter(
        sparkProcessorConfiguration.writerConfiguration,
        setLogic(
          setReader(
            sparkProcessorConfiguration.readerConfiguration
          )
        )
      ).save()
    )
