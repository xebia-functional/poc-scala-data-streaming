package com.fortyseven.processor.flink

import cats.effect.*
import cats.implicits.*
import fs2.kafka.*
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.serialization.StringDeserializer

object KafkaUtils:

  def createTopics[F[_]: Async](bootstrapServers: String, topics: String*): F[Unit] =
    KafkaAdminClient
      .resource[F](AdminClientSettings(bootstrapServers))
      .use(client => topics.traverse(t => client.createTopic(new NewTopic(t, 1, 1.toShort))))
      .void

  def consumerStream[F[_]: Async](bootstrapServers: String, topic: String): fs2.Stream[F, String] =
    val settings = ConsumerSettings(
      keyDeserializer = Deserializer[F, String],
      valueDeserializer = Deserializer[F, String]
    ).withBootstrapServers(bootstrapServers)
      .withGroupId("test-consumer-group-" + topic)
      .withAutoOffsetReset(AutoOffsetReset.Earliest)

    KafkaConsumer.stream(settings).subscribeTo(topic).records.map(_.record.value)

  def produce[F[_]: Async](
      bootstrapServers: String,
      topic: String,
      key: String,
      message: String): F[ProducerResult[String, String]] =
    val settings = ProducerSettings(
      keySerializer = Serializer[F, String],
      valueSerializer = Serializer[F, String]
    ).withBootstrapServers(bootstrapServers)

    KafkaProducer.resource(settings).use { producer =>
      val record = ProducerRecord(
        topic,
        key,
        message
      )
      producer.produce(ProducerRecords.one(record)).flatten
    }
