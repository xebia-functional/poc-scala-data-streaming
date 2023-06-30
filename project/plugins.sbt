ThisBuild / useSuperShell := false

ThisBuild / autoStartServer := false

addDependencyTreePlugin

addSbtPlugin("ch.epfl.scala"     % "sbt-scalafix"              % "0.11.0")
addSbtPlugin("com.eed3si9n"      % "sbt-assembly"              % "2.1.1")
addSbtPlugin("com.github.cb372"  % "sbt-explicit-dependencies" % "0.2.16")
addSbtPlugin("com.github.sbt"    % "sbt-git"                   % "2.0.1")
addSbtPlugin("com.github.sbt"    % "sbt-native-packager"       % "1.9.16")
addSbtPlugin("com.mayreh"        % "sbt-thank-you-stars"       % "0.2")
addSbtPlugin("com.timushev.sbt"  % "sbt-rewarn"                % "0.1.3")
addSbtPlugin("com.timushev.sbt"  % "sbt-updates"               % "0.6.2")
addSbtPlugin("de.heikoseeberger" % "sbt-header"                % "5.9.0")
addSbtPlugin("io.spray"          % "sbt-revolver"              % "0.9.1")
addSbtPlugin("org.jmotor.sbt"    % "sbt-dependency-updates"    % "1.2.7")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt"              % "2.5.0")
addSbtPlugin("org.scoverage"     % "sbt-scoverage"             % "2.0.7")
