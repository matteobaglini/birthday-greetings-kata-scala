package it.kata.birthday_greetings

import Repository._
import GreetingsNotification._

object BirthdayService {
  def sendGreetings(fileName: String,
                    xDate: XDate,
                    smtpHost: String,
                    smtpPort: Int): Unit = {
    for {
      e <- loadEmployees(fileName)
      if (e.isBirthday(xDate))
    } sendMessage(e, smtpHost, smtpPort)
  }
}
