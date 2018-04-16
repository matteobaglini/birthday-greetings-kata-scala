package it.kata.birthday_greetings

import Repository._
import GreetingsNotification._

object BirthdayService {
  def sendGreetings(loadEmployees: LoadEmployees,
                    sendMessage: SendMessage,
                    today: XDate): Unit = {
    loadEmployees()
      .unsafeRunSync()
      .filter(e => e.isBirthday(today))
      .foreach(e => sendMessage(e).unsafeRunSync())
  }
}
