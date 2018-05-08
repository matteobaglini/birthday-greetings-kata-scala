package it.kata.birthday_greetings

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

import cats.implicits._
import cats.effect._

object BirthdayService {
  def sendGreetings(fileName: String,
                    today: XDate,
                    smtpHost: String,
                    smtpPort: Int): IO[Unit] = {

    loadEmployees(fileName)
      .map(loaded => haveBirthday(loaded, today))
      .flatMap(birthdays => sendMessages(smtpHost, smtpPort, birthdays))
  }

  private def loadEmployees(fileName: String): IO[List[Employee]] = {
    loadLines(fileName).map { lines =>
      lines
        .drop(1) // skip header
        .map(parseEmployee(_))
    }
  }

  private def loadLines(fileName: String): IO[List[String]] =
    IO(io.Source.fromFile(fileName)).bracket { source =>
      IO(source.getLines.toList)
    } { source =>
      IO(source.close)
    }

  private def parseEmployee(line: String): Employee = {
    val employeeData = line.split(", ")
    Employee(employeeData(1), employeeData(0), employeeData(2), employeeData(3))
  }

  private def haveBirthday(loaded: List[Employee],
                           today: XDate): List[Employee] = {
    loaded.filter(employee => employee.isBirthday(today))
  }

  private def sendMessages(smtpHost: String,
                           smtpPort: Int,
                           employees: List[Employee]): IO[Unit] = {
    employees
      .traverse_ { employee =>
        sendMessage(smtpHost, smtpPort, employee)
      }
  }

  private def sendMessage(smtpHost: String,
                          smtpPort: Int,
                          employee: Employee): IO[Unit] = IO {
    val session = buildSession(smtpHost, smtpPort)
    val msg = buildMessage(session, employee)
    Transport.send(msg)
  }

  private def buildSession(smtpHost: String, smtpPort: Int): Session = {
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
    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient))
    msg.setSubject(subject)
    msg.setText(body);
    msg
  }
}
