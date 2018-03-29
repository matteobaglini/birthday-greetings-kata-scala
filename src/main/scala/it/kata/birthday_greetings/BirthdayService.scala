package it.kata.birthday_greetings

import Repository._
import GreetingsNotification._

object BirthdayService {
  def sendGreetings(fileName: String,
                    today: XDate,
                    smtpHost: String,
                    smtpPort: Int): Unit = {

    val loadEmployees = buildFileLoadEmployees(fileName)
    val sendMessage = buildSmtpSendMessage(smtpHost, smtpPort)

    sendGreetings(loadEmployees, sendMessage, today)
  }

  def sendGreetings(loadEmployees: LoadEmployees,
                    sendMessage: SendMessage,
                    today: XDate): Unit = {
    for {
      e <- loadEmployees()
      if (e.isBirthday(today))
    } sendMessage(e)
  }
}
