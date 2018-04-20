package it.kata.birthday_greetings

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

import cats._
import cats.implicits._
import cats.effect.IO

object GreetingsNotification {

  trait GreetingsNotification[F[_]] {
    def sendMessage(e: Employee): F[Unit]

    def sendMessages(es: List[Employee])(implicit A: Applicative[F]): F[Unit] =
      Traverse[List].traverse(es)(e => sendMessage(e)).map(_ => ())
  }

  def buildSmtpGreetingsNotification(smtpHost: String,
                                     smtpPort: Int): GreetingsNotification[IO] =
    new GreetingsNotification[IO] {
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
