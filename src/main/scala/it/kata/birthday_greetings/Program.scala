package it.kata.birthday_greetings

import BirthdayService._

object Program {
  def main(args: Array[String]): Unit = {
    val employeeRepository =
      FlatFileEmployeeRepository.fromFile("employee_data.txt")

    sendGreetings(XDate())(employeeRepository, "localhost", 25)
      .unsafeRunSync()
  }
}
