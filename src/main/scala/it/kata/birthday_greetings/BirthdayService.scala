package it.kata.birthday_greetings

import cats.effect.IO

import Repository._
import GreetingsNotification._

object BirthdayService {

  def sendGreetings(today: XDate)(
      repository: EmployeeRepository,
      notification: GreetingsNotification): IO[Unit] = {

    repository
      .loadEmployees()
      .map(es => es.filter(e => e.isBirthday(today)))
      .flatMap(bs => notification.sendMessages(bs))
  }
}
