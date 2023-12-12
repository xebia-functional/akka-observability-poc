// ThisBuild / scalaVersion     := "2.13.12"
// ThisBuild / version          := "0.1.0-SNAPSHOT"
// ThisBuild / organization     := "com.xebia"
// ThisBuild / organizationName := "Xebia"

lazy val root = (project in file("."))
  .enableAkkaProject()
  .enableCinnamon
  .settings(
    scalacOptions += "-deprecation",
    organization := "com.nclh",
    name := "nclh-akka-service",
    nclMaintainer := "NCLH Digital Experience Team",
    libraryDependencies ++= Dependencies.libraryDependencies,
    resolvers += "Akka library repository".at("https://repo.akka.io/maven"),
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
