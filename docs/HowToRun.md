# How to Run?

Service stack can be divided in two groups:

* Dependency services: Kafka, Postgres, Prometheus, ...
* Apps: data generator, spark, flink, ...

In the `docker` folder, there are files for the different app groups, including the apps. Each app have its own "Main"
class, which means it can be executed as a regular Java app.

## Publish Docker app image

In order to generate a docker image for an specific app you need to follow these steps:

1. Log into SBT console: `sbt`
2. Select the app: ie `project data-generator`
3. Generate the über JAR: `assembly`
4. Generate the docker image and publish it locally: `docker:publishLocal`

## Run the services

In the `docker` folder, run `docker compose` passing the desired files. Each file contains the specification of one or 
more services.

### Data Generator

To run the data generator, we need the Kafka stack

```bash
docker compose -f docker-compose-kafka.yml -f docker-compose-generator.yml up -d
```

Alternatively, we could just start the Kafka stack

```bash
docker compose -f docker-compose-kafka.yml up -d
```

And then run the data generator from a SBT console:

```bash
sbt "project data-generator; run"
```

### Spark

Running Apache Spark has its caveats. It is important to follow the steps properly.

If you wish to run Spark in your local machine, you have to make sure that both `kafka-consumer` and `data-generator` are running on Docker.
Otherwise, Spark will not be able to read any data. Once the two services are running you can execute Spark locally.

* If you have installed Java 11 or older:

```bash
sbt "project processor-spark; run"
```

* If you have a newer version than Java 11, you must add the following flags:

```
--add-opens=java.base/java.lang=ALL-UNNAMED
--add-opens=java.base/java.lang.invoke=ALL-UNNAMED
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED
--add-opens=java.base/java.io=ALL-UNNAMED
--add-opens=java.base/java.net=ALL-UNNAMED
--add-opens=java.base/java.nio=ALL-UNNAMED
--add-opens=java.base/java.util=ALL-UNNAMED
--add-opens=java.base/java.util.concurrent=ALL-UNNAMED
--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED
--add-opens=java.base/sun.nio.cs=ALL-UNNAMED
--add-opens=java.base/sun.security.action=ALL-UNNAMED
--add-opens=java.base/sun.util.calendar=ALL-UNNAMED
```
Otherwise, Spark/Java classes will not be accesible and the program will fail.

If you wish to run Spark on Docker, you can execute the shell script runSpark.sh:

```bash
 .././runSpark.sh
```

The following script does these steps:
1) Generates the Spark-app jar file
```bash
sbt "processor-spark / assembly;"
```
2) Creates a folder were the jar can be copied
```bash
mkdir -p ./02-o-processor-spark/app-jar
```
3) Copies the generated jar into the new folder
```bash
cp "./02-o-processor-spark/target/scala-3.3.0/processor-spark-assembly-0.1.0-SNAPSHOT.jar" "./02-o-processor-spark/app-jar"
```
4) Renames the jar so it is easier to handle on the container
```bash
mv "./02-o-processor-spark/app-jar/processor-spark-assembly-0.1.0-SNAPSHOT.jar" "./02-o-processor-spark/app-jar/spark-app.jar"
```
5) Build the docker image based on the Dockerfile available in the Spark module
```bash
docker build ./02-o-processor-spark/docker/ -t cluster-apache-spark:3.4.1
```
6) Calls docker-compose with two nodes: one master node and one worker node
```bash
docker compose -f ./docker/docker-compose-spark.yml up -d --scale spark-worker=1

```
7) Copies the jar file from the folder to the container
```bash
docker cp "./02-o-processor-spark/app-jar/spark-app.jar" "docker-spark-master-1:/opt/spark/app-jar"

```
8) Executes the spark-submit command
```bash
docker exec docker-spark-master-1 /opt/spark/bin/spark-submit \
  --master spark://spark:7077 \
  --deploy-mode client \
  --driver-memory 1G \
  --executor-memory 2G \
  --total-executor-cores 2 \
  --class com.fortyseven.processor.spark.run \
  app-jar/spark-app.jar
```



