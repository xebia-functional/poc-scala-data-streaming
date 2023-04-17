import sbt._

object Dependencies {

  object Versions {
    val scala: String = "3.2.2"
    val organizeImports: String = "0.6.0"
  }

 object SbtPlugins {
   val organizeImports: ModuleID = "com.github.liancheng" %% "organize-imports" % Versions.organizeImports
 }

  object org {
    object scalatest {
      val scalatest: ModuleID =
        "org.scalatest" %% "scalatest" % "3.2.11"
    }

    object scalatestplus {
      val `scalacheck-1-15`: ModuleID =
        "org.scalatestplus" %% "scalacheck-1-15" % "3.2.11.0"
    }
  }

}
