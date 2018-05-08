package it.kata.birthday_greetings

import BirthdayService._

object Program {
  def main(args: Array[String]): Unit = {
    sendGreetings("employee_data.txt", XDate(), "localhost", 25)
      .unsafeRunSync()
  }
}
