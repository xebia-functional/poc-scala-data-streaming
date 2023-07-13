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

import com.fortyseven.coreheaders.SparkProcessorHeader
import com.fortyseven.processor.spark.config.ProcessorSparkConfiguration
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures

class DataProcessor extends SparkProcessorHeader:

  override def process(): Unit =
    ConfigSource.default.at("processor-spark").load[ProcessorSparkConfiguration] match
      case Right(conf) => runWithConfiguration(conf)
      case Left(fail)  => throw new Exception(s"${fail.head.description}")

  private def runWithConfiguration(sconf: ProcessorSparkConfiguration): Unit =

    val sparkConf: SparkConf = new SparkConf()
      .setAppName(sconf.app.appName.asString)
      .setMaster(sconf.app.masterURL.asString)
      .set("spark.streaming.backpressure.enabled", sconf.streaming.backpressureEnabled.toString)
      .set("spark.streaming.blockInterval", sconf.streaming.blockInterval.toString)
      .set(
        "spark.streaming.stopGracefullyOnShutdown",
        sconf.streaming.stopGracefullyOnShutdown.toString
      )

    val spark = SparkSession.builder.config(sparkConf).getOrCreate()

    new SparkDataProcessor(spark).run(sconf)
