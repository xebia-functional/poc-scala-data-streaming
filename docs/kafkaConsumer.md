# Kafka Consumer

This module channels the information coming from up stream sources into a kafka topic that later can be read by one or 
multiple data processors (for now only Flink and Spark).

## Configuration

There are two modules for reading configuration: `ciris` and `pureconfig`.
The configurations are located here:
- ciris: `02-c-config-ciris/src/main/scala/com/fortyseven/cirisconfiguration/KafkaConsumerConfigurationLoader.scala`
- pureconfig: `02-c-config-pureconfig/src/main/resources/application.conf`

If you change the values on `ciris` you will have to recompile the project:
```shell
cd ..
sbt "configuration-ciris/clean; configuration-ciris/compile"
```

In the case of pureconfig, you do not need to recompile.

## Running

The kafka consumer does not run on its own. It has to be run in combination with the other elements of the `main` method
in the module `03-c-main`. There are a couple of main classes, each for a specific combination of libraries or frameworks.

The kafka consumer need a docker environment available in the scope of execution and the services like kafka, zookeeper 
and schema registry have to be running. Todo do that, execute:
```shell
cd ../02-i-consumer-kafka/
docker-compose up -d
```