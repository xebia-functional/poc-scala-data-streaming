# Data Generator

This module generates dummy data using the library `fs2.kafka` based on the application's data model held in the modules
`01-c-core` and `02-c-core`. It sends the generated information to a given kafka topic.

## Configuration

The topic is provided via configuration. There are two modules for reading configuration: `ciris` and `pureconfig`.
The configurations are located here:
- ciris: `02-c-config-ciris/src/main/scala/com/fortyseven/cirisconfiguration/DataGeneratorConfigurationLoader.scala`
- pureconfig: `02-c-config-pureconfig/src/main/resources/application.conf`

If you change the values on `ciris` you will have to recompile the project:
```shell
cd ..
sbt "configuration-ciris/clean; configuration-ciris/compile"
```

In the case of pureconfig, you do not need to recompile.

## Running

The generator does not run on its own. Ity has been developed for testing proposes. It replaces a real source of data.
It runs in combination with the other elements of the `main` method in the module `03-c-main`. There are a couple of
main classes, each for a specific combination of libraries or frameworks.

Yet, in order to run it, you need a Kafka service running on Docker so the data is sent somewhere.
