package it.kata.birthday_greetings

import minitest._
import com.dumbster.smtp._

import BirthdayService._

object SystemTest extends TestSuite[SimpleSmtpServer] {
  private val NONSTANDARD_PORT = 555

  def setup(): SimpleSmtpServer = {
    SimpleSmtpServer.start(NONSTANDARD_PORT)
  }

  def tearDown(mailServer: SimpleSmtpServer): Unit = {
    mailServer.stop()
  }

  test("will send greetings when its somebody's birthday") { mailServer =>
    implicit val employeeRepository =
      FlatFileEmployeeRepository.fromFile("employee_data.txt")
    implicit val messageGateway =
      SmtpMessageGateway.fromEndpoint("localhost", NONSTANDARD_PORT)

    sendGreetings(XDate("2008/10/08"))
      .unsafeRunSync()

    assert(mailServer.getReceivedEmailSize == 1, "message not sent?")
    val message = mailServer.getReceivedEmail().next().asInstanceOf[SmtpMessage]
    assertEquals("Happy Birthday, dear John!", message.getBody)
    assertEquals("Happy Birthday!", message.getHeaderValue("Subject"))
    val recipients = message.getHeaderValues("To")
    assertEquals(1, recipients.length)
    assertEquals("john.doe@foobar.com", recipients(0).toString)
  }

  test("will not send emails when nobody's birthday") { mailServer =>
    implicit val employeeRepository =
      FlatFileEmployeeRepository.fromFile("employee_data.txt")
    implicit val messageGateway =
      SmtpMessageGateway.fromEndpoint("localhost", NONSTANDARD_PORT)

    sendGreetings(XDate("2008/01/01"))
      .unsafeRunSync()

    assert(mailServer.getReceivedEmailSize == 0, "what? messages?")
  }
}