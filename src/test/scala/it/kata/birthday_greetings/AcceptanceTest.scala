package it.kata.birthday_greetings

import minitest._
import cats.effect.IO
import cats.data.EitherT

import Display._
import Repository._
import GreetingsNotification._
import BirthdayService._

object AcceptanceTest extends SimpleTestSuite {

  class StubRepository(es: List[Employee]) extends Repository {
    def loadEmployees(): EitherT[IO, Throwable, List[Employee]] = EitherT {
      IO.pure(es).attempt
    }
  }

  class StubErrorRepository(message: String) extends Repository {
    def loadEmployees(): EitherT[IO, Throwable, List[Employee]] = EitherT {
      IO.raiseError(new Exception(message)).attempt
    }
  }

  class MockGreetingsNotification extends GreetingsNotification {

    val receivers = new collection.mutable.ListBuffer[Employee]

    def sendMessage(e: Employee): EitherT[IO, Throwable, Unit] = EitherT {
      receivers += e
      IO.unit.attempt
    }
  }

  class MockDisplay extends Display {

    var message = ""

    def printDone(): IO[Unit] = {
      message = "done"
      IO.unit
    }

    def printError(e: Throwable): IO[Unit] = {
      message = e.getMessage
      IO.unit
    }
  }

  test("will send greetings when its somebody's birthday") {
    val employee = Employee("aldo", "raine", "1900/10/08", "a@b.com")
    val stubRepository = new StubRepository(List(employee))
    val mockGreetingsNotification = new MockGreetingsNotification()
    val mockDisplay = new MockDisplay()
    val today = XDate("2008/10/08")

    val program =
      sendGreetings(stubRepository,
                    mockGreetingsNotification,
                    mockDisplay,
                    today)
    program.unsafeRunSync()

    assert(mockGreetingsNotification.receivers.size == 1, "message not sent?")
    assert(mockGreetingsNotification.receivers.contains(employee),
           "to wrong employee?")
    assert(mockDisplay.message == "done")
  }

  test("will not send emails when nobody's birthday") {
    val employee = Employee("aldo", "raine", "1900/10/08", "a@b.com")
    val stubRepository = new StubRepository(List(employee))
    val mockGreetingsNotification = new MockGreetingsNotification()
    val mockDisplay = new MockDisplay()
    val today = XDate("2008/01/01")

    val program =
      sendGreetings(stubRepository,
                    mockGreetingsNotification,
                    mockDisplay,
                    today)
    program.unsafeRunSync()

    assert(mockGreetingsNotification.receivers.size == 0, "what? messages?")
    assert(mockDisplay.message == "done")
  }

  test("missing employees source") {
    val stubRepository = new StubErrorRepository("very bad!")
    val mockGreetingsNotification = new MockGreetingsNotification()
    val mockDisplay = new MockDisplay()
    val today = XDate("2008/01/01")

    val program =
      sendGreetings(stubRepository,
                    mockGreetingsNotification,
                    mockDisplay,
                    today)
    program.unsafeRunSync()

    assert(mockGreetingsNotification.receivers.size == 0, "what? messages?")
    assertEquals("very bad!", mockDisplay.message)
  }
}
