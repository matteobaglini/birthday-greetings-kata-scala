package it.kata.birthday_greetings

import Repository._
import GreetingsNotification._

object BirthdayService {
  def sendGreetings(fileName: String,
                    xDate: XDate,
                    smtpHost: String,
                    smtpPort: Int): Unit = {

    val loadEmployees = buildFileLoadEmployees(fileName)

    for {
      e <- loadEmployees()
      if (e.isBirthday(xDate))
    } sendMessage(smtpHost, smtpPort, e)
  }
}
