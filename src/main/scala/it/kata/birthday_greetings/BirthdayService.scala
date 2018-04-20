package it.kata.birthday_greetings

import cats._
import cats.implicits._

import Repository._
import GreetingsNotification._

object BirthdayService {

  def sendGreetings[F[_]: Monad](today: XDate)(
      implicit repository: EmployeeRepository[F],
      notification: GreetingsNotification[F]): F[Unit] = {

    repository
      .loadEmployees()
      .map(es => es.filter(e => e.isBirthday(today)))
      .flatMap(bs => notification.sendMessages(bs))
  }
}
