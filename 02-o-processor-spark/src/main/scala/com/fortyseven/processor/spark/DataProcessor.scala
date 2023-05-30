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

import cats.effect.kernel.Async
import cats.implicits.{toFlatMapOps, toFunctorOps}
import com.fortyseven.coreheaders.configuration.{FlinkProcessorConfiguration, SparkProcessorConfiguration}
import com.fortyseven.coreheaders.{ConfigurationLoaderHeader, FlinkProcessorHeader, SparkProcessorHeader}

class DataProcessor[F[_]: Async] extends SparkProcessorHeader[F]:

  override def process(config: ConfigurationLoaderHeader[F, SparkProcessorConfiguration]): F[Unit] = for
    conf <- config.load()
    _    <- runWithConfiguration(conf)
  yield ()

  private def runWithConfiguration(sparkConfiguration: SparkProcessorConfiguration): F[Unit] =

    val sparkConf: SparkConf = new SparkConf()
      .setAppName(sparkConfiguration.applicationProperties.appName.asString)
      .setMaster(sparkConfiguration.applicationProperties.masterURL.asString)

    val spark = SparkSession.builder.config(sparkConf).getOrCreate()

    new SparkDataProcessor(spark).run(sparkConfiguration)
