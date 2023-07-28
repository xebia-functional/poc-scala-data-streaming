#!/bin/sh

# Builds the docker image using the Dockerfile
# The tag will be used in the docker-compose
docker build ./02-o-processor-spark/docker/ -t cluster-apache-spark:3.4.1

# Start a master node and 1 workers
docker compose -f ./docker/docker-compose-spark.yml up -d --scale spark-worker=1

# Generates the App's jar file
sbt "processor-spark / assembly;"

# Copies the jar from the folder app-jar into the running container (master)
docker cp "./02-o-processor-spark/target/scala-3.3.0/spark-app.jar" "docker-spark-master-1:/opt/spark/app-jar"

# We're using the default 'client' deploy mode here, so the driver will run on the master node
# and the job's stdout will be printed to the terminal console.
# You can also do `--deploy-mode cluster`, which will cause the driver to run on one of the worker nodes.
# I've confirmed that both modes work fine with Scala 3, but for our purposes client mode is slightly more convenient.
docker exec docker-spark-master-1 /opt/spark/bin/spark-submit \
  --packages org.apache.spark:spark-sql-kafka-0-10_2.13:3.4.1 \
  --master spark://spark:7077 \
  --deploy-mode client \
  --driver-memory 1G \
  --executor-memory 2G \
  --total-executor-cores 2 \
  --class com.fortyseven.processor.spark.run \
  app-jar/spark-app.jar