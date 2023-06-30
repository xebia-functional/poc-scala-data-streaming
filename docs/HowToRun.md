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