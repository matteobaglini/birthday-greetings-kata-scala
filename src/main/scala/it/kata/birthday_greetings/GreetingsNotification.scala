package it.kata.birthday_greetings

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}
import cats.effect.IO

object GreetingsNotification {

  trait GreetingsNotification {
    def sendMessage(e: Employee): IO[Unit]
  }

  def buildSmtpGreetingsNotification(smtpHost: String,
                                     smtpPort: Int): GreetingsNotification =
    new GreetingsNotification {
      def sendMessage(employee: Employee): IO[Unit] = IO {
        val session = buildSession(smtpHost, smtpPort)
        val msg = buildMessage(employee, session)
        Transport.send(msg)
      }
    }

  private def buildSession(smtpHost: String, smtpPort: Int): Session = {
    val props = new Properties
    props.put("mail.smtp.host", smtpHost)
    props.put("mail.smtp.port", "" + smtpPort)
    Session.getInstance(props, null)
  }

  private def buildMessage(employee: Employee,
                           session: Session): MimeMessage = {
    val msg = new MimeMessage(session)
    msg.setFrom(new InternetAddress("sender@here.com"))
    msg.setRecipient(Message.RecipientType.TO,
                     new InternetAddress(employee.email))
    msg.setSubject("Happy Birthday!")
    msg.setText(s"Happy Birthday, dear ${employee.firstName}!");
    msg
  }
}
