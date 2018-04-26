package it.kata.birthday_greetings

import BirthdayService._

object Program {
  def main(args: Array[String]): Unit = {
    sendGreetings(Config("employee_data.txt", "localhost", 25), XDate())
  }
}
