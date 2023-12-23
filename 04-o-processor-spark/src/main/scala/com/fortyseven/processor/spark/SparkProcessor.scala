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
import org.apache.spark.sql.SparkSession

import com.fortyseven.common.configuration.SparkProcessorConfigurationI
import com.fortyseven.output.api.SparkProcessorAPI

class SparkProcessor extends SparkProcessorAPI:

  override def process[Configuration <: SparkProcessorConfigurationI](config: Configuration): Unit =
    runWithConfiguration(config)

  private def runWithConfiguration(sparkProcessorConfiguration: SparkProcessorConfigurationI): Unit =

    val sparkConf: SparkConf = new SparkConf()
      .setAppName(sparkProcessorConfiguration.app.appName.toString)
      .setMaster(sparkProcessorConfiguration.app.masterUrl.toString)
      .set("spark.streaming.backpressure.enabled", sparkProcessorConfiguration.streaming.backpressureEnabled.toString)
      .set("spark.streaming.blockInterval", sparkProcessorConfiguration.streaming.blockInterval.toString)
      .set(
        "spark.streaming.stopGracefullyOnShutdown",
        sparkProcessorConfiguration.streaming.stopGracefullyOnShutdown.toString
      )

    val spark = SparkSession.builder.config(sparkConf).getOrCreate()

    new SparkEngine(spark).run(sparkProcessorConfiguration)

  end runWithConfiguration

end SparkProcessor
