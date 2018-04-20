package it.kata.birthday_greetings

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

import cats.effect.Sync
import fs2.Stream

object GreetingsNotification {

  trait GreetingsNotification[F[_]] {
    def sendMessage(e: Employee): F[Unit]

    def sendMessages(es: List[Employee]): F[Unit]
  }

  def buildSmtpGreetingsNotification[F[_]](smtpHost: String, smtpPort: Int)(
      implicit S: Sync[F]): GreetingsNotification[F] =
    new GreetingsNotification[F] {
      def sendMessage(employee: Employee): F[Unit] = S.delay {
        val session = buildSession(smtpHost, smtpPort)
        val msg = buildMessage(employee, session)
        Transport.send(msg)
      }

      def sendMessages(es: List[Employee]): F[Unit] =
        Stream.emits(es).flatMap(e => Stream.eval(sendMessage(e))).compile.drain
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
