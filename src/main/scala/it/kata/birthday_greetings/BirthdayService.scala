package it.kata.birthday_greetings

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

object BirthdayService {
  def sendGreetings(fileName: String,
                    today: XDate,
                    smtpHost: String,
                    smtpPort: Int): Unit = {

    val loaded = loadEmployees(fileName)
    val birthdays = haveBirthday(loaded, today)
    sendMessages(smtpHost, smtpPort, birthdays)
  }

  private def loadEmployees(fileName: String): List[Employee] = {
    loadLines(fileName)
      .drop(1) // skip header
      .map(parseEmployee(_))
  }

  private def loadLines(fileName: String): List[String] = {
    val source = io.Source.fromFile(fileName)
    try source.getLines.toList
    finally source.close
  }

  private def parseEmployee(line: String): Employee = {
    val employeeData = line.split(", ")
    Employee(employeeData(1), employeeData(0), employeeData(2), employeeData(3))
  }

  private def haveBirthday(loaded: List[Employee],
                           today: XDate): List[Employee] = {
    loaded.filter(employee => employee.isBirthday(today))
  }

  private def sendMessages(smtpHost: String,
                           smtpPort: Int,
                           employees: List[Employee]): Unit = {
    for (employee <- employees) {
      val recipient = employee.email
      val body = s"Happy Birthday, dear ${employee.firstName}!"
      val subject = "Happy Birthday!"

      sendMessage(smtpHost,
                  smtpPort,
                  "sender@here.com",
                  subject,
                  body,
                  recipient)
    }
  }

  private def sendMessage(smtpHost: String,
                          smtpPort: Int,
                          sender: String,
                          subject: String,
                          body: String,
                          recipient: String): Unit = {
    // Create a mail session
    val props = new Properties
    props.put("mail.smtp.host", smtpHost)
    props.put("mail.smtp.port", "" + smtpPort)
    val session = Session.getInstance(props, null)

    // Construct the message
    val msg = new MimeMessage(session)
    msg.setFrom(new InternetAddress(sender))
    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient))
    msg.setSubject(subject)
    msg.setText(body);

    // Send the message
    Transport.send(msg)
  }
}
