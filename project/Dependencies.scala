import sbt._

object Dependencies {

  object Versions {
    val scala: String = "3.2.2"
    val organizeImports: String = "0.6.0"
  }

 object SbtPlugins {
   val organizeImports: ModuleID = "com.github.liancheng" %% "organize-imports" % Versions.organizeImports
 }

}
