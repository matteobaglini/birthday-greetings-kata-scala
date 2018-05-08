package it.kata.birthday_greetings

import cats.effect._

import BirthdayService._

object Program {
  def main(args: Array[String]): Unit = {
    implicit val employeeRepository =
      FlatFileEmployeeRepository.fromFile[IO]("employee_data.txt")
    implicit val messageGateway =
      SmtpMessageGateway.fromEndpoint[IO]("localhost", 25)

    sendGreetings(XDate())
      .unsafeRunSync()
  }
}
