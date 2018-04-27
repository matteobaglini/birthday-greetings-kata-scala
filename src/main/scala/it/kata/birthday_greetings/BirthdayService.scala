package it.kata.birthday_greetings

import cats._
import cats.implicits._

import EmployeeRepository._
import GreetingsGateway._
import Domain._

case class Config(fileName: String, smtpHost: String, smtpPort: Int)

object BirthdayService {

  def sendGreetings[F[_]](today: XDate)(implicit
                                        M: Monad[F],
                                        ER: EmployeeRepository[F],
                                        GG: GreetingsGateway[F]): F[Unit] = {
    for {
      es <- ER.loadEmployees()
      bs = hasBirthday(today, es)
      _ <- GG.sendAllGreetings(bs)
    } yield ()
  }
}
