package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}
import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

import cats.implicits._
import cats.data.Reader
import cats.data.ReaderT
import cats.effect.IO

case class Config(fileName: String, smtpHost: String, smtpPort: Int)

object BirthdayService {

  def sendGreetings(xDate: XDate): Reader[Config, Unit] = ???
  // loadEmployees()
  //   .map(es => es.filter(_.isBirthday(xDate)))
  //   .flatMap(es => sendAllGreetings(es))

  def loadEmployees(): ReaderT[IO, Config, List[Employee]] = ReaderT { config =>
    IO {
      val employees = new collection.mutable.ListBuffer[Employee]
      val in = new BufferedReader(new FileReader(config.fileName))
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
  }

  def sendAllGreetings(es: List[Employee]): Reader[Config, Unit] =
    es.traverse(e => sendGreetings(e)).map(_ => ())

  def sendGreetings(employee: Employee): Reader[Config, Unit] =
    Reader { config =>
      val session = buildSession(config.smtpHost, config.smtpPort)
      val msg = buildMessage(employee, session)
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
