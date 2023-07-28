# How to Run Apache Flink

In this project you can run Flink locally using the sbt server, Docker or Kubernetes. In all the cases, it is needed for
the code to be compiled first:

```shell
cd .. && sbt compile
```

## SBT Server

First, we will _compose-up_ the kafka consumer, that will make the data available to Apache Flink.

```shell
docker-compose -f ../docker/docker-compose-kafka.yml up -d
```

Once the code is compiled and the kafka consumer is up, you can run it in module `03-c-main`:
```bash
cd .. && sbt "main / run;"
```
The way the Flink works, will run also the data-generator, will send data to the kafka-consumer running on Docker.
Then, the Flink Processor will read from the configured topic.

You can also lunch it from the flink module `02-o-processor-flink`, it will ask you which main to run, select the 
number 2:

```shell
cd .. && sbt "processor-flink / run;"
```

### Integration Tests

**NOTE**: There will be a warning because of a deprecated notation in 
[DataProcessorIntegrationTest](./src/main/scala/com/fortyseven/processor/flink/DataProcessorIntegrationTest.scala).
Make sure to comment it out before running the tests.

To run the integration tests, you must have a docker environment available in your machine before executing the test.
You do *NOT* need to run `docker compose up`. You just need an available docker environment. The integration test runs 
using [Test Containers for Scala](https://github.com/testcontainers/testcontainers-scala).
Once you have made sure the environment is available, you can run: 

```shell
cd .. && sbt flinkIT
```

## Docker

Right now, Flink is not yet implemented with Docker Images. _TBD_

[General documentation](https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/docker/)

## Kubernetes

Right now, Flink is not yet implemented with Kubernetes. _TBD_

[General documentation](https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/kubernetes/)

