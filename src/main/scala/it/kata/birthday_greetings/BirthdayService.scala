package it.kata.birthday_greetings

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

    val sendAllIO: List[IO[Unit]] = birthdays.map(e => {
      val sendIO: IO[Unit] = sendMessage(e)
      sendIO
    })

    sendAllIO.foreach(_.unsafeRunSync())
  }
}
