package it.kata.birthday_greetings

import Repository._
import GreetingsNotification._
import BirthdayService._

object Program {
  def main(args: Array[String]): Unit = {

    val repository = buildFileLoadEmployees("employee_data.txt")
    val sendMessage = buildSmtpSendMessage("localhost", 25)
    val today = XDate()

    val program = sendGreetings(repository, sendMessage, today)
    program.unsafeRunSync()
  }
}
