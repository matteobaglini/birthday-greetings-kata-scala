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
    val es: List[Employee] = loadIO.unsafeRunSync()

    val birthdays: List[Employee] = es.filter(e => e.isBirthday(today))

    val collapsedSendAllIO: IO[Unit] =
      birthdays.traverse(e => sendMessage(e)).map(_ => ())

    collapsedSendAllIO.unsafeRunSync()
  }
}
