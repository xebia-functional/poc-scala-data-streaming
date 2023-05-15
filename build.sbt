import Dependencies.*
import CustomSbt.*

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / organization := "com.47deg"

ThisBuild / scmInfo      := Some(
  ScmInfo(
    url("https://github.com/47deg/poc-scala-data-streaming"),
    "scm:git@github.com:47deg/poc-scala-data-streaming.git"
  )
)

ThisBuild / scalaVersion := Versions.scala

ThisBuild / semanticdbEnabled := true

ThisBuild / scalafixDependencies += SbtPlugins.organizeImports

ThisBuild / scalacOptions ++=
  Seq(
    "-deprecation",
    "-explain",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Ykind-projector"
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future-migration")

lazy val `poc-scala-data-streaming`: Project =
  project
    .in(file("."))
    .aggregate(
      // Layer 1
      `core-headers`,
      // Layer 2
      // Common and Utils
      configuration,
      core,
      `data-generator`,
      // Input
      `consumer-kafka`,
      // Output
      `processor-flink`,
      // Layer 3
      main
    )

// Layer 1

lazy val `core-headers`: Project =
  project
    .in(file("01-c-core"))
    .settings(commonSettings)
    .settings(
      name := "core-headers",
      libraryDependencies ++= Seq()
    )

// Layer 2

// Common and Utils
lazy val configuration: Project = (project in file("02-c-config"))
  .dependsOn(`core-headers`)
  .settings(commonSettings)
  .settings(
    name := "configuration",
    libraryDependencies ++= Seq(
      Libraries.config.cirisRefined
    )
  )

lazy val core: Project =
  project
    .in(file("02-c-core"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "core",
      libraryDependencies ++= Seq(
        Libraries.kafka.fs2KafkaVulcan,
        Libraries.test.munitScalacheck,
        Libraries.test.scalatest
      )
    )

// Input
lazy val `data-generator`: Project = (project in file("02-i-data-generator"))
  .dependsOn(`core-headers` % Cctt)
  .dependsOn(core % Cctt) // This should be avoided
  .settings(commonSettings)
  .settings(
    name := "data-generator",
    libraryDependencies ++= Seq(
      Libraries.test.munitCatsEffect,
      Libraries.logging.log4catsSlf4j % Test
    )
  )

lazy val `consumer-kafka`: Project =
  project
    .in(file("02-i-consumer-kafka"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "kafka-consumer",
      libraryDependencies ++= Seq(
        Libraries.kafka.fs2KafkaVulcan
      )
    )

// Output
lazy val `processor-flink`: Project =
  project
    .in(file("02-o-processor-flink"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "flink-processor",
      libraryDependencies ++= Seq(
        Libraries.cats.catsEffect,
        Libraries.flink.clients,
        Libraries.flink.kafka,
        Libraries.kafka.fs2KafkaVulcan
      )
    )

lazy val `processor-flink-integration`: Project =
  project.in(file("02-o-processor-flink/integration"))
    .dependsOn(`processor-flink`)
    .settings(commonSettings)
    .settings(
      name := "flink-integration-test",
      publish / skip := true,
      libraryDependencies ++= Seq(
        Libraries.integrationTest.kafka,
        Libraries.integrationTest.munit,
        Libraries.test.munitCatsEffect,
        Libraries.logging.log4catsSlf4j % Test,
        Libraries.cats.catsEffect % Test
      )
    )

// Layer 3
lazy val main: Project =
  project
    .in(file("03-c-main"))
    .dependsOn(configuration % Cctt)
    .dependsOn(core % Cctt)
    .dependsOn(`consumer-kafka` % Cctt)
    .dependsOn(`data-generator` % Cctt)
    .dependsOn(`processor-flink` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "main",
      libraryDependencies ++= Seq(
        Libraries.logging.logback,
        Libraries.logging.log4catsSlf4j
      )
    )

lazy val commonSettings = commonScalacOptions ++ Seq(
  resolvers += "confluent" at "https://packages.confluent.io/maven/",
  update / evictionWarningOptions := EvictionWarningOptions.empty
)

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings"
  ),
  Test / console / scalacOptions := (Compile / console / scalacOptions).value
)

