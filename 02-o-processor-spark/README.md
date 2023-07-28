# How to Run Apache Spark

In this project you can run Spark locally using IntelliJ Idea or Docker. Both cases need the code to be compiled first:

```shell
cd ..
sbt compile
```

And to have a docker environment available in the machine.

## IntelliJ Idea

First, we will _compose-up_ the kafka consumer, that will make the data available to Apache Spark.

```shell
cd ../02-i-consumer-kafka
docker compose up -d
```

Once the code is compiled and the kafka consumer is up, you can run the `SparkMain` in module `03-c-main`. In order to 
run Spark directly in InteliJ, you have to add the following 
[VM options](https://www.360learntocode.com/2022/01/how-to-set-jvm-arguments-in-intellij.html)
to the run configuration:

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
### Integration Tests

_TBD_

## Docker

Information will be added once the open PR with the docker changes for Spark will be merged.

## Kubernetes

_TBD_

[General documentation](https://spark.apache.org/docs/latest/running-on-kubernetes.html)

