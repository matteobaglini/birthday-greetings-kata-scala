package it.kata.birthday_greetings

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

import cats.implicits._
import cats.effect._

object BirthdayService {
  def sendGreetings(today: XDate)(employeeRepository: EmployeeRepository,
                                  smtpHost: String,
                                  smtpPort: Int): IO[Unit] = {

    for {
      loaded <- employeeRepository.loadEmployees()
      birthdays = haveBirthday(loaded, today)
      _ <- sendMessages(birthdays)(smtpHost, smtpPort)
    } yield ()
  }

  private def haveBirthday(loaded: List[Employee],
                           today: XDate): List[Employee] = {
    loaded.filter(employee => employee.isBirthday(today))
  }

  private def sendMessages(
      employees: List[Employee])(smtpHost: String, smtpPort: Int): IO[Unit] = {
    employees
      .traverse_ { employee =>
        sendMessage(employee)(smtpHost, smtpPort)
      }
  }

  private def sendMessage(employee: Employee)(smtpHost: String,
                                              smtpPort: Int): IO[Unit] = IO {
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
