package it.kata.birthday_greetings

import cats.effect.IO

import Repository._
import GreetingsNotification._

object BirthdayService {

  def sendGreetings(repository: EmployeeRepository,
                    notification: GreetingsNotification,
                    today: XDate): IO[Unit] = {

    repository
      .loadEmployees()
      .map(es => es.filter(e => e.isBirthday(today)))
      .flatMap(bs => notification.sendMessages(bs))
  }
}
