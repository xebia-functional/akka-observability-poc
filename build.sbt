

lazy val root = (project in file("."))
  .enableAkkaProject()
  .enableCinnamon
  .settings(
    scalacOptions += "-deprecation",
    organization := "com.nclh",
    name := "nclh-akka-service",
    nclMaintainer := "NCLH Digital Experience Team",
    libraryDependencies ++= Dependencies.libraryDependencies,
    coverageExcludedAdditionalPackages := Set(
      "app.Main",
      "app.support.AppConfig",
      "app.support.AppModule",
      "app.routing.MainRouter"
    ),
    scalastyleExcludedFiles := Set(
      "Main.scala"
    )
  )
