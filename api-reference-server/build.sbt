enablePlugins(JavaAppPackaging)

name := "cfapiserver"
organization := "com.cabforce"

version := "1.0"

scalaVersion := "2.12.4"

mainClass in (Compile, run) := Some("com.cabforce.service.HttpMicroservice")
mainClass in (Compile, packageBin) := Some("com.cabforce.service.HttpMicroservice")

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.5.11"
  val akkaHttpV   = "10.0.11"
  val scalaTestV  = "3.0.4"
  val cacheV      = "10.0.11"

  Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-caching" % cacheV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "org.scalatest"     %% "scalatest" % scalaTestV % "test"
  )
}

Revolver.settings
