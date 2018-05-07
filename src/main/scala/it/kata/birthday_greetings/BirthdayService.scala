package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}
import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

class BirthdayService {
  def sendGreetings(fileName: String,
                    today: XDate,
                    smtpHost: String,
                    smtpPort: Int): Unit = {
    val in = new BufferedReader(new FileReader(fileName))
    var str = ""
    str = in.readLine // skip header
    while ({ str = in.readLine; str != null }) {
      val employeeData = str.split(", ")
      val employee = Employee(employeeData(1),
                              employeeData(0),
                              employeeData(2),
                              employeeData(3))

      if (employee.isBirthday(today)) {
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
