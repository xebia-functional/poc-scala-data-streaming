import Dependencies._

Global / onChangedBuildSource := ReloadOnSourceChanges
ThisBuild / organization := "com.47deg"
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/47deg/poc-scala-data-streaming"),
    "scm:git@github.com:47deg/poc-scala-data-streaming.git"
  )
)
ThisBuild / scalaVersion := Versions.scala
ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixDependencies += SbtPlugins.organizeImports

lazy val `poc-scala-data-streaming`: Project = (project in file("."))
  .settings(
    name := "poc-scala-data-streaming"
  )

lazy val common: Project = (project in file("common"))
  .settings(
    name := "common"
  )

lazy val `data-generator`: Project = (project in file("data-generator"))
  .settings(
    name := "data-generator"
  )

lazy val `processor-flink`: Project = (project in file("processor/flink"))
  .settings(
    name := "processor-flink"
  )

lazy val `processor-kafka`: Project = project.in(file("processor/kafka"))
  .settings(
    name := "processor-kafka"
  )

