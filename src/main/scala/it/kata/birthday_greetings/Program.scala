package it.kata.birthday_greetings

import cats.data._
import cats.effect._
import cats.mtl.implicits._

import BirthdayService._

case class Config(fileName: String,
                  smtpHost: String,
                  smtpPort: Int,
                  errorColor: String)

object Program {

  type Result[A] = ReaderT[IO, Config, A]

  implicit val employeeRepository = EmployeeRepository[Result]()
  implicit val greetingsGateway = GreetingsGateway[Result]()
  implicit val display = Display[Result](Console.out)

  def main(args: Array[String]): Unit = {

    val config = Config("employee_data.txt", "localhost", 25, Console.MAGENTA)

    sendGreetings[Result](XDate("2008/10/08"))
      .run(config)
      .unsafeRunSync()
  }
}
