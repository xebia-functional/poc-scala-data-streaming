import sbt._

object Dependencies {

  object Versions {

    val scala = "3.2.2"

    val organizeImports = "0.6.0"

    val catsEffect = "3.4.10"
    val ciris = "3.1.0"
    val `fs2-kafka` = "3.0.0"
    val scalatest = "3.2.15"
    val `scalacheck-1-15` = "3.2.11.0"
  }

  object SbtPlugins {
    val organizeImports = "com.github.liancheng" %% "organize-imports" % Versions.organizeImports
  }

  object Libraries {

    object cats {
      val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
    }

    object config {
      val ciris = "is.cir" %% "ciris" % Versions.ciris
    }

    object kafka {
      val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % Versions.`fs2-kafka`
    }

    object test {
      val scalatest = "org.scalatest" %% "scalatest" % Versions.scalatest
      val `scalacheck-1-15` = "org.scalatestplus" %% "scalacheck-1-15" % Versions.`scalacheck-1-15`
    }
  }
}