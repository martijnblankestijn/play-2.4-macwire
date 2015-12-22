name := "play-course-macwire-app"

scalaVersion := "2.11.7"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)

routesGenerator := InjectedRoutesGenerator

val macWireVersion = "2.2.1"
libraryDependencies ++= Seq(
  ws,                    // Play Web Services API
  evolutions,            // Play Database Evolutions
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "com.typesafe.slick" %% "slick" % "3.1.0",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  // Compile Time Dependency injection
  "com.softwaremill.macwire" %% "macros" % macWireVersion % "provided",
  "com.softwaremill.macwire" %% "util" % macWireVersion,
  "com.softwaremill.macwire" %% "proxy" % macWireVersion,

  // Test dependencies
  specs2 % Test

)

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)
// Hack to have the maximum parallelism in test to 1
// TODO investigate
concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)
javaOptions in Test += "-DDATABASE_JDBC_FORMAT_URL=jdbc:postgresql://192.168.99.100:5432/postgres?user=postgres&password=postgres"