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

    val loadIO: IO[List[Employee]] = loadEmployees()

    val birthdaysIO: IO[List[Employee]] =
      loadIO.map(es => es.filter(e => e.isBirthday(today)))
    val birthdays: List[Employee] = birthdaysIO.unsafeRunSync()

    val collapsedSendAllIO: IO[Unit] =
      birthdays.traverse(e => sendMessage(e)).map(_ => ())

    collapsedSendAllIO.unsafeRunSync()
  }
}
