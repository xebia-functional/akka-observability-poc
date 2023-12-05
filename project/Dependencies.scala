import sbt._

object Dependencies {

  object Version {
    val scalaLogging = "3.9.2"
    val logstashLogbackEncoder = "4.11"
    val logback = "1.2.3"
    val nclCommonServer = "1.3.6"
    val nclCommonHttp = "1.0.20"
    val nclCommonTesting = "0.0.2"
    val mockito = "1.16.37"
    val scalaCheck = "1.15.2"
  }

  val libraryDependencies = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % Version.scalaLogging,
    "ch.qos.logback" % "logback-core" % Version.logback,
    "ch.qos.logback" % "logback-classic" % Version.logback,
    "ch.qos.logback" % "logback-access" % Version.logback,
    "net.logstash.logback" % "logstash-logback-encoder" % Version.logstashLogbackEncoder,
    "com.ncl" %% "ncl-common-server" % Version.nclCommonServer,
    "com.ncl" %% "ncl-common-http" % Version.nclCommonHttp,

    // Testing
    "com.ncl" %% "ncl-common-testing" % Version.nclCommonTesting,
    "org.mockito" %% "mockito-scala-scalatest" % Version.mockito % Test,
    "org.scalacheck" %% "scalacheck" % Version.scalaCheck % Test,
  )
}