package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}
import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

import cats.data.Reader

case class Config(fileName: String, smtpHost: String, smtpPort: Int)

object BirthdayService {

  def sendGreetings(config: Config, xDate: XDate): Reader[Config, Unit] =
    Reader { config =>
      loadEmployees(config)
        .run(config)
        .filter(_.isBirthday(xDate))
        .map(sendGreetings(config, _).run(config))
    }

  def loadEmployees(config: Config): Reader[Config, List[Employee]] = Reader {
    config =>
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

  def sendGreetings(config: Config, employee: Employee): Reader[Config, Unit] =
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
