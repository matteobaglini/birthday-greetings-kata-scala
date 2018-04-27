package it.kata.birthday_greetings

import cats._
import cats.implicits._

import EmployeeRepository._
import GreetingsGateway._
import Display._
import Domain._

object BirthdayService {

  def sendGreetings[F[_]](today: XDate)(implicit
                                        M: MonadError[F, Throwable],
                                        ER: EmployeeRepository[F],
                                        GG: GreetingsGateway[F],
                                        D: Display[F]): F[Unit] = {
    (for {
      es <- ER.loadEmployees()
      bs = hasBirthday(today, es)
      r <- GG.sendAll(bs)
    } yield r).attempt
      .flatMap {
        case Right(_) => D.printDone
        case Left(e)  => D.printError(e)
      }
  }
}
