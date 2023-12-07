
val AkkaVersion = "2.9.0"
val AkkaDiagnosticsVersion = "2.1.0"
val LogbackClassicVersion = "1.2.11"
val ScalaTestVersion = "3.1.1"

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.xebia"
ThisBuild / organizationName := "Xebia"

lazy val root = (project in file("."))
  .settings(
    name := "Kafka Producer POC",
    resolvers += "Akka library repository".at("https://repo.akka.io/maven"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream-kafka" % "5.0.0",
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion,
      "com.lightbend.akka" %% "akka-diagnostics" % AkkaDiagnosticsVersion,
      "ch.qos.logback" % "logback-classic" % LogbackClassicVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
      "org.scalatest" %% "scalatest" % ScalaTestVersion % Test
    ),
  )
