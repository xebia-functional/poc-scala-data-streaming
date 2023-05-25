package com.fortyseven.processor.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

import cats.Applicative
import cats.effect.IO
import cats.effect.kernel.Async

private [spark] final class SparkDataProcessor[F[_]: Async](sparkSession: SparkSession):
  def run: F[Unit] =
    val df: DataFrame = sparkSession
      .read
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribePattern", "data-generator-*")
      .option("startingOffsets", "earliest")
      .option("endingOffsets", "latest")
      .load()

    Async.apply.pure(
      df.write
        .format("console")
        .save()
    )