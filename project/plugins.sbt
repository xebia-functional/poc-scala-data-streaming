ThisBuild / useSuperShell := false

ThisBuild / autoStartServer := false

addDependencyTreePlugin

addSbtPlugin("ch.epfl.scala"     % "sbt-scalafix"              % "0.11.0")
addSbtPlugin("com.eed3si9n"      % "sbt-assembly"              % "2.1.1")
addSbtPlugin("com.github.cb372"  % "sbt-explicit-dependencies" % "0.3.1")
addSbtPlugin("com.github.sbt"    % "sbt-git"                   % "2.0.1")
addSbtPlugin("com.github.sbt"    % "sbt-native-packager"       % "1.9.16")
addSbtPlugin("com.timushev.sbt"  % "sbt-updates"               % "0.6.3")
addSbtPlugin("de.heikoseeberger" % "sbt-header"                % "5.10.0")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt"              % "2.5.0")
addSbtPlugin("org.scoverage"     % "sbt-scoverage"             % "2.0.8")
