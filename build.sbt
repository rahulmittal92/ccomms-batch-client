name := """ccomms-batch-client"""
organization := "com.itcinfotech"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))

scalaVersion := "2.12.8"

resolvers += "Cascading repo" at "http://conjars.org/repo"

// Hive dependencies
libraryDependencies ++= Seq(
 "org.apache.hive" % "hive-exec" % "1.2.1",
 "org.apache.hive" % "hive-jdbc" % "1.2.1",
 "org.apache.hadoop" % "hadoop-common" % "2.7.3",
 "org.pentaho" % "pentaho-aggdesigner-algorithm" % "5.1.5-jhyde" % Test
)

// Web-service dependencies
libraryDependencies ++= Seq(
 "org.apache.httpcomponents" % "httpclient" % "4.5.5",
  "org.json4s" %% "json4s-jackson" % "3.6.6"
)

// Tooling dependencies
libraryDependencies += "com.typesafe" % "config" % "1.3.4"

// Logging dependencies
libraryDependencies ++= Seq(
  "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0",
  "org.apache.logging.log4j" % "log4j-api" % "2.11.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.11.0" % Runtime
)
