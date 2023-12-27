import sbt.*

object Dependencies {

  object Libraries {

    object cats {
      private val coreVersion: String = "2.10.0"
      private val effectVersion: String = "3.5.2"
      val core: ModuleID = "org.typelevel" %% "cats-core" % coreVersion
      val effectKernel: ModuleID = "org.typelevel" %% "cats-effect-kernel" % effectVersion
      val effect: ModuleID = "org.typelevel" %% "cats-effect" % effectVersion
      val free: ModuleID = "org.typelevel" %% "cats-free" % coreVersion
    }

    object kafka {
      private val clientVersion: String = "7.5.0"
      val kafkaClients: ModuleID = "org.apache.kafka" % "kafka-clients" % s"$clientVersion-ccs"
      val kafkaSerializer: ModuleID = "io.confluent" % "kafka-avro-serializer" % clientVersion
      val kafkaSchemaRegistry: ModuleID = "io.confluent" % "kafka-schema-registry-client" % clientVersion
      val kafkaSchemaSerializer: ModuleID = "io.confluent" % "kafka-schema-serializer" % clientVersion
    }

    object fs2 {
      private val version: String = "3.9.2"
      private val kafkaVersion: String = "3.1.0"
      val core: ModuleID = "co.fs2" %% "fs2-core" % version
      val kafka: ModuleID = "com.github.fd4s" %% "fs2-kafka" % kafkaVersion
      val kafkaVulcan: ModuleID = "com.github.fd4s" %% "fs2-kafka-vulcan" % kafkaVersion
    }

    object avro {
      val avro: ModuleID = "org.apache.avro" % "avro" % "1.11.3"
      val vulcan: ModuleID = "com.github.fd4s" %% "vulcan" % "1.9.0"

    }

    object config {
      private val pureConfigVersion: String = "0.17.4"
      val ciris: ModuleID = "is.cir" %% "ciris" % "3.2.0"
      val pureConfig: ModuleID = "com.github.pureconfig" %% "pureconfig-core" % pureConfigVersion
      val pureConfigCE: ModuleID = "com.github.pureconfig" %% "pureconfig-cats-effect" % pureConfigVersion
    }

    object flink {
      private val version: String = "1.17.1"
      val avro: ModuleID = "org.apache.flink" % "flink-avro" % version
      val avroConfluent: ModuleID = "org.apache.flink" % "flink-avro-confluent-registry" % version
      val core: ModuleID = "org.apache.flink" % "flink-core" % version
      val clients: ModuleID = "org.apache.flink" % "flink-clients" % version
      val kafka: ModuleID = "org.apache.flink" % "flink-connector-kafka" % "3.0.0-1.17"
      val streaming: ModuleID = "org.apache.flink" % "flink-streaming-java" % version
    }

    object spark {
      private val version: String = "3.5.0"
      val catalyst: ModuleID = "org.apache.spark" %% "spark-catalyst" % version % "provided"
      val core: ModuleID = "org.apache.spark" %% "spark-core" % version % "provided"
      val sql: ModuleID = "org.apache.spark" %% "spark-sql" % version % "provided"
      val streaming: ModuleID = "org.apache.spark" %% "spark-streaming" % version % "provided"
      val `sql-kafka`: ModuleID = "org.apache.spark" %% "spark-sql-kafka-0-10" % version % "provided"
    }

    object testContainers {
      private val version: String = "0.40.14" // Dependency conflict on 0.40.15
      val kafka: ModuleID = "com.dimafeng" %% "testcontainers-scala-kafka" % version % Test
      val munit: ModuleID = "com.dimafeng" %% "testcontainers-scala-munit" % version % Test
    }

    object logging {
      private val catsVersion: String = "2.6.0"
      private val logbackVersion: String = "1.4.11"
      val catsCore: ModuleID = "org.typelevel" %% "log4cats-core" % catsVersion
      val catsSlf4j: ModuleID = "org.typelevel" %% "log4cats-slf4j" % catsVersion
      val logback: ModuleID = "ch.qos.logback" % "logback-classic" % logbackVersion // % Runtime
    }

    object test {
      private val munitScalacheckVersion: String = "1.0.4"
      private val munitCatsEffectVersion: String = "1.0.7"
      val munitCatsEffect: ModuleID = "org.typelevel" %% "munit-cats-effect-3" % munitCatsEffectVersion % Test
      val munitScalacheck: ModuleID = "org.typelevel" %% "scalacheck-effect-munit" % munitScalacheckVersion % Test
    }

    object iron {
      val version: String = "2.3.0"
      val core: ModuleID = "io.github.iltotore" %% "iron" % version
      val cats: ModuleID = "io.github.iltotore" %% "iron-cats" % version
      val circe: ModuleID = "io.github.iltotore" %% "iron-circe" % version
      val ciris: ModuleID = "io.github.iltotore" %% "iron-ciris" % version
      val scalacheck: ModuleID = "io.github.iltotore" %% "iron-scalacheck" % version % Test
    }

  }

}
