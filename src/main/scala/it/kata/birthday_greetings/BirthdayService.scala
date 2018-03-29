package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}
import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}

object BirthdayService {
  def sendGreetings(fileName: String,
                    xDate: XDate,
                    smtpHost: String,
                    smtpPort: Int): Unit = {
    for {
      e <- loadEmployees(fileName)
      if (e.isBirthday(xDate))
    } sendGreetings(e, smtpHost, smtpPort)
  }

  def loadEmployees(fileName: String): List[Employee] = {
    val employees = new collection.mutable.ListBuffer[Employee]
    val in = new BufferedReader(new FileReader(fileName))
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

  def sendGreetings(employee: Employee,
                    smtpHost: String,
                    smtpPort: Int): Unit = {
    val session = buildSession(smtpHost, smtpPort)
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
