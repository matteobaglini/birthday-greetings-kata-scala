package it.kata.birthday_greetings

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

import cats.instances.list._
import cats.syntax.traverse._
import cats.effect.IO
import cats.data.EitherT

object GreetingsNotification {

  trait GreetingsNotification {
    def sendMessage(e: Employee): EitherT[IO, Throwable, Unit]

    def sendMessages(es: List[Employee]): EitherT[IO, Throwable, Unit] =
      es.traverse(e => sendMessage(e)).map(_ => ())
  }

  def buildSmtpGreetingsNotification(smtpHost: String,
                                     smtpPort: Int): GreetingsNotification =
    new GreetingsNotification {
      def sendMessage(employee: Employee): EitherT[IO, Throwable, Unit] =
        EitherT {
          IO {
            val session = buildSession(smtpHost, smtpPort)
            val msg = buildMessage(employee, session)
            Transport.send(msg)
          }.attempt
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
