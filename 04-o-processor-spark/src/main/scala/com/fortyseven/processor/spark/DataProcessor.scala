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

import com.fortyseven.common.api.ConfigurationAPI
import com.fortyseven.output.api.SparkProcessorAPI
import com.fortyseven.processor.spark.configuration.ProcessorConfiguration
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import pureconfig.ConfigReader.Result

class DataProcessor extends SparkProcessorAPI[Result, ProcessorConfiguration]:

  override def process(config: ConfigurationAPI[Result, ProcessorConfiguration]): Unit =
    config
      .load().fold(
        configReaderFailures => throw new Exception(configReaderFailures.toString),
        processorConfiguration => runWithConfiguration(processorConfiguration)
      )

  private def runWithConfiguration(processorConfiguration: ProcessorConfiguration): Unit =

    val sparkConf: SparkConf = new SparkConf()
      .setAppName(processorConfiguration.app.appName.asString)
      .setMaster(processorConfiguration.app.masterURL.asString)
      .set("spark.streaming.backpressure.enabled", processorConfiguration.streaming.backpressureEnabled.toString)
      .set("spark.streaming.blockInterval", processorConfiguration.streaming.blockInterval.toString)
      .set(
        "spark.streaming.stopGracefullyOnShutdown",
        processorConfiguration.streaming.stopGracefullyOnShutdown.toString
      )

    val spark = SparkSession.builder.config(sparkConf).getOrCreate()

    new SparkDataProcessor(spark).run(processorConfiguration)
