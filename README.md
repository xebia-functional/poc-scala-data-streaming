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
- [Language](#language)
  - [Scala 2](#scala-2)
  - [Scala 3](#scala-3)
    - [Compile-time operations](#compile-time-operations)
    - [Context Bounds](#context-bounds)
    - [Enumerations](#enumerations)
    - [Export Clauses](#export-clauses)
    - [Extension Methods](#extension-methods)
    - [Given Instances](#given-instances)
    - [Implicit Conversions](#implicit-conversions)
    - [Inline](#inline)
    - [New Control Syntax](#new-control-syntax)
    - [Opaque Type Aliases](#opaque-type-aliases)
    - [Using Clauses](#using-clauses)
- [Frameworks](#frameworks)
  - [Apache Flink](#apache-flink)
  - [Apache Kafka](#apache-kafka)
  - [Apache Spark](#apache-spark)
- [Libraries](#libraries)
  - [Apache Avro](#apache-avro)
  - [Cats](#cats)
  - [Ciris](#ciris)
  - [FS2](#fs2)
  - [Logback](#logback)
  - [Munit](#munit)
  - [Pureconfig](#pureconfig)
  - [Test Containers](#test-containers)
  - [Vulcan](#vulcan)
- [Tooling](#tooling)
  - [Assembly](#assembly)
  - [Explicit Dependencies](#explicit-dependencies)
  - [ScalaFix](#scalafix)
  - [ScalaFmt](#scalafmt)
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

[Go back to Index](#index)

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

[Go back to Index](#index)

<h3 id="integration-checks">Integration checks</h3>

Right now, there is only an integration test available for Apache Flink. Before you run the integration test, a docker
environment **must be** available in your computer. The integration test run on the library `TestContainers` so there 
is no need to do anything else on docker (other than having an environment available).

```bash
sbt flinkIT;
```

[Go back to Index](#index)

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

[Go back to Index](#index)

<h3 id="run-with-docker">Run with Docker</h3>

<h4 id="running-for-the-first-time">Running for the first time</h4>

There are two custom images in this project: `data-generator` and `processor-spark`. These images have to be generated.
If you have already generated the images, you can skip to the section [Start the flow of events](#start-the-flow-of-events)

<h5 id="data-generator-image">Data Generator Image</h5>

```bash
sbt data-generator/assembly; data-generator/docker:publishLocal;
```

<h5 id="spark-image">Spark Image</h5>

```bash
sbt processor-spark/c;
```

The Spark image is build from a [Dockerfile](./03-o-processor-spark/docker/Dockerfile). 

```bash
docker build ./03-o-processor-spark/docker/ -t cluster-apache-spark:3.4.1;
```

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
[Go back to Index](#index)

<h2 id="language">Language</h2>

<h3 id="scala-2">Scala 2</h3>

More information at [Relationship with Scala 2 Implicits](https://docs.scala-lang.org/scala3/reference/contextual/relationship-implicits.html#).

[Go back to Index](#index)

<h3 id="scala-3">Scala 3</h3>

<h4 id="compile-time-operations">Compile-time operations</h4>

More information at [Compile-time operations](https://docs.scala-lang.org/scala3/reference/metaprogramming/compiletime-ops.html).

<h4 id="context-bounds">Context Bounds</h4>

More information at [Context Bounds](https://docs.scala-lang.org/scala3/reference/contextual/context-bounds.html).

<h4 id="enumerations">Enumerations</h4>

More information at [Enums](https://docs.scala-lang.org/scala3/reference/enums/index.html).

<h4 id="export-clauses">Export Clauses</h4>

More information at [Export Clauses](https://docs.scala-lang.org/scala3/reference/other-new-features/export.html).

<h4 id="extension-methods">Extension Methods</h4>

More information at [Extension Methods](https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html).

<h4 id="given-instances">Given Instances</h4>

More information at [Given Instances](https://docs.scala-lang.org/scala3/reference/contextual/givens.html).

<h4 id="implicit-conversions">Implicit Conversions</h4>

More information at [Implicit Conversions](https://docs.scala-lang.org/scala3/reference/contextual/conversions.html).

<h4 id="inline">Inline</h4>

More information at [Inline](https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html).

<h4 id="new-control-syntax">New Control Syntax</h4>

More information at [New Control Syntax](https://docs.scala-lang.org/scala3/reference/other-new-features/control-syntax.html).

<h4 id="opaque-type-aliases">Opaque Type Aliases</h4>

More information at [Opaque Type Aliases](https://docs.scala-lang.org/scala3/reference/other-new-features/opaques.html).

<h4 id="using-clauses">Using Clauses</h4>

More information at [Using Clauses](https://docs.scala-lang.org/scala3/reference/contextual/using-clauses.html).

[Go back to Index](#index)


<h2 id="frameworks">Frameworks</h2>

<h3 id="apache-flink">Apache Flink</h3>

More information at [Apache Flink](https://flink.apache.org/).

[Go back to Index](#index)

<h3 id="apache-kafka">Apache Kafka</h3>

More information at [Apache Kafka](https://kafka.apache.org/).

[Go back to Index](#index)

<h3 id="apache-spark">Apache Spark</h3>

More information at [Apache Spark](https://spark.apache.org/).

[Go back to Index](#index)

<h2 id="libraries">Libraries</h2>

<h3 id="apache-avro">Apache Avro</h3>

More information at [Apache Avro](https://avro.apache.org/).

[Go back to Index](#index)

<h3 id="cats">Cats</h3>

More information at [Typelevel Cats](https://typelevel.org/cats/).

[Go back to Index](#index)

<h3 id="ciris">Ciris</h3>

More information at [Ciris](https://cir.is/).

[Go back to Index](#index)

<h3 id="fs2">FS2</h3>

More information at [FS2](https://fs2.io/#/).

[Go back to Index](#index)

<h3 id="logback">Logback</h3>

More information at [QOS Logback](https://logback.qos.ch/).

[Go back to Index](#index)

<h3 id="munit">Munit</h3>

More information at [Scalameta Munit](https://scalameta.org/munit/).

[Go back to Index](#index)

<h3 id="pureconfig">Pureconfig</h3>

More information at [PureConfig](https://pureconfig.github.io/).

[Go back to Index](#index)

<h3 id="test-containers">Test Containers</h3>

More information at [Testcontainers-scala](https://github.com/testcontainers/testcontainers-scala).

[Go back to Index](#index)

<h3 id="vulcan">Vulcan</h3>

More information at [FD4S Vulcan](https://fd4s.github.io/vulcan/).

[Go back to Index](#index)

<h2 id="tooling">Tooling</h2>

<h3 id="assembly">Assembly</h3>

More information at [sbt-assembly](https://github.com/sbt/sbt-assembly). 

[Go back to Index](#index)

<h3 id="explicit-dependencies">Explicit Dependencies</h3>

More information at [sbt-explicit-dependencies](https://github.com/cb372/sbt-explicit-dependencies).

[Go back to Index](#index)

<h3 id="scalafix">ScalaFix</h3>

More information at [Scala Center](https://scalacenter.github.io/scalafix/docs/users/installation.html).

[Go back to Index](#index)

<h3 id="scalafmt">ScalaFmt</h3>

More information at [Scalameta Scalafmt](https://scalameta.org/scalafmt/).

[Go back to Index](#index)

<h2 id="license">License</h2>

`Apache-2.0`, see [LICENSE](LICENSE.md)
