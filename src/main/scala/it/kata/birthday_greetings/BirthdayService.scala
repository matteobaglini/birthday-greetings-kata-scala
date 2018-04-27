package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}
import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

import cats._
import cats.implicits._
import cats.mtl._
import cats.mtl.implicits._
import cats.effect._

import EmployeeRepository._
import GreetingsGateway._

case class Config(fileName: String, smtpHost: String, smtpPort: Int)

object EmployeeRepository {

  def apply[F[_]]()(implicit
                    MR: ApplicativeAsk[F, Config],
                    S: Sync[F]): EmployeeRepository[F] =
    new FlatFileEmployeeRepository[F]()

  trait EmployeeRepository[F[_]] {
    def loadEmployees(): F[List[Employee]]
  }

  class FlatFileEmployeeRepository[F[_]](implicit
                                         MR: ApplicativeAsk[F, Config],
                                         S: Sync[F])
      extends EmployeeRepository[F] {

    def loadEmployees(): F[List[Employee]] =
      for {
        config <- MR.ask
        lines <- loadLines(config.fileName)
        es = lines.map(l => parse(l))
      } yield es

    private def loadLines(fileName: String): F[List[String]] = S.delay {
      val lines = new collection.mutable.ListBuffer[String]
      val in = new BufferedReader(new FileReader(fileName))
      var str = in.readLine // skip header
      while ({ str = in.readLine; str != null }) {
        lines += str
      }
      lines.toList
    }

    private def parse(line: String): Employee = {
      val data = line.split(", ")
      Employee(data(1), data(0), data(2), data(3))
    }
  }

}

object GreetingsGateway {

  def apply[F[_]]()(implicit
                    MR: ApplicativeAsk[F, Config],
                    S: Sync[F]): GreetingsGateway[F] =
    new SmtpGreetingsGateway[F]()

  trait GreetingsGateway[F[_]] {
    def sendAllGreetings(es: List[Employee]): F[Unit]
    def send(e: Employee): F[Unit]
  }

  class SmtpGreetingsGateway[F[_]](implicit
                                   MR: ApplicativeAsk[F, Config],
                                   S: Sync[F])
      extends GreetingsGateway[F] {

    def sendAllGreetings(es: List[Employee]): F[Unit] =
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

object BirthdayService {

  def sendGreetings[F[_]](today: XDate)(implicit
                                        M: Monad[F],
                                        ER: EmployeeRepository[F],
                                        GG: GreetingsGateway[F]): F[Unit] = {
    for {
      es <- ER.loadEmployees()
      bs = filter(today, es)
      _ <- GG.sendAllGreetings(bs)
    } yield ()
  }

  private def filter(today: XDate, es: List[Employee]): List[Employee] =
    es.filter(_.isBirthday(today))
}
