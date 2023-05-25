package com.fortyseven.processor.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import cats.effect.kernel.Async
import com.fortyseven.coreheaders.configuration.JobProcessorConfiguration
import com.fortyseven.coreheaders.{ConfigurationLoaderHeader, JobProcessorHeader}


class DataProcessor[F[_]: Async]:

  def run(): F[Unit] =

    val sparkConf: SparkConf = new SparkConf()
      .setAppName("StructuredKafkaRead")
      .setMaster("local")

    val spark = SparkSession
      .builder
      .config(sparkConf)
      .getOrCreate()

    new SparkDataProcessor(spark).run
