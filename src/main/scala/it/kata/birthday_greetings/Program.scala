package it.kata.birthday_greetings

import BirthdayService._

object Program {
  def main(args: Array[String]): Unit = {
    implicit val employeeRepository =
      FlatFileEmployeeRepository.fromFile("employee_data.txt")
    implicit val messageGateway =
      SmtpMessageGateway.fromEndpoint("localhost", 25)

    sendGreetings(XDate())
      .unsafeRunSync()
  }
}
