import sbt.*

object Dependencies {

  object SbtPlugins {
    private val version: String = "0.6.0"
    val organizeImports: ModuleID = "com.github.liancheng" %% "organize-imports" % version
  }

  object Libraries {

    object cats {
      private val coreVersion: String = "2.9.0"
      private val effectVersion: String = "3.5.0"
      val core: ModuleID = "org.typelevel" %% "cats-core" % coreVersion
      val effectKernel: ModuleID = "org.typelevel" %% "cats-effect-kernel" % effectVersion
      val effect: ModuleID = "org.typelevel" %% "cats-effect" % effectVersion
      val free: ModuleID = "org.typelevel" %% "cats-free" % coreVersion
    }

    object kafka {
      private val clientVersion: String = "7.4.0"
      val kafkaClients: ModuleID = "org.apache.kafka" % "kafka-clients" % s"$clientVersion-ccs"
      val kafkaSerializer: ModuleID = "io.confluent" % "kafka-avro-serializer" % clientVersion
      val kafkaSchemaRegistry: ModuleID = "io.confluent" % "kafka-schema-registry-client" % clientVersion
      val kafkaSchemaSerializer: ModuleID = "io.confluent" % "kafka-schema-serializer" % clientVersion
    }

    object fs2 {
      private val version: String = "3.7.0"
      private val kafkaVersion: String = "3.0.1"
      val core: ModuleID = "co.fs2" %% "fs2-core" % version
      val kafka: ModuleID = "com.github.fd4s" %% "fs2-kafka" % kafkaVersion
      val kafkaVulcan: ModuleID = "com.github.fd4s" %% "fs2-kafka-vulcan" % kafkaVersion
    }

    object avro {
      val avro: ModuleID = "org.apache.avro" % "avro" % "1.11.1"
      val vulcan: ModuleID = "com.github.fd4s" %% "vulcan" % "1.9.0"

    }

    object config {
      private val cirisVersion: String = "3.1.0"
      val ciris: ModuleID = "is.cir" %% "ciris" % cirisVersion
      val cirisRefined: ModuleID = "is.cir" %% "ciris-refined" % cirisVersion
      val cirisHocon: ModuleID = "lt.dvim.ciris-hocon" %% "ciris-hocon" % "1.1.0"
    }

    object refined {
      private val refinedVersion: String = "0.10.3"
      val refined: ModuleID = "eu.timepit" %% "refined" % refinedVersion
    }

    object shapeless {
      private val shapelessVersion: String = "2.3.10"
      val shapeless: ModuleID = "com.chuusai" % "shapeless_2.13" % shapelessVersion
    }

    object flink {
      private val version: String = "1.17.0"
      val core: ModuleID = "org.apache.flink" % "flink-core" % version
      val clients: ModuleID = "org.apache.flink" % "flink-clients" % version
      val kafka: ModuleID = "org.apache.flink" % "flink-connector-kafka" % "3.0.0-1.17"
      val streaming: ModuleID = "org.apache.flink" % "flink-streaming-java" % version
    }

    object testContainers {
      private val version: String = "0.40.14" // Dependency conflict on 0.40.15
      val kafka: ModuleID = "com.dimafeng" %% "testcontainers-scala-kafka" % version % Test
      val munit: ModuleID = "com.dimafeng" %% "testcontainers-scala-munit" % version % Test
    }

    object logging {
      private val catsVersion: String = "2.6.0"
      private val logbackVersion: String = "1.4.7"
      val catsCore: ModuleID = "org.typelevel" %% "log4cats-core" % catsVersion
      val catsSlf4j: ModuleID = "org.typelevel" %% "log4cats-slf4j" % catsVersion
      val logback: ModuleID = "ch.qos.logback" % "logback-classic" % logbackVersion
    }

    object test {
      private val munitScalacheckVersion: String = "2.0-9366e44"
      private val munitCatsEffectVersion: String = "1.0.7"
      val munitCatsEffect: ModuleID = "org.typelevel" %% "munit-cats-effect-3" % munitCatsEffectVersion % Test
      val munitScalacheck: ModuleID = "org.typelevel" %% "scalacheck-effect-munit" % munitScalacheckVersion % Test
    }
  }
}