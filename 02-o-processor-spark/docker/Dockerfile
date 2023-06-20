# This Dockerfile is taken from https://github.com/mvillarrealb/docker-spark-cluster/blob/master/docker-compose.yml
# and updated to install the Scala 2.13 version of Spark 3.2.0

FROM openjdk:11.0.13-jre-slim-buster as builder

RUN apt-get update && apt-get install -y curl vim wget software-properties-common ssh net-tools ca-certificates

ENV SPARK_VERSION=3.4.0 \
SCALA_VERSION=2.13 \
HADOOP_VERSION=3 \
SPARK_HOME=/opt/spark

RUN wget --no-verbose -O apache-spark.tgz "https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}-scala${SCALA_VERSION}.tgz" \
&& mkdir -p /opt/spark \
&& tar -xf apache-spark.tgz -C /opt/spark --strip-components=1 \
&& rm apache-spark.tgz


FROM builder as apache-spark

ARG SCALA_VERSION="3.3.0"
ARG PROJECT_VERSION="0.1.0-SNAPSHOT"


WORKDIR /opt/spark

ENV SPARK_MASTER_PORT=7077 \
SPARK_MASTER_WEBUI_PORT=8080 \
SPARK_LOG_DIR=/opt/spark/logs \
SPARK_MASTER_LOG=/opt/spark/logs/spark-master.out \
SPARK_WORKER_LOG=/opt/spark/logs/spark-worker.out \
SPARK_WORKER_WEBUI_PORT=8080 \
SPARK_WORKER_PORT=7000

EXPOSE 8080 7077 7000

RUN mkdir -p $SPARK_LOG_DIR && \
touch $SPARK_MASTER_LOG && \
touch $SPARK_WORKER_LOG && \
ln -sf /dev/stdout $SPARK_MASTER_LOG && \
ln -sf /dev/stdout $SPARK_WORKER_LOG

COPY start-spark.sh /

CMD ["/bin/bash", "/start-spark.sh"]
