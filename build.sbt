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
    "-Ykind-projector",
    "-Ysafe-init"       // experimental (I've seen it cause issues with circe)
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future-migration")

lazy val `poc-scala-data-streaming`: Project =
  project
    .in(file("."))
    .aggregate(
      // layer 1
      // team red
      `core-headers`,
      // team yellow (utils/common)

      // layer 2
      // team blue
      `data-generator`,
      `kafka-consumer`,
      `job-processor-flink`,
      `job-processor-spark`,
      `job-processor-storm`,
      // team green
      core,

      // layer 3
      // team red
      entryPoint
    )

// layer 1

// team red
lazy val `core-headers`: Project =
  project
    .in(file("01-core-headers"))
    .settings(commonSettings)
    .settings(commonDependencies)
    .settings(
      name := "core-headers",
      libraryDependencies ++= Seq(
        Libraries.kafka.fs2Kafka,
        Libraries.codec.fs2KafkaVulcan,
      )
    )

// layer 2

// team yellow (c=common)
lazy val configuration: Project = (project in file("02-c-config-ciris"))
  .dependsOn(`core-headers`)
  .settings(
    name := "configuration",
    libraryDependencies ++= Libraries.config.all
  )
  .settings(commonSettings)
  .settings(commonDependencies)


lazy val `data-generator`: Project = (project in file("02-c-data-generator"))
  .dependsOn(`core-headers`)
  .settings(
    name := "data-generator"
  )
  .settings(commonSettings)
  .settings(commonDependencies)
  .settings(
    libraryDependencies ++= Seq(
      Libraries.kafka.fs2Kafka,
      Libraries.codec.fs2KafkaVulcan
    )
  )

lazy val `kafka-util`: Project = (project in file("02-c-kafka-util"))
  .settings(
    name := "kafka-util"
  )

// team blue (i=input) from here
lazy val `kafka-consumer`: Project =
  project
    .in(file("02-i-kafka-consumer"))
    .dependsOn(`kafka-util` % Cctt)
    .dependsOn(configuration % Cctt) // does not depend in core-headers because it depends on kafka-utils (transitive)
    .settings(commonSettings)
    .settings(commonDependencies)
    .settings(
      libraryDependencies ++= Seq(Libraries.kafka.fs2Kafka)
    )

lazy val `job-processor-spark`: Project =
  project
    .in(file("02-i-job-processor-spark"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq()
    )

lazy val `job-processor-flink`: Project =
  project
    .in(file("02-i-job-processor-flink"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq()
    )

lazy val `job-processor-storm`: Project =
  project
    .in(file("02-i-job-processor-storm"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq()
    )

// team green (o=output) from here
lazy val core: Project =
  project
    .in(file("02-o-core"))
    .dependsOn(`core-headers` % Cctt)
    .settings(commonSettings)
    .settings(
      name := "core",
      libraryDependencies ++= Seq()
    )

// layer 3
// team red
lazy val entryPoint: Project =
  project
    .in(file("03-entryPoint"))
    // the dependency on `core-headers` is added transitively
    // the dependencies on team yellow are added transitively
    // team blue
    .dependsOn(`kafka-consumer` % Cctt)
    .dependsOn(`data-generator` % Cctt)
    .dependsOn(`job-processor-flink` % Cctt)
    .dependsOn(`job-processor-spark` % Cctt)
    .dependsOn(`job-processor-storm` % Cctt)
    // team green
    .dependsOn(core % Cctt)
    .settings(commonSettings)
    .settings(
      name := "entryPoint",
      libraryDependencies ++= Seq(
        // the less the better (usually zero)
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
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value
)

lazy val commonDependencies = Seq(
  libraryDependencies ++= Seq(
    Libraries.cats.catsEffect,
    Libraries.logging.log4catsSlf4j,
    Libraries.logging.logback
  ),
  libraryDependencies ++= Seq(
    Libraries.test.munitCatsEffect,
    Libraries.test.munitScalacheck,
    Libraries.test.scalatest,
  ).map(_ % Test)
)
