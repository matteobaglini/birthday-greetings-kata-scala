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
    ER.loadEmployees()
      .map(hasBirthday(today, _))
      .flatMap(GG.sendAll(_))
      .attempt
      .flatMap(_.fold(e => D.printError(e), _ => D.printDone()))
  }
}
