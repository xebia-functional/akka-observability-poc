import sbt._

object Dependencies {

  object Version {
    val scalaLogging = "3.9.2"
    val logstashLogbackEncoder = "4.11"
    val logback = "1.2.11"
    val nclCommonServer = "1.3.6"
    val nclCommonHttp = "1.0.20"
    val nclCommonTesting = "0.0.2"
    val mockito = "1.16.37"
    val scalaCheck = "1.15.2"

    val akkaVersion = "2.6.18"
    val akkaDiagnosticsVersion = "2.0.0"
    val scalaTestVersion = "3.1.1"
    val akkaStreamKafka = "3.0.0"
  }

  val libraryDependencies = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % Version.scalaLogging,
    "ch.qos.logback" % "logback-core" % Version.logback,
    "ch.qos.logback" % "logback-classic" % Version.logback,
    "ch.qos.logback" % "logback-access" % Version.logback,
    "net.logstash.logback" % "logstash-logback-encoder" % Version.logstashLogbackEncoder,
    "com.ncl" %% "ncl-common-server" % Version.nclCommonServer,
    "com.ncl" %% "ncl-common-http" % Version.nclCommonHttp,

    // akka
    "com.typesafe.akka" %% "akka-actor-typed" % Version.akkaVersion,
    "com.typesafe.akka" %% "akka-stream-kafka" % Version.akkaStreamKafka,
    "com.typesafe.akka" %% "akka-stream" % Version.akkaVersion,
    "com.typesafe.akka" %% "akka-persistence-typed" % Version.akkaVersion,
    "com.typesafe.akka" %% "akka-serialization-jackson" % Version.akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-sharding-typed" % Version.akkaVersion,
    "com.typesafe.akka" %% "akka-distributed-data" % Version.akkaVersion,
   // "com.lightbend.akka" %% "akka-diagnostics" % Version.akkaDiagnosticsVersion,

    // monitoring
    "com.newrelic.agent.java" %% "newrelic-scala-api" % "8.7.0",

    // Testing
    "com.ncl" %% "ncl-common-testing" % Version.nclCommonTesting,
    "org.mockito" %% "mockito-scala-scalatest" % Version.mockito % Test,
    "org.scalacheck" %% "scalacheck" % Version.scalaCheck % Test,

    "com.typesafe.akka" %% "akka-actor-testkit-typed" % Version.akkaVersion % Test,
    "org.scalatest" %% "scalatest" % Version.scalaTestVersion % Test
  )
}
