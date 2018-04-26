package it.kata.birthday_greetings

import BirthdayService._

object Program {
  def main(args: Array[String]): Unit = {
    val config = Config("employee_data.txt", "localhost", 25)
    sendGreetings(XDate())
      .run(config)
      .unsafeRunSync()
  }
}
