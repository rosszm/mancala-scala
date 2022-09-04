// Mancala Build file

ThisBuild / organization := "dev.zacharyross.mancala"
ThisBuild / scalaVersion := "3.1.3"


// The http / ws server dependencies
val catsVersion    = "3.3.12"
val http4sVersion  = "0.23.15"
val fs2Version     = "3.2.12"
val circeVersion   = "0.14.1"
val logbackVersion = "1.4.0"

lazy val serverDeps = Seq(
  "org.typelevel" %% "cats-effect"         % catsVersion,
  "org.http4s"    %% "http4s-dsl"          % http4sVersion,
  "org.http4s"    %% "http4s-ember-server" % http4sVersion,
  "org.http4s"    %% "http4s-circe"        % http4sVersion,
  "co.fs2"        %% "fs2-core"            % fs2Version,
  "io.circe"      %% "circe-generic"       % circeVersion,
  "ch.qos.logback" % "logback-classic"     % logbackVersion % Runtime,
)

/**
  * The core mancala project.
  * 
  * This contains all the core classes and state management tools that define
  * how a mancala game is played.
  */
lazy val core = project
  .settings(
    name := "mancala-core",
    version := "1.0",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test,
    )
  )

// The CLI implementation of manacala.
lazy val cli = project
  .dependsOn(core)
  .settings(
    name := "mancala-cli",
    version := "1.0",
    assembly / assemblyJarName := "mancala-cli.jar",
  )

// The http server implementation of manacala
lazy val server = project
  .dependsOn(core)
  .settings(
    name := "mancala-server",
    version := "1.0",
    assembly / assemblyJarName := "mancala-server.jar",
    libraryDependencies ++= serverDeps,
  )
