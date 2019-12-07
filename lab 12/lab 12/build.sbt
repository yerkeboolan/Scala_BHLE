name := "lab 12"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.21",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.21",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-http"   % "10.1.7",
  // JSON support
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7",
  "com.typesafe.akka" %% "akka-stream" % "2.5.21",

  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.531"
)
