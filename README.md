# Data streaming POC

Building an end-to-end data streaming pipeline, with data generator, consumer and processor modules, using well established open-source libraries:
* [Apache Kafka](https://kafka.apache.org/)
* [Apache Kafka - Kafka Streams](https://kafka.apache.org/documentation/streams/)
* [Apache Flink](https://flink.apache.org/)
* [Apache Spark - Spark Streaming](https://spark.apache.org/docs/latest/streaming-programming-guide.html)
* [Apache Storm](https://storm.apache.org/)

This project applies [Diamond Architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)) design patterns and follows a [Kappa Architecture](https://www.newsletter.swirlai.com/p/sai-13-lambda-vs-kappa-architecture) data processing strategy.

![Kappa Architecture](https://substack-post-media.s3.amazonaws.com/public/images/d544524c-15ec-4bb1-b2ce-d28f390f0dd7_4793x5911.png)

For more information, see [specification](https://docs.google.com/document/d/1f6vxfJrBA8dylbEGMHsJIcjHEztaYu5BHjoTB-XHvw8).

### Pre-requisites

* [JDK](https://openjdk.org/projects/jdk/20/)
* [SBT](https://www.scala-sbt.org/download.html)
* [Docker Compose](https://docs.docker.com/compose/install/linux/)
* [Kafkacat](https://formulae.brew.sh/formula/kcat)

### How-to

#### Sanity checks

* Compile -> `sbt compile`
* Format -> `sbt styleFix`
* Test -> `sbt test`
* Code Coverage -> `sbt runCoverage`
* Run Flink integration tests -> `sbt flinkIT`

#### Integration checks

* Start docker images -> `docker-compose up -d`
* Inspect Kafka topics -> `kcat -b localhost:9092 -L`

### License

`Apache-2.0`, see [LICENSE](LICENSE.md)
