import CustomSbt.*

addCommandAlias("l", "projects")

addCommandAlias("ll", "projects")

addCommandAlias("ls", "projects")

addCommandAlias("cd", "project")

addCommandAlias("root", "cd poc-scala-data-streaming")

addCommandAlias("c", "compile")

addCommandAlias("ca", "Test / compile")

addCommandAlias("t", "test")

addCommandAlias("r", "run")

addCommandAlias("rs", "reStart")

addCommandAlias("s", "reStop")

addCommandAlias("star", "thankYouStars")

addCommandAlias(
  "styleCheck",
  "scalafmtSbtCheck; scalafmtCheckAll"
)

addCommandAlias(
  "styleFix",
  "scalafix; scalafmtSbt; scalafmtAll"
)

addCommandAlias(
  "up2date",
  "reload plugins; dependencyUpdates; reload return; dependencyUpdates"
)

addCommandAlias(
  "coverage",
  "clean; coverage; test; coverageReport; coverageAggregate"
)

onLoadMessage +=
  s"""|
      |╭─────────────────────────────────╮
      |│     List of defined ${styled("aliases")}     │
      |├─────────────┬───────────────────┤
      |│ ${styled("l")} | ${styled("ll")} | ${styled("ls")} │ projects          │
      |│ ${styled("cd")}          │ project           │
      |│ ${styled("root")}        │ cd root           │
      |│ ${styled("c")}           │ compile           │
      |│ ${styled("ca")}          │ compile all       │
      |│ ${styled("t")}           │ test              │
      |│ ${styled("r")}           │ run               │
      |│ ${styled("rs")}          │ reStart           │
      |│ ${styled("s")}           │ reStop            │
      |│ ${styled("star")}        │ thankYouStars     │
      |│ ${styled("styleCheck")}  │ fmt check         │
      |│ ${styled("styleFix")}    │ fmt               │
      |│ ${styled("up2date")}     │ dependency updates│
      |│ ${styled("coverage")}    │ coverage report   │
      |╰─────────────┴───────────────────╯""".stripMargin
