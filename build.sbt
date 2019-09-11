lazy val commonSettings = commonSmlBuildSettings ++ ossPublishSettings ++ Seq(
  organization := "com.softwaremill.correlator",
  scalaVersion := "2.12.9"
)

val http4sVersion = "0.20.10"

val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8" % "test"

lazy val rootProject = (project in file("."))
  .settings(commonSettings: _*)
  .settings(publishArtifact := false, name := "root")
  .aggregate(monixLogbackHttp4s)

lazy val monixLogbackHttp4s: Project = (project in file("monix-logback-http4s"))
  .settings(commonSettings: _*)
  .settings(
    name := "monix-logback-http4s",
    libraryDependencies ++= Seq(
      "io.monix" %% "monix" % "3.0.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion % "test",
      scalaTest
    )
  )

