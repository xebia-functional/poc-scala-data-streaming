import Dependencies._
import CustomSbt._

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / organization := "com.47deg"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/47deg/poc-scala-data-streaming"),
    "scm:git@github.com:47deg/poc-scala-data-streaming.git"
  )
)

ThisBuild / scalaVersion := "3.3.0"

ThisBuild / semanticdbEnabled := true

ThisBuild / scalacOptions ++=
  Seq(
    "-deprecation",
    "-explain",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Ykind-projector",
    "-Wunused:all",
    "-Wvalue-discard"
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future-migration")

ThisBuild / assemblyMergeStrategy := {
  case PathList(ps @ _*) if ps.lastOption.contains("module-info.class") => MergeStrategy.discard
  case x if x.endsWith(".properties")                                   => MergeStrategy.filterDistinctLines
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

lazy val `poc-scala-data-streaming`: Project =
  project
    .in(file("."))
    .aggregate(
      // Layer 1 - Domain
      `domain`,
      // Layer 2 - APIs
      // APIs
      `common-api`,
      `input-api`,
      `output-api`,
      // Layer 3 - Business Logic, Common and Utils
      // Business Logic
      `business-logic`,
      // Common
      `configuration-ciris`,
      `configuration-pureconfig`,
      // Utils
      `data-generator`,
      // Layer 4 - Implementations
      // Input
      `consumer-kafka`,
      // Output
      `processor-flink`,
      `processor-spark`,
      // Layer 5 - Program
      main
    )

// Layer 1 - Domain

lazy val domain: Project =
  project
    .in(file("01-c-domain"))
    .settings(commonSettings)
    .settings(
      name := "domain",
      libraryDependencies ++= Seq(
        Libraries.avro.vulcan,
        Libraries.avro.avro,
        Libraries.cats.free,
        Libraries.cats.core,
        Libraries.test.munitScalacheck
      )
    )

// Layer 2 - APIs, Common and Utils

// APIs

lazy val `common-api`: Project =
  project
    .in(file("02-c-api"))
    .dependsOn(domain % Cctt)
    .settings(commonSettings)
    .settings(
      name := "common-api",
      libraryDependencies ++= Seq()
    )

lazy val `input-api`: Project =
  project
    .in(file("02-i-api"))
    .dependsOn(`common-api` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "input-api",
      libraryDependencies ++= Seq()
    )

lazy val `output-api`: Project =
  project
    .in(file("02-o-api"))
    .dependsOn(`common-api` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "output-api",
      libraryDependencies ++= Seq()
    )

// Layer 3 - Business Logic, Common and Utils

// Business Logic

lazy val `business-logic`: Project =
  project
    .in(file("03-c-business-logic"))
    .dependsOn(domain % Cctt)
    .settings(commonSettings)
    .settings(
      name := "business-logic",
      libraryDependencies ++= Seq(
      )
    )

// Common

lazy val `configuration-ciris`: Project =
  project
    .in(file("03-c-config-ciris"))
    .dependsOn(`common-api` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "ciris",
      libraryDependencies ++= Seq(
        Libraries.config.ciris,
        Libraries.cats.effectKernel
      )
    )

lazy val `configuration-pureconfig`: Project =
  project
    .in(file("03-c-config-pureconfig"))
    .dependsOn(`common-api` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "pureconfig",
      libraryDependencies ++= Seq(
        Libraries.config.pureConfig,
        Libraries.cats.core,
        Libraries.test.munitCatsEffect
      )
    )

// Utils

lazy val `data-generator`: Project =
  project
    .in(file("03-u-data-generator")) 
    .dependsOn(`configuration-pureconfig` % Cctt)
    .enablePlugins(DockerPlugin)
    .enablePlugins(JavaAppPackaging)
    .settings(commonSettings)
    .settings(
      name                 := "data-generator",
      assembly / mainClass := Some("com.fortyseven.datagenerator.Main"),
      Docker / packageName := "data-generator",
      dockerBaseImage      := "openjdk:11-jre-slim-buster",
      dockerExposedPorts ++= Seq(8080),
      dockerUpdateLatest := true,
      dockerAlias := DockerAlias(
        registryHost = Some("ghcr.io"),
        username = Some((ThisBuild / organization).value),
        name = (Docker / packageName).value,
        tag = Some("latest")
      ),
      libraryDependencies ++= Seq(
        Libraries.fs2.core,
        Libraries.fs2.kafka,
        Libraries.kafka.kafkaClients,
        Libraries.cats.core,
        Libraries.cats.effect,
        Libraries.cats.effectKernel,
        Libraries.avro.vulcan,
        Libraries.avro.avro,
        Libraries.kafka.kafkaSchemaRegistry,
        Libraries.kafka.kafkaSchemaSerializer,
        Libraries.kafka.kafkaSerializer,
        Libraries.test.munitCatsEffect,
        Libraries.test.munitScalacheck,
        Libraries.config.pureConfig,
        Libraries.config.pureConfigCE
      )
    )

// Layer 4 - Implementations

// Input

lazy val `consumer-kafka`: Project =
  project
    .in(file("04-i-consumer-kafka"))
    .dependsOn(`configuration-pureconfig` % Cctt)
    .dependsOn(`input-api` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "kafka-consumer",
      libraryDependencies ++= Seq(
        Libraries.cats.core,
        Libraries.cats.effectKernel,
        Libraries.kafka.kafkaClients,
        Libraries.fs2.kafka,
        Libraries.fs2.core,
        Libraries.config.pureConfig,
        Libraries.config.pureConfigCE
      )
    )

// Output

lazy val `processor-flink`: Project =
  project
    .in(file("04-o-processor-flink"))
    .dependsOn(`configuration-pureconfig` % Cctt)
    .dependsOn(`output-api` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "flink",
      libraryDependencies ++= Seq(
        Libraries.avro.avro,
        Libraries.avro.vulcan,
        Libraries.cats.core,
        Libraries.cats.effect,
        Libraries.cats.effectKernel,
        Libraries.flink.avro,
        Libraries.flink.avroConfluent,
        Libraries.flink.clients,
        Libraries.flink.core,
        Libraries.flink.kafka,
        Libraries.flink.streaming,
        Libraries.fs2.kafkaVulcan,
        Libraries.kafka.kafkaClients,
        Libraries.logging.catsCore,
        Libraries.logging.catsSlf4j,
        Libraries.logging.logback,
        Libraries.config.pureConfig,
        Libraries.config.pureConfigCE
      )
    )

lazy val `processor-flink-integration`: Project =
  project
    .in(file("04-o-processor-flink/integration"))
    .dependsOn(`processor-flink`)
    .settings(commonSettings)
    .settings(
      name           := "integration-test",
      publish / skip := true,
      libraryDependencies ++= Seq(
        Libraries.testContainers.kafka,
        Libraries.testContainers.munit,
        Libraries.logging.catsCore,
        Libraries.logging.catsSlf4j,
        Libraries.logging.logback,
        Libraries.test.munitCatsEffect
      ),
      javacOptions ++= Seq("-source", "11", "-target", "11")
    )

lazy val `processor-spark`: Project = project
  .in(file("04-o-processor-spark"))
  .dependsOn(`output-api` % Cctt)
  .dependsOn(`configuration-pureconfig` % Cctt)
  .settings(commonSettings)
  .settings(
    name := "spark",
    libraryDependencies ++= Seq(
      Libraries.spark.catalyst,
      Libraries.spark.core,
      Libraries.spark.sql,
      Libraries.spark.streaming,
      Libraries.spark.`sql-kafka`
    ).map(_.cross(CrossVersion.for3Use2_13)),
    Compile / run := Defaults
      .runTask(
        Compile / fullClasspath,
        Compile / run / mainClass,
        Compile / run / runner
      ).evaluated,
    javacOptions ++= Seq("-source", "17", "-target", "17"),
    assembly / assemblyJarName := "spark-app.jar"
  )

// Layer 5 - Program

lazy val main: Project =
  project
    .in(file("05-c-main"))
    .dependsOn(`consumer-kafka` % Cctt)
    .dependsOn(`data-generator` % Cctt)
    .dependsOn(`processor-flink` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "app",
      libraryDependencies ++= Seq(
        Libraries.cats.core,
        Libraries.cats.effect,
        Libraries.cats.effectKernel,
        Libraries.logging.catsCore,
        Libraries.logging.catsSlf4j,
        Libraries.logging.logback
      )
    )
    .settings(
      assembly / assemblyMergeStrategy := {
        case PathList(ps @ _*) if ps.lastOption.contains("module-info.class") => MergeStrategy.discard
        case x if x.endsWith(".conf")                                         => MergeStrategy.filterDistinctLines
        case x if x.endsWith(".xml")                                          => MergeStrategy.filterDistinctLines
        case x =>
          val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
          oldStrategy(x)
      }
    )

lazy val commonSettings = commonScalacOptions ++ Seq(
  resolvers += "confluent" at "https://packages.confluent.io/maven/",
  update / evictionWarningOptions := EvictionWarningOptions.empty,
  assemblyMergeStrategy := {
    case PathList("META-INF") => MergeStrategy.discard
    case x                    => MergeStrategy.first
  }
)

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings"
  ),
  Test / console / scalacOptions := (Compile / console / scalacOptions).value
)
