package it.kata.birthday_greetings

import cats.effect.IO

import Display._
import Repository._
import GreetingsNotification._

object BirthdayService {

  def sendGreetings(repository: Repository,
                    notification: GreetingsNotification,
                    display: Display,
                    today: XDate): IO[Unit] = {

    repository
      .loadEmployees()
      .map(es => es.filter(e => e.isBirthday(today)))
      .flatMap(bs => notification.sendMessages(bs))
      .attempt
      .flatMap {
        case Right(_) => display.printDone()
        case Left(e)  => display.printError(e)
      }
  }
}
