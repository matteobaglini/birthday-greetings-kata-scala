package it.kata.birthday_greetings

import cats.effect.IO

import Repository._
import GreetingsNotification._
import BirthdayService._

object Program {
  def main(args: Array[String]): Unit = {

    implicit val repository = buildFileRepositoy("employee_data.txt")
    implicit val greetingsNotification =
      buildSmtpGreetingsNotification("localhost", 25)

    val today = XDate()
    val program = sendGreetings[IO](today)
    program.unsafeRunSync()
  }
}
