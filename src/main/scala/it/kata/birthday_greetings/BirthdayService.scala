package it.kata.birthday_greetings

import cats.instances.list._
import cats.syntax.traverse._
import cats.effect.IO

import Repository._
import GreetingsNotification._

object BirthdayService {

  def sendGreetings(repository: Repository,
                    sendMessage: SendMessage,
                    today: XDate): IO[Unit] = {

    repository
      .loadEmployees()
      .map(es => es.filter(e => e.isBirthday(today)))
      .flatMap(bs => bs.traverse(e => sendMessage(e)).map(_ => ()))
  }
}
