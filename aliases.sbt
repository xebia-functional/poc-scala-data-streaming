import CustomSbt.*

addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; scalafixAll --check; ++test")

addCommandAlias("l", "projects")

addCommandAlias("ll", "projects")

addCommandAlias("ls", "projects")

addCommandAlias("cd", "project")

addCommandAlias("root", "cd poc-scala-data-streaming")

addCommandAlias("c", "compile")

addCommandAlias("ct", "Test / compile")

addCommandAlias("t", "test")

addCommandAlias("r", "run")

addCommandAlias("rs", "reStart")

addCommandAlias("s", "reStop")

addCommandAlias(
  "styleCheck",
  "scalafixAll --check; scalafmtSbtCheck; scalafmtCheckAll"
)

addCommandAlias(
  "styleFix",
  "scalafixAll; scalafmtSbt; scalafmtAll"
)

addCommandAlias(
  "explicit",
  "undeclaredCompileDependenciesTest"
)

addCommandAlias(
  "up2date",
  "reload plugins; reload return; dependencyUpdates"
)

addCommandAlias(
  "runCoverage",
  "clean; coverage; test; coverageReport; coverageAggregate"
)

addCommandAlias(
  "flinkIT",
  "processor-flink-integration / test"
)

addCommandAlias(
  "runFlink",
  "main/runMain com.fortyseven.main.FlinkMain;"
)

addCommandAlias(
  "generateData",
  "main/runMain com.fortyseven.main.DataGeneratorMain;"
)

addCommandAlias(
  "runSpark",
  "processor-spark/run;"
)

onLoadMessage +=
  s"""|
      |╭─────────────┴─────────────────────────────╮
      |│     List of defined ${styled("aliases")}               │
      |├─────────────┴─────────────────────────────┤
      |│ ${styled("l")} | ${styled("ll")} | ${styled("ls")} │ projects                    │
      |│ ${styled("cd")}          │ project                     │
      |│ ${styled("root")}        │ cd root                     │
      |│ ${styled("c")}           │ compile                     │
      |│ ${styled("ct")}          │ compile test                │
      |│ ${styled("t")}           │ test                        │
      |│ ${styled("r")}           │ run                         │
      |│ ${styled("rs")}          │ reStart                     │
      |│ ${styled("s")}           │ reStop                      │
      |│ ${styled("styleCheck")}  │ scala fix & fmt check       │
      |│ ${styled("styleFix")}    │ scala fix & fmt             │
      || ${styled("explicit")}    | transitive dependency check |
      |│ ${styled("up2date")}     │ dependency updates          │
      |│ ${styled("runCoverage")} │ coverage report
      |│ ${styled("generateData")}│ start sending dummy data
      |│ ${styled("flinkIT")}     │ run flink integration tests │
      |│ ${styled("runFlink")}    │ run flink                   │
      |│ ${styled("runSpark")}    │ run spark                   │
      |╰─────────────┴─────────────────────────────╯""".stripMargin
