package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}
import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}
import collection.mutable.ListBuffer

object BirthdayService {
  def sendGreetings(fileName: String,
                    today: XDate,
                    smtpHost: String,
                    smtpPort: Int): Unit = {

    val loaded = loadEmployees(fileName)
    val birthdays = haveBirthday(loaded, today)

    for (employee <- birthdays) {
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

  private def loadEmployees(fileName: String): List[Employee] = {
    val employees = new ListBuffer[Employee]
    val in = new BufferedReader(new FileReader(fileName))
    var str = ""
    str = in.readLine // skip header
    while ({ str = in.readLine; str != null }) {
      val employeeData = str.split(", ")
      val employee = Employee(employeeData(1),
                              employeeData(0),
                              employeeData(2),
                              employeeData(3))

      employees += employee
    }
    employees.toList
  }

  private def haveBirthday(loaded: List[Employee],
                              today: XDate): List[Employee] = {
    val employees = new ListBuffer[Employee]
    for (employee <- loaded) {
      if (employee.isBirthday(today)) {
        employees += employee
      }
    }
    employees.toList
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
