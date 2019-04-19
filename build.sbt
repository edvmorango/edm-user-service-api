name := "edm-user-service-api"

version := "0.1"

scalaVersion := "2.12.8"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8")

scalacOptions ++= Seq(
  "-Ypartial-unification",
  "-language:higherKinds",
  "-deprecation",
  "-encoding", "utf-8",
  "-explaintypes",
  "-feature",
  "-language:existentials",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint:infer-any",
  "-Xlint:type-parameter-shadow",
  "-Xlint:unsound-match",
  "-Ywarn-dead-code",
  "-Ywarn-extra-implicit",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates",
  "-Ywarn-value-discard"
)

lazy val zioVersion =  "1.0-RC4"
lazy val http4sVersion = "0.20.0-RC1"
lazy val fs2Version = "1.0.4"
lazy val circeVersion = "0.11.1"
lazy val scanamoVersion = "1.0.0-M8"
lazy val pureConfigVersion =  "0.10.2"


lazy val fuuidVersion = "0.2.0-M8"
// Dependencies

libraryDependencies ++=  Seq(
  "org.scalaz" %% "scalaz-zio",
  "org.scalaz" %% "scalaz-zio-interop-shared",
  "org.scalaz" %% "scalaz-zio-interop-cats"
).map(_ % zioVersion)


libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl",
  "org.http4s" %% "http4s-circe"
).map(_ % http4sVersion)


libraryDependencies ++= Seq(
  "co.fs2" %% "fs2-core",
  "co.fs2" %% "fs2-io"
).map(_ % fs2Version)


libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-generic-extras",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)


libraryDependencies ++= Seq (
  "com.gu" %% "scanamo",
  "com.gu" %% "scanamo-alpakka"
).map(_ % scanamoVersion)


libraryDependencies ++= Seq(
  "com.github.pureconfig" %% "pureconfig",
  "com.github.pureconfig" %% "pureconfig-yaml"
).map(_ % pureConfigVersion)


libraryDependencies += "io.chrisdavenport" %% "fuuid" % fuuidVersion