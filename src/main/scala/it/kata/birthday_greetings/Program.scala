package it.kata.birthday_greetings

import BirthdayService._

import cats.data.ReaderT
import cats.effect.IO
import cats.mtl.implicits._

case class Config(fileName: String, smtpHost: String, smtpPort: Int)

object Program {

  type Result[A] = ReaderT[IO, Config, A]

  def main(args: Array[String]): Unit = {

    val config = Config("employee_data.txt", "localhost", 25)

    implicit val employeeRepository = EmployeeRepository[Result]()
    implicit val greetingsGateway = GreetingsGateway[Result]()

    sendGreetings[Result](XDate())
      .run(config)
      .unsafeRunSync()
  }
}
