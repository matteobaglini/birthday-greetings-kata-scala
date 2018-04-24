package it.kata.birthday_greetings

import cats.effect.IO
import cats.implicits._

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
      .map(_.filter(_.isBirthday(today)))
      .flatMap(notification.sendMessages(_))
      .fold(display.printError(_), _ => display.printDone())
      .flatten
  }
}
