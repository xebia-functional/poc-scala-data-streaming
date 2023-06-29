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

import cats.effect.kernel.Async

import com.fortyseven.coreheaders.configuration.ReaderConfiguration
import com.fortyseven.coreheaders.configuration.SparkProcessorConfiguration
import com.fortyseven.coreheaders.configuration.WriterConfiguration
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.DataFrameReader
import org.apache.spark.sql.DataFrameWriter
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession

private[spark] final class SparkDataProcessor[F[_]: Async](sparkSession: SparkSession):

  def run(sparkProcessorConfiguration: SparkProcessorConfiguration): F[Unit] =

    def setReader(reader: ReaderConfiguration): DataFrameReader =
      sparkSession.read
        .format("kafka")
        .option(
          "kafka.bootstrap.servers",
          reader.kafkaStream.bootstrapServers.asString
        )
        .option(
          "subscribePattern",
          reader.kafkaStream.topic.asString
        )
        .option(
          "startingOffsets",
          reader.kafkaStream.startingOffsets.asString
        )
        .option(
          "endingOffsets",
          reader.kafkaStream.endingOffsets.asString
        )

    def setLogic(dataFrameReader: DataFrameReader): DataFrame = dataFrameReader.load()

    def setWriter(writer: WriterConfiguration, dataFrame: DataFrame): DataFrameWriter[Row] =
      dataFrame.write.format(writer.format.asString)

    Async.apply.delay(
      setWriter(
        sparkProcessorConfiguration.writer,
        setLogic(
          setReader(
            sparkProcessorConfiguration.reader
          )
        )
      ).save()
    )
