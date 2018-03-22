import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "birthday-greetings-kata-scala",
    scalafmtOnCompile := true,
    resolvers ++= Seq(
      Resolver.sonatypeRepo("public")
    ),
    libraryDependencies ++= Seq(
      javaMail,
      javaActivation,
      miniTest % Test,
      dumbster % Test
    ),
    testFrameworks += new TestFramework("minitest.runner.Framework")
  )
