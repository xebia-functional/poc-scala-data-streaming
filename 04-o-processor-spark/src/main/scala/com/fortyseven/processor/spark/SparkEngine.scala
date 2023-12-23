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

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.DataFrameReader
import org.apache.spark.sql.DataFrameWriter
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession

import com.fortyseven.common.configuration.SparkProcessorConfigurationI
import com.fortyseven.common.configuration.SparkProcessorReaderConfigurationI
import com.fortyseven.common.configuration.SparkProcessorWriterConfigurationI

final private[spark] class SparkEngine(sparkSession: SparkSession):

  def run(sparkProcessorConfiguration: SparkProcessorConfigurationI): Unit =

    extension (sparkSession: SparkSession)
      def buildReader(reader: SparkProcessorReaderConfigurationI): DataFrameReader = sparkSession
        .read
        .format("kafka")
        .option("kafka.bootstrap.servers", reader.kafka.bootstrapServers.value)
        .option("subscribePattern", reader.kafka.topic.value)
        .option("startingOffsets", reader.kafka.startingOffsets.value)
        .option("endingOffsets", reader.kafka.endingOffsets.value)

    extension (dataFrameReader: DataFrameReader) def applyLogic(): DataFrame = dataFrameReader.load()

    extension (dataFrame: DataFrame)
      def buildWriter(writer: SparkProcessorWriterConfigurationI): DataFrameWriter[Row] = dataFrame
        .write
        .format(writer.format.toString)

    sparkSession
      .buildReader(sparkProcessorConfiguration.reader)
      .applyLogic()
      .buildWriter(sparkProcessorConfiguration.writer)
      .save()

  end run

end SparkEngine
