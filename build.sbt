import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "birthday-greetings-kata-scala",
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",   // source files are in UTF-8
      "-deprecation",         // warn about use of deprecated APIs
      "-unchecked",           // warn about unchecked type parameters
      "-feature",             // warn about misused language features
      "-language:higherKinds",// allow higher kinded types without `import scala.language.higherKinds`
      "-Xlint",               // enable handy linter warnings
      "-Xfatal-warnings",     // turn compiler warnings into errors
      "-Ypartial-unification" // allow the compiler to unify type constructors of different arities
    ),
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
