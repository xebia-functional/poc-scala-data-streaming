#!/bin/sh

# Assuming you've already run 'sbt assembly', the assembly jar will be in the target directory.
# We first copy it into the ./spark-jars directory, which is mounted as a volume into both the master and workers.
cp target/scala-3.1.1/spark-scala3-example-assembly-0.1.0-SNAPSHOT.jar ./spark-apps/spark-scala3-example.jar

cp ./target/scala-3.3.0/processor-spark-assembly-0.1.0-SNAPSHOT.jar ./spark-apps/processor-spark-assembly-0.1.0-SNAPSHOT.jar
cp ..../03-c-main/target/scala-3.3.0/main-assembly-0.1.0-SNAPSHOT.jar ./spark-apps/main-assembly-0.1.0-SNAPSHOT.jar

# We're using the default 'client' deploy mode here, so the driver will run on the master node
# and the job's stdout will be printed to the terminal console.
# You can also do `--deploy-mode cluster`, which will cause the driver to run on one of the worker nodes.
# I've confirmed that both modes work fine with Scala 3, but for our purposes client mode is slightly more convenient.
docker compose exec spark-master /opt/spark/bin/spark-submit \
  --master spark://spark-master:7077 \
  --total-executor-cores 2 \
  --class SparkMain \
  --driver-memory 4G \
  --executor-memory 1G \
  --jars /opt/spark-jars/processor-spark-assembly-0.1.0-SNAPSHOT.jar \
  /opt/spark-jars/main-assembly-0.1.0-SNAPSHOT.jar

