# How to Run Apache Flink

In this project you can run Flink locally using IntelliJ Idea, Docker or Kubernetes. Both cases need the code to be
compiled first:

```shell
cd ..
sbt compile
```

And to have a docker environment available in the machine.

## IntelliJ Idea

First, we will _compose-up_ the kafka consumer, that will make the data available to Apache Flink.

```shell
cd ../02-i-consumer-kafka
docker compose up -d
```

Once the code is compiled and the kafka consumer is up, you can run the `FlinkMain` in module `03-c-main`.

### Integration Tests

**NOTE**: There will be a warning because of a deprecated notation in 
[DataProcessorIntegrationTest](./src/main/scala/com/fortyseven/processor/flink/DataProcessorIntegrationTest.scala).
Make sure to comment it out before running the tests.

To run the integration tests, you must have a docker environment available in your machine before executing the test.
You do NOT need to run `docker compose up`. You just need the environment. The integration test run using 
[Test Containers for Scala](https://github.com/testcontainers/testcontainers-scala).
Once you have made sure the environment is available, you can run: 

```shell
sbt flinkIT
```

## Docker

_TBD_

[General documentation](https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/docker/)

## Kubernetes

_TBD_

[General documentation](https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/kubernetes/)

