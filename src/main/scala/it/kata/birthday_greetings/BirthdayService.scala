package it.kata.birthday_greetings

import Repository._
import GreetingsNotification._

object BirthdayService {
  def sendGreetings(loadEmployees: LoadEmployees,
                    sendMessage: SendMessage,
                    today: XDate): Unit = {
    for {
      e <- loadEmployees().unsafeRunSync()
      if (e.isBirthday(today))
    } sendMessage(e)
  }
}
