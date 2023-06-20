#!/bin/sh

sbt "main / assembly; processor-spark / assembly;"

mkdir -p ./02-o-processor-spark/spark-jars

cp "./03-c-main/target/scala-3.3.0/main-assembly-0.1.0-SNAPSHOT.jar" "./02-o-processor-spark/spark-jars"

cp "./02-o-processor-spark/target/scala-3.3.0/processor-spark-assembly-0.1.0-SNAPSHOT.jar" "./02-o-processor-spark/spark-jars"

cd "./02-o-processor-spark/docker" || returns

# Start a master node and 2 workers
docker compose up -d --scale spark-worker=2

# We're using the default 'client' deploy mode here, so the driver will run on the master node
# and the job's stdout will be printed to the terminal console.
# You can also do `--deploy-mode cluster`, which will cause the driver to run on one of the worker nodes.
# I've confirmed that both modes work fine with Scala 3, but for our purposes client mode is slightly more convenient.
docker compose exec spark-master /opt/spark/bin/spark-submit \
  --master spark://spark-master:7077 \
  --total-executor-cores 2 \
  --class com.fortyseven.SparkMain.run \
  --driver-memory 4G \
  --executor-memory 1G \
  --jars /opt/spark-jars/processor-spark-assembly-0.1.0-SNAPSHOT.jar \
  /opt/spark-jars/main-assembly-0.1.0-SNAPSHOT.jar
