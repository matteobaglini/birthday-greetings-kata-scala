package it.kata.birthday_greetings

import Repository._
import GreetingsNotification._

object BirthdayService {
  def sendGreetings(fileName: String,
                    xDate: XDate,
                    smtpHost: String,
                    smtpPort: Int): Unit = {

    val loadEmployees = buildFileLoadEmployees(fileName)
    val sendMessage = buildSmtpSendMessage(smtpHost, smtpPort)

    sendGreetings(loadEmployees, sendMessage, xDate)
  }

  def sendGreetings(loadEmployees: LoadEmployees,
                    sendMessage: SendMessage,
                    xDate: XDate): Unit = {
    for {
      e <- loadEmployees()
      if (e.isBirthday(xDate))
    } sendMessage(e)
  }
}
