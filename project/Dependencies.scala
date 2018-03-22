import sbt._

object Dependencies {
  lazy val javaMail = "javax.mail" % "mail" % "1.4.7"
  lazy val javaActivation = "javax.activation" % "activation" % "1.1.1"

  lazy val miniTest = "io.monix" %% "minitest" % "2.1.1"
  lazy val dumbster = "dumbster" % "dumbster" % "1.6"
}