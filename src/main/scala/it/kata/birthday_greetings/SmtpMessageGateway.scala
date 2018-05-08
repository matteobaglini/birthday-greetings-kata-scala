package it.kata.birthday_greetings

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

import cats.effect._

object SmtpMessageGateway {

  def fromEndpoint(smtpHost: String, smtpPort: Int): MessageGateway =
    new MessageGateway {

      def sendMessage(employee: Employee): IO[Unit] = IO {
        val session = buildSession()
        val msg = buildMessage(session, employee)
        Transport.send(msg)
      }

      private def buildSession(): Session = {
        val props = new Properties
        props.put("mail.smtp.host", smtpHost)
        props.put("mail.smtp.port", "" + smtpPort)
        Session.getInstance(props, null)
      }

      private def buildMessage(session: Session,
                               employee: Employee): MimeMessage = {
        val recipient = employee.email
        val sender = "sender@here.com"
        val body = s"Happy Birthday, dear ${employee.firstName}!"
        val subject = "Happy Birthday!"

        val msg = new MimeMessage(session)
        msg.setFrom(new InternetAddress(sender))
        msg.setRecipient(Message.RecipientType.TO,
                         new InternetAddress(recipient))
        msg.setSubject(subject)
        msg.setText(body);
        msg
      }
    }
}
