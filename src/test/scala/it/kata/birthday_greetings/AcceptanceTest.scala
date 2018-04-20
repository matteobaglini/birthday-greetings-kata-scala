package it.kata.birthday_greetings

import minitest._
import cats.effect.IO

import Repository._
import GreetingsNotification._
import BirthdayService._

object AcceptanceTest extends SimpleTestSuite {

  class StubRepository(es: List[Employee]) extends EmployeeRepository {
    def loadEmployees(): IO[List[Employee]] = IO {
      es
    }
  }

  class MockGreetingsNotification extends GreetingsNotification {

    val receivers = new collection.mutable.ListBuffer[Employee]

    def sendMessage(e: Employee): IO[Unit] = IO {
      receivers += e
    }
  }

  test("will send greetings when its somebody's birthday") {
    val employee = Employee("aldo", "raine", "1900/10/08", "a@b.com")
    val stubRepository = new StubRepository(List(employee))
    val mockGreetingsNotification = new MockGreetingsNotification()
    val today = XDate("2008/10/08")

    val program =
      sendGreetings(stubRepository, mockGreetingsNotification, today)
    program.unsafeRunSync()

    assert(mockGreetingsNotification.receivers.size == 1, "message not sent?")
    assert(mockGreetingsNotification.receivers.contains(employee),
           "to wrong employee?")
  }

  test("will not send emails when nobody's birthday") {
    val employee = Employee("aldo", "raine", "1900/10/08", "a@b.com")
    val stubRepository = new StubRepository(List(employee))
    val mockGreetingsNotification = new MockGreetingsNotification()
    val today = XDate("2008/01/01")

    val program =
      sendGreetings(stubRepository, mockGreetingsNotification, today)
    program.unsafeRunSync()

    assert(mockGreetingsNotification.receivers.size == 0, "what? messages?")
  }
}
