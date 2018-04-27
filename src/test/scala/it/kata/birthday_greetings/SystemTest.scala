package it.kata.birthday_greetings

import java.io.{ByteArrayOutputStream, PrintStream}
import minitest._
import com.dumbster.smtp._
import cats.mtl.implicits._

import BirthdayService._
import Program._

object SystemTest extends TestSuite[SimpleSmtpServer] {
  private val NONSTANDARD_PORT = 555
  private val config =
    Config("employee_data.txt", "localhost", NONSTANDARD_PORT, Console.MAGENTA)

  implicit val employeeRepository = EmployeeRepository.file[Result]()
  implicit val greetingsGateway = GreetingsGateway.smtp[Result]()
  implicit val display =
    Display.stream[Result](new PrintStream(new ByteArrayOutputStream()))

  def setup(): SimpleSmtpServer =
    SimpleSmtpServer.start(NONSTANDARD_PORT)

  def tearDown(mailServer: SimpleSmtpServer): Unit =
    mailServer.stop()

  test("will send greetings when its somebody's birthday") { mailServer =>
    sendGreetings[Result](XDate("2008/10/08"))
      .run(config)
      .unsafeRunSync()

    assertEquals(mailServer.getReceivedEmailSize, 1)
    val message = mailServer.getReceivedEmail().next().asInstanceOf[SmtpMessage]
    assertEquals("Happy Birthday, dear John!", message.getBody)
    assertEquals("Happy Birthday!", message.getHeaderValue("Subject"))
    val recipients = message.getHeaderValues("To")
    assertEquals(1, recipients.length)
    assertEquals("john.doe@foobar.com", recipients(0).toString)
  }

  test("will not send emails when nobody's birthday") { mailServer =>
    sendGreetings[Result](XDate("2008/01/01"))
      .run(config)
      .unsafeRunSync()

    assert(mailServer.getReceivedEmailSize == 0, "what? messages?")
  }
}
