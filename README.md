<h1 id="data-streaming-diamond-architecture">Data Streaming & Diamond Architecture</h1>

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

>The scala.compiletime package contains helper definitions that provide support for compile-time operations over values.

The project uses two methods from the package [scala.compiletime](https://scala-lang.org/api/3.x/scala/compiletime.html):
- error
- requireConst

>The error method is used to produce user-defined compile errors during inline expansion. If an inline expansion results 
>in a call error(msgStr) the compiler produces an error message containing the given msgStr.

>The requireConst method checks at compiletime that the provided values is a constant after inlining and constant folding.

These two methods are used together in the apply method for refined type `Latitude`.

```scala 3
import scala.compiletime.{error, requireConst}

opaque type Latitude = Double

object Latitude:
  inline def apply(coordinate: Double): Latitude =
    requireConst(coordinate)
    inline if coordinate < -90.0 || coordinate > 90.0
    then error("Invalid latitude value. Accepted coordinate values are between -90.0 and 90.0.")
    else coordinate
end Latitude

val latOk: Latitude = Latitude(-3) // Compiles fine.
val latKo: Latitude = Latitude(91) // Won't compile and will display the error message.
```

More information at [Compile-time operations](https://docs.scala-lang.org/scala3/reference/metaprogramming/compiletime-ops.html).

<h4 id="context-bounds">Context Bounds</h4>

>A context bound is a shorthand for expressing the common pattern of a context parameter that depends on a type parameter.

Examples in module `03-u-data-generator`:
```scala 3
final class DataGenerator[F[_]: Async: Parallel]
final class ModelGenerators[F[_]: Temporal]
```

Examples in module `04-o-processor-flink`:
```scala 3
final class FlinkDataProcessor[F[_]: Applicative]
final class FlinkProcessor[F[_]: Async]
```

More information at [Context Bounds](https://docs.scala-lang.org/scala3/reference/contextual/context-bounds.html).

<h4 id="enumerations">Enumerations</h4>

**Beware**: The compiler expands enums and their cases to code that only uses Scala's other language features. 
As such, *enums in Scala are convenient syntactic sugar*, but they are not essential to understand Scala's core.

>An enumeration is used to define a type consisting of a set of named values.

```scala 3
enum KafkaCompressionType:
  case none, gzip, snappy, lz4, zstd
```
>The companion object of an enum also defines three utility methods. The `valueOf` method obtains an enum value by its 
> name. The `values` method returns all enum values defined in an enumeration in an Array. The `fromOrdinal method obtains
> an enum value from its ordinal (Int) value.
```scala 3
scala> KafkaCompressionType.valueOf("lz4")
val res: com.fortyseven.common.configuration.refinedTypes.KafkaCompressionType = lz4

scala> KafkaCompressionType.valueOf("lz5")
//java.lang.IllegalArgumentException: enum case not found: lz5
//at com.fortyseven.common.configuration.refinedTypes$KafkaCompressionType$.valueOf(refinedTypes.scala:47)

scala> KafkaCompressionType.values
val res: Array[com.fortyseven.common.configuration.refinedTypes.KafkaCompressionType] = Array(none, gzip, snappy, lz4, zstd)
  
scala> KafkaCompressionType.fromOrdinal(4)
val res: com.fortyseven.common.configuration.refinedTypes.KafkaCompressionType = zstd

scala> KafkaCompressionType.fromOrdinal(5)
//java.util.NoSuchElementException: 5
//at com.fortyseven.common.configuration.refinedTypes$KafkaCompressionType$.fromOrdinal(refinedTypes.scala:47)
```

More information at [Enums](https://docs.scala-lang.org/scala3/reference/enums/index.html).

<h4 id="extension-methods">Extension Methods</h4>

>Extension methods allow one to add methods to a type after the type is defined.

```scala 3
opaque type Latitude = Double
extension (coordinate: Latitude) def value: Double = coordinate
```
```scala 3
scala> Latitude(90)
val res: com.fortyseven.domain.model.types.refinedTypes.Latitude = 90.0

scala> Latitude(90).value
val res: Double = 90.0 
```

More information at [Extension Methods](https://docs.scala-lang.org/scala3/reference/contextual/extension-methods.html).

<h4 id="given-instances">Given Instances</h4>

>Given instances (or, simply, "givens") define "canonical" values of certain types that serve for synthesizing arguments
> to context parameters.
> 
> The name of a given can be left out. If the name of a given is missing, the compiler will synthesize a name from the
> implemented type(s).

In this project, givens are defined to fill the canonical value of a method that has a using clause in its parameters.
For example:
- Codecs:
```scala 3
import vulcan.{AvroError, Codec}

given latitudeCodec: Codec[Latitude] = Codec.double.imapError(Latitude.from(_)
        .leftMap(e => AvroError(s"AvroError: ${e.message}")))(_.value)
```
- ConfigReader:
```scala 3
import pureconfig.ConfigReader
import pureconfig.error.ExceptionThrown

given ConfigReader[KafkaCompressionType] =
  ConfigReader.fromString(KafkaCompressionType.from(_).leftMap(ExceptionThrown.apply))
```

More information at [Given Instances](https://docs.scala-lang.org/scala3/reference/contextual/givens.html).

<h4 id="implicit-conversions">Implicit Conversions</h4>

>Implicit conversions are defined by given instances of the `scala.Conversion` class.

This given Conversion is inside the companion object of the refined type `NonEmptyString`. Compiler will convert the type
`NonEmptyString` to `String` everytime that a method expects to receive a `String` but a  `NonEmptyString` is provided.
```scala 3
given Conversion[NonEmptyString, String] with 
  override def apply(x: NonEmptyString): String = x
```

This snippet of code is from of `kafka-consumer`. Here we can see that a given instance of the type 
`Conversion[KafkaAutoOffsetReset,AutoOffsetReset]` is in scope when the compiler finds a method that requires the second
type (AutoOffsetReset), but it is provided with the first type (KafkaAutoOffsetReset).
```scala 3
import com.fortyseven.common.configuration.refinedTypes.KafkaAutoOffsetReset // Our refined type
import fs2.kafka.AutoOffsetReset // Actual type of Kafka's API

given Conversion[KafkaAutoOffsetReset, AutoOffsetReset] with
  override def apply(x: KafkaAutoOffsetReset): AutoOffsetReset = x match // Match is exhaustive
    case KafkaAutoOffsetReset.Earliest => AutoOffsetReset.Earliest
    case KafkaAutoOffsetReset.Latest   => AutoOffsetReset.Latest
    case KafkaAutoOffsetReset.None     => AutoOffsetReset.None
      
val consumerSettings = ConsumerSettings[F, String, Array[Byte]]
        .withAutoOffsetReset(consumerConfig.autoOffsetReset) // Receives a KafkaAutoOffsetReset but expects AutoOffsetReset. Compiles
        .withBootstrapServers(kc.broker.brokerAddress)
        .withGroupId(consumerConfig.groupId)
```
More information at [Implicit Conversions](https://docs.scala-lang.org/scala3/reference/contextual/conversions.html).

<h4 id="inline">Inline</h4>

> `inline` is a new soft modifier that guarantees that a definition will be inlined at the point of use.

Inlining is used only with `def` (methods) but can be used also with `val`(values). 
```scala 3
inline def apply(coordinate: Double): Latitude =
  requireConst(coordinate)
  inline if coordinate < -90.0 || coordinate > 90.0
  then error("Invalid latitude value. Accepted coordinate values are between -90.0 and 90.0.")
  else coordinate
```
> This method will always be inlined at the point of call. In the inlined code, an if-then-else with a constant condition
> will be rewritten to its then- or else-part.

More information at [Inline](https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html).

<h4 id="new-control-syntax">New Control Syntax</h4>

> Scala 3 has a new "quiet" syntax for control expressions that does not rely on enclosing the condition in parentheses,
> and also allows to drop parentheses or braces around the generators of a `for`-expression.

- Quiet syntax:
```scala 3
def from(intCandidate: Int): Either[Throwable, PositiveInt] =
  if intCandidate < 0
  then Left(new IllegalStateException(s"The provided int $intCandidate is not positive."))
  else Right(intCandidate)
```

More information at [New Control Syntax](https://docs.scala-lang.org/scala3/reference/other-new-features/control-syntax.html).

<h4 id="opaque-type-aliases">Opaque Type Aliases</h4>

> Opaque types aliases provide type abstraction without any overhead.

```scala 3
object ids:
  opaque type BicycleId = UUID
  object BicycleId:
    def apply(id: UUID): BicycleId = id

    extension (bicycleId: BicycleId)
      def value: UUID = bicycleId
```
This introduces `BicycleId` as a new abstract type, which is implemented as `UUID`. The fact that BicycleId is the same 
as UUID is only known in the scope where BicycleId is defined, which in the above example corresponds to the object 
`ids`. Or in other words, within the scope, it is treated as a type alias, but this is opaque to the outside world 
where, in consequence, BicycleId is seen as an abstract type that has nothing to do with UUID.

The public API of BicycleId consists of the `apply` method defined in the companion object. It converts from `UUID` to 
`BicycleId` values. Moreover, an operation `value` that converts the other way is defined as extension method
on `BicycleId` values.

More information at [Opaque Type Aliases](https://docs.scala-lang.org/scala3/reference/other-new-features/opaques.html).

<h4 id="using-clauses">Using Clauses</h4>

>Functional programming tends to express most dependencies as simple function parameterization. This is clean and 
>powerful, but it sometimes leads to functions that take many parameters where the same value is passed over and
>over again in long call chains to many functions. Context parameters can help here since they enable the compiler
>to synthesize repetitive arguments instead of the programmer having to write them explicitly.

Using clauses are present in the module `03-u-data-generator`, where they are used to handle generic serializers.

```scala 3
  def avroSerializer[T](configuration: Configuration, includeKey: Boolean)
        (using codec: Codec[T]): Serializer[T] = 
    new Serializer[T]: // More code here

  import com.fortyseven.domain.codecs.iot.IotCodecs.given // Codec[GPSPosition] is here

  val gpsPositionSerializer = avroSerializer[GPSPosition](
    Configuration(configuration.schemaRegistry.schemaRegistryUrl), 
    includeKey = false
  )
```

More information at [Using Clauses](https://docs.scala-lang.org/scala3/reference/contextual/using-clauses.html).

[Go back to Index](#index)

<h4 id="native-refined-types">Native Refined Types</h4>  //TODO this section is not clear yet

If we were to combine some of the prior new features of Scala 3, we could build natively refined types and their APIs.

The first example combines the following features:
- compile-time operations
- given instances
- implicit conversions
- inline
- new control syntax
- opaque types aliases
```scala 3
opaque type PositiveInt = Int

object PositiveInt:
  
  def from(intCandidate: Int): Either[Throwable, PositiveInt] =
    if intCandidate < 0
    then Left(new IllegalStateException(s"The provided int $intCandidate is not positive."))
    else Right(intCandidate)
    
  inline def apply(int: Int): PositiveInt =
    requireConst(int)
    inline if int < 0 
    then error("Int must be positive.")
    else int
    
  given Conversion[PositiveInt, Int] with
    override def apply(x: PositiveInt): Int = x
```
And with this small amount of code we get all this functionality:
```scala 3
scala> PositiveInt.from(-1) 
val res: Either[Throwable, com.fortyseven.common.configuration.refinedTypes.PositiveInt] = 
  Left(java.lang.IllegalStateException: The provided int -1 is not positive.)

scala> PositiveInt.from(0) 
val res: Either[Throwable, com.fortyseven.common.configuration.refinedTypes.PositiveInt] = Right(0)

scala> PositiveInt(0) // PositiveInt.apply(0) checks during compilation time
val res: com.fortyseven.common.configuration.refinedTypes.PositiveInt = 0

scala> PositiveInt(-1) // PositiveInt.apply(-1) checks during compilation time
        -- Error: ----------------------------------------------------------------------
1 |PositiveInt(-1)
        |^^^^^^^^^^^^^^^
        |Int must be positive.
        
scala> PositiveInt(0)
val res: com.fortyseven.common.configuration.refinedTypes.PositiveInt = 0

scala> PositiveInt(0) + 1 // Implicit conversion
val res: Int = 1

scala> 1 + PositiveInt(0)  // Implicit conversion
val res: Int = 1
```
The second example combines the following features:
- compile-time operations
- enum
- inline
- new control syntax
```scala 3
enum KafkaAutoOffsetReset:
  case Earliest, Latest, None
  
object KafkaAutoOffsetReset:   
  def from(kafkaAutoOffsetResetCandidate: String): Either[Throwable, KafkaAutoOffsetReset] =
      Try(valueOf(kafkaAutoOffsetResetCandidate)).toEither
      
  inline def apply(kafkaAutoOffsetReset: String): KafkaAutoOffsetReset =
    requireConst(kafkaAutoOffsetReset)
    inline if values.map(_.toString).contains(kafkaAutoOffsetReset) // This inline fails in the console. Needs checking
    then valueOf(kafkaAutoOffsetReset)
    else error("The valid values are Earliest, Latest and None.")
```
And this is what we get:
```scala 3
scala> KafkaAutoOffsetReset.values
val res: Array[com.fortyseven.common.configuration.refinedTypes.KafkaAutoOffsetReset] = Array(Earliest, Latest, None)

scala> KafkaAutoOffsetReset.valueOf("None")
val res: com.fortyseven.common.configuration.refinedTypes.KafkaAutoOffsetReset = None

scala> KafkaAutoOffsetReset.from("None")
val res: Either[Throwable, com.fortyseven.common.configuration.refinedTypes.KafkaAutoOffsetReset] = Right(None)
```

<h2 id="frameworks">Frameworks</h2>

There are three main frameworks used in the project:
- Apache Flink
- Apache Kafka
- Apache Spark

<h3 id="apache-flink">Apache Flink</h3>

>Apache Flink is a framework and distributed processing engine for stateful computations over unbounded and bounded
> data streams. Flink has been designed to run in all common cluster environments, perform computations at in-memory 
> speed and at any scale.

More information at [Apache Flink](https://flink.apache.org/).

[Go back to Index](#index)

<h3 id="apache-kafka">Apache Kafka</h3>

>Apache Kafka is an open-source distributed event streaming platform used by thousands of companies for high-performance 
>data pipelines, streaming analytics, data integration, and mission-critical applications.

In this project, Kafka is used as a event bus that connects multiple pieces of the pipeline. One of the goals of the
project is to process the same event by multiple processors (right now Flink and Spark).

More information at [Apache Kafka](https://kafka.apache.org/).

[Go back to Index](#index)

<h3 id="apache-spark">Apache Spark</h3>

>Apache Spark™ is a multi-language engine for executing data engineering, data science, and machine learning on 
>single-node machines or clusters.

In this project, Spark can be executed using sbt (beware of the flag --add-opens) or docker.

More information at [Apache Spark](https://spark.apache.org/).

[Go back to Index](#index)

<h2 id="libraries">Libraries</h2>

<h3 id="apache-avro">Apache Avro</h3>

>Apache Avro™ is the leading serialization format for record data, and first choice for streaming data pipelines. 
>It offers excellent schema evolution. 

In this project, it is implemented with [Vulcan](#vulcan).

More information at [Apache Avro](https://avro.apache.org/).

[Go back to Index](#index)

<h3 id="cats">Cats</h3>

>Cats is a library which provides abstractions for functional programming in the Scala programming language.
>
>Cats strives to provide functional programming abstractions that are core, binary compatible, modular,
>approachable and efficient. A broader goal of Cats is to provide a foundation for an ecosystem of pure, typeful 
>libraries to support functional programming in Scala applications.

Cats is the effect system used in the whole project except for the module `processor-spark`. Spark's internal 
way of working does not benefit from an effect system on top of it.

More information at [Typelevel Cats](https://typelevel.org/cats/).

[Go back to Index](#index)

<h3 id="ciris">Ciris</h3>

>Functional, lightweight, and composable configuration loading for Scala.

It is used in the module `ciris`.

More information at [Ciris](https://cir.is/).

[Go back to Index](#index)

<h3 id="fs2">FS2</h3>

>Functional, effectful, concurrent streams for Scala.

It is used in the module `data-generator`, `kafka-consumer` and `processor-flink`.

More information at [FS2](https://fs2.io/#/).

[Go back to Index](#index)

<h3 id="logback">Logback</h3>

>Logback is intended as a successor to the popular log4j project, picking up where log4j 1.x leaves off.
>
>Logback's architecture is quite generic so as to apply under different circumstances. At present time, logback is 
>divided into three modules, logback-core, logback-classic and logback-access. This project only uses logback-classic.
> 
>The logback-core module lays the groundwork for the other two modules. The logback-classic module can be assimilated to
>a significantly improved version of log4j 1.x. Moreover, logback-classic natively implements the SLF4J API so that you
>can readily switch back and forth between logback and other logging frameworks such as log4j 1.x or java.util.logging
>(JUL).

It is used in the module `main` and logs the underlying activity of the modules involved in the execution of the program.

More information at [QOS Logback](https://logback.qos.ch/).

[Go back to Index](#index)

<h3 id="munit">Munit</h3>

>Scala testing library with actionable errors and extensible APIs.

This project uses two modules of munit that are compatible with `cats` and with `scala-check`.
- Cats: https://github.com/typelevel/munit-cats-effect
- Scala Check: https://scalameta.org/munit/docs/integrations/scalacheck.html

More information at [Scalameta Munit](https://scalameta.org/munit/).

[Go back to Index](#index)

<h3 id="pureconfig">Pureconfig</h3>

>PureConfig is a Scala library for loading configuration files. It reads Typesafe Config configurations written in HOCON,
>Java .properties, or JSON to native Scala classes in a boilerplate-free way. Sealed traits, case classes, collections,
>optional values, and many other types are all supported out-of-the-box. Users also have many ways to add support for
>custom types or customize existing ones.

In this project, both automatic derivation and custom types are used. Fin the implementations in module `pureconfig`.

More information at [PureConfig](https://pureconfig.github.io/).

[Go back to Index](#index)

<h3 id="test-containers">Test Containers</h3>

>Scala wrapper for testcontainers-java that allows using docker containers for functional/integration/unit testing.
>> TestContainers is a Java 8 library that supports JUnit tests, providing lightweight, throwaway instances of common 
>> databases, Selenium web browsers, or anything else that can run in a Docker container.

In this project it is used for the integration testing of `processor-flink`.

More information at [Testcontainers-scala](https://github.com/testcontainers/testcontainers-scala).

[Go back to Index](#index)

<h3 id="vulcan">Vulcan</h3>

>Functional Avro encodings for Scala using the official Apache Avro library.

It is used to encode and decode the events between the `data-generator`, the `kafka-consumer` and `processor-flink`.

More information at [FD4S Vulcan](https://fd4s.github.io/vulcan/).

[Go back to Index](#index)

<h2 id="tooling">Tooling</h2>

<h3 id="assembly">Assembly</h3>

>Sbt plugin originally ported from codahale's assembly-sbt, which I'm guessing was inspired by Maven's assembly plugin.
>The goal is simple: Create a über JAR of your project with all of its dependencies.

More information at [sbt-assembly](https://github.com/sbt/sbt-assembly). 

[Go back to Index](#index)

<h3 id="explicit-dependencies">Explicit Dependencies</h3>

>Sbt plugin to check that your libraryDependencies accurately reflects the libraries that your code depends on in 
>order to compile.

More information at [sbt-explicit-dependencies](https://github.com/cb372/sbt-explicit-dependencies).

[Go back to Index](#index)

<h3 id="scalafix">ScalaFix</h3>

>Refactoring and linting tool for Scala.

More information at [Scala Center](https://scalacenter.github.io/scalafix/docs/users/installation.html).

[Go back to Index](#index)

<h3 id="scalafmt">ScalaFmt</h3>

>Code formatter for Scala.

More information at [Scalameta Scalafmt](https://scalameta.org/scalafmt/).

[Go back to Index](#index)

<h2 id="license">License</h2>

`Apache-2.0`, see [LICENSE](LICENSE.md)
