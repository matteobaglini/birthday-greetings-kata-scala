package it.kata.birthday_greetings

import cats.instances.list._
import cats.syntax.traverse._
import cats.effect.IO

import Repository._
import GreetingsNotification._

object BirthdayService {
  def sendGreetings(loadEmployees: LoadEmployees,
                    sendMessage: SendMessage,
                    today: XDate): Unit = {

    val birthdaysIO: IO[List[Employee]] =
      loadEmployees()
        .map(es => es.filter(e => e.isBirthday(today)))

    val collapsedSendAllIO: IO[Unit] =
      birthdaysIO.flatMap(birthdays =>
        birthdays.traverse(e => sendMessage(e)).map(_ => ()))

    collapsedSendAllIO.unsafeRunSync()
  }
}
