package it.kata.birthday_greetings

import BirthdayService._

object Program {
  def main(args: Array[String]): Unit = {
    sendGreetings(XDate())("employee_data.txt", "localhost", 25)
      .unsafeRunSync()
  }
}
