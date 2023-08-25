<h1 id="data-streaming-diamond-architecture">Data streaming Diamond Architecture</h1>

<h2 id="index">Index</h2>

- [Pre-requisites](#pre-requisites)
- [How To](#how-to)
  - [Sanity checks](#sanity-checks)
  - [Integration checks](#integration-checks)
  - [Run with sbt](#run-with-sbt)
  - [Run with Docker](#run-with-docker)
    - [Running for the first time](#running-for-the-first-time)
      - [Data Generator Image](#data-generator-image)
      - [Spark Image](#spark-image)
    - [Start the flow of events](#start-the-flow-of-events)
    - [Spark Submit](#spark-submit)
- [License](#license)

Building an end-to-end data streaming pipeline, with a dummy data generator, a kafka consumer and processor modules,
using well established open-source libraries:

- [Apache Kafka](https://kafka.apache.org/)
- [Apache Flink](https://flink.apache.org/)
- [Apache Spark - Spark Streaming](https://spark.apache.org/docs/latest/streaming-programming-guide.html)

This project applies [Diamond Architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)) design 
patterns and follows a [Kappa Architecture](https://www.newsletter.swirlai.com/p/sai-13-lambda-vs-kappa-architecture) 
data processing strategy.

For the motivation on using the Diamond Architecture or the Kappa Architecture, click on the following links:

- [Diamond Architecture Motivation](./docs/diamondArchitecture.md)
- [Kappa Architecture Motivation](./docs/kappaArchitecture.md)

<h2 id="pre-requisites">Pre-requisites</h2>

- [JDK](https://openjdk.org/projects/jdk/20/) - At least Java 9.0. Recommended > 11.0
- [SBT](https://www.scala-sbt.org/download.html) - At least 1.9.0
- [Docker Compose](https://docs.docker.com/compose/install/linux/)
- [Kafkacat](https://formulae.brew.sh/formula/kcat)

[<p style="text-align: right;">Go back to Index</p>](#index)

<h2 id="how-to">How To</h2>

<h3 id="sanity-checks">Sanity checks</h3>

- Compile src:
```bash
sbt c;
```
- Compile test:
```bash
sbt ct;
```
- Format:
```bash
sbt styleFix;
```
- Test:
```bash
sbt t;
```
- Code coverage:
```bash
sbt runCoverage;
```

[<p style="text-align: right;">Go back to Index</p>](#index)

<h3 id="integration-checks">Integration checks</h3>

Right now, there is only an integration test available for Apache Flink. Before you run the integration test, a docker
environment **must be** available in your computer. The integration test run on the library `TestContainers` so there 
is no need to do anything else on docker (other than having an environment available).

```bash
sbt flinkIT;
```

[<p style="text-align: right;">Go back to Index</p>](#index)

<h3 id="run-with-sbt">Run with sbt</h3>

The modules use kafka topics to communicate among them. Thus, the kafka consumer **must be** running on Docker.
Other than that, the rest of the Application can be executed using the sbt command line.

1. Start the kafka consumer
```bash
docker-compose -f ./docker/docker-compose-kafka.yml up -d;
```
2. Start the Data Generator
```bash
sbt generateData;
```
3. Execute Apache Flink or Spark
```bash
sbt runFlink;
```
```bash
sbt runSpark;
```
4. Stop Docker:
```bash
docker-compose -f ./docker/docker-compose-kafka.yml down;
```

[<p style="text-align: right;">Go back to Index</p>](#index)

<h3 id="run-with-docker">Run with Docker</h3>

[<p style="text-align: right;">Go back to Index</p>](#index)

<h4 id="running-for-the-first-time">Running for the first time</h4>

There are two custom images in this project: `data-generator` and `processor-spark`. These images have to be generated.
If you have already generated the images, you can skip to the section [Start the flow of events](#start-the-flow-of-events) 

[<p style="text-align: right;">Go back to Index</p>](#index)

<h5 id="data-generator-image">Data Generator Image</h5>

```bash
sbt data-generator/assembly; data-generator/docker:publishLocal;
```

[<p style="text-align: right;">Go back to Index</p>](#index)

<h5 id="spark-image">Spark Image</h5>

```bash
sbt processor-spark/c;
```

The Spark image is build from a [Dockerfile](./03-o-processor-spark/docker/Dockerfile). 

```bash
docker build ./03-o-processor-spark/docker/ -t cluster-apache-spark:3.4.1;
```

[<p style="text-align: right;">Go back to Index</p>](#index)

<h4 id="start-the-flow-of-events">Start the flow of events</h4>

The first thing that is needed is to have some events coming through the kafka topics.

```bash
docker-compose \
-f ./docker/docker-compose-generator.yml \
-f ./docker/docker-compose-kafka.yml \
up -d
```
Composing up both images together will trigger the `data-generator` autonomously. Before lunching the app, inspect Kafka
topics and verify that there are the following topics:

```shell
kcat -b localhost:9092 -L
```

You should see something like this:

```
Metadata for all topics (from broker 1: localhost:9092/1):
 1 brokers:
  broker 1 at localhost:9092 (controller)
 4 topics:
  topic "data-generator-gps" with 1 partitions:
    partition 0, leader 1, replicas: 1, isrs: 1
  topic "data-generator-pp" with 1 partitions:
    partition 0, leader 1, replicas: 1, isrs: 1
  topic "_schemas" with 1 partitions:
    partition 0, leader 1, replicas: 1, isrs: 1
  topic "__consumer_offsets" with 50 partitions:
    partition 0, leader 1, replicas: 1, isrs: 1
[...]
```

[<p style="text-align: right;">Go back to Index</p>](#index)

<h4 id="spark-submit">Spark Submit</h4>

1. Init the Spark Cluster
```bash
docker-compose \
-f ./docker/docker-compose-spark.yml \
up -d
```
2. Generates Spark jar file
```bash
sbt "processor-spark / assembly;"
```

3. Copy the fat jar from the folder app-jar into the running container (master)
```bash
docker cp "./04-o-processor-spark/target/scala-3.3.0/spark-app.jar" "docker-spark-master-1:/opt/spark/app-jar"
```

4. Execute Spark via spark-submit

The spark-submit uses the default 'client' deploy mode here, so the driver will run on the master node and the job's 
stdout will be printed to the terminal console. You can also do `--deploy-mode cluster`, which will cause the driver 
to run on one of the worker nodes. For experimentation, client mode is slightly more convenient.

```bash
docker exec docker-spark-master-1 /opt/spark/bin/spark-submit \
--packages org.apache.spark:spark-sql-kafka-0-10_2.13:3.4.1 \
--master spark://spark:7077 \
--deploy-mode client \
--driver-memory 1G \
--executor-memory 2G \
--total-executor-cores 2 \
--class com.fortyseven.processor.spark.run \
app-jar/spark-app.jar
```

Once you have finished running the app, stop docker
 
```shell
docker-compose \
-f ./docker/docker-compose-generator.yml \
-f ./docker/docker-compose-kafka.yml \
-f ./docker/docker-compose-spark.yml \
down
```
[<p style="text-align: right;">Go back to Index</p>](#index)

<h2 id="license">License</h2>

`Apache-2.0`, see [LICENSE](LICENSE.md)
