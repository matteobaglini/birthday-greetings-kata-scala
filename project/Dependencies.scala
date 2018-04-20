import sbt._

object Dependencies {
  lazy val javaMail = "javax.mail" % "mail" % "1.4.7"
  lazy val javaActivation = "javax.activation" % "activation" % "1.1.1"
  lazy val catsCore = "org.typelevel" %% "cats-core" % "1.1.0"
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "0.10.1"
  lazy val fs2 = "co.fs2" %% "fs2-core" % "0.10.3"
  lazy val fs2io = "co.fs2" %% "fs2-io" % "0.10.3"

  lazy val miniTest = "io.monix" %% "minitest" % "2.1.1"
  lazy val dumbster = "dumbster" % "dumbster" % "1.6"
}