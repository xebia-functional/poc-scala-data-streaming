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

import com.fortyseven.processor.spark.configuration.{ProcessorConfiguration, ReaderConfiguration, WriterConfiguration}
import org.apache.spark.sql.{DataFrame, DataFrameReader, DataFrameWriter, Row, SparkSession}

private[spark] final class SparkDataProcessor(sparkSession: SparkSession):

  def run(processorConfiguration: ProcessorConfiguration): Unit =

    extension (sparkSession: SparkSession)
      def buildReader(reader: ReaderConfiguration): DataFrameReader =
        sparkSession.read
          .format("kafka")
          .option(
            "kafka.bootstrap.servers",
            reader.kafka.bootstrapServers.asString
          )
          .option(
            "subscribePattern",
            reader.kafka.topic.asString
          )
          .option(
            "startingOffsets",
            reader.kafka.startingOffsets.asString
          )
          .option(
            "endingOffsets",
            reader.kafka.endingOffsets.asString
          )

    extension (dataFrameReader: DataFrameReader) def applyLogic(): DataFrame = dataFrameReader.load()

    extension (dataFrame: DataFrame)
      def buildWriter(writer: WriterConfiguration): DataFrameWriter[Row] =
        dataFrame.write.format(writer.format.asString)

    sparkSession
      .buildReader(processorConfiguration.reader)
      .applyLogic()
      .buildWriter(processorConfiguration.writer)
      .save()
