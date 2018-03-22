import sbt._

object Dependencies {
  lazy val javaMail = "javax.mail" % "mail" % "1.4.7"
  lazy val javaActivation = "javax.activation" % "activation" % "1.1.1"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val dumbster = "dumbster" % "dumbster" % "1.6"
}
