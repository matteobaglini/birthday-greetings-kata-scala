package it.kata.birthday_greetings

import cats._
import cats.implicits._
import cats.effect.Sync
import fs2.Stream

import Repository._
import GreetingsNotification._

object BirthdayService {

  def sendGreetings[F[_]: Monad](today: XDate)(
      implicit repository: EmployeeRepository[F],
      notification: GreetingsNotification[F],
      S: Sync[F]): F[Unit] = {

    repository
      .loadEmployees()
      .filter(e => e.isBirthday(today))
      .flatMap(e => Stream.eval(notification.sendMessage(e)))
      .compile
      .drain
  }
}
