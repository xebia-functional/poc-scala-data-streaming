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

import org.apache.spark.sql.SparkSession

object HelloWorld:

  @main def run =
    val spark = SparkSession.builder.appName("StructuredNetworkWordCount").config("spark.master", "local").getOrCreate()

    import spark.implicits.*
    // Create DataFrame representing the stream of input lines from connection to localhost:9999
    val lines = spark.readStream.format("socket").option("host", "localhost").option("port", 9999).load()

    // Split the lines into words
    val words = lines.as[String].flatMap(_.split(" "))

    // Generate running word count
    val wordCounts = words.groupBy("value").count()

    // Start running the query that prints the running counts to the console
    val query = wordCounts.writeStream.outputMode("complete").format("console").start()

    query.awaitTermination()
