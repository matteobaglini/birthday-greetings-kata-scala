package it.kata.birthday_greetings

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

import cats.implicits._
import cats.mtl._
import cats.mtl.implicits._
import cats.effect._

object GreetingsGateway {

  def apply[F[_]]()(implicit
                    MR: ApplicativeAsk[F, Config],
                    S: Sync[F]): GreetingsGateway[F] =
    new SmtpGreetingsGateway[F]()

  trait GreetingsGateway[F[_]] {
    def sendAll(es: List[Employee]): F[Unit]
    def send(e: Employee): F[Unit]
  }

  class SmtpGreetingsGateway[F[_]](implicit
                                   MR: ApplicativeAsk[F, Config],
                                   S: Sync[F])
      extends GreetingsGateway[F] {

    def sendAll(es: List[Employee]): F[Unit] =
      es.traverse_(e => send(e))

    def send(e: Employee): F[Unit] =
      for {
        config <- MR.ask
        _ <- sendMessage(config.smtpHost, config.smtpPort, e)
      } yield ()

    private def sendMessage(smtpHost: String,
                            smtpPort: Int,
                            e: Employee): F[Unit] = S.delay {
      val session = buildSession(smtpHost, smtpPort)
      val msg = buildMessage(e, session)
      Transport.send(msg)
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
}
