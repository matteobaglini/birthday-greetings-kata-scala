package it.kata.birthday_greetings

import minitest._
import cats.effect.IO

import Repository._
import GreetingsNotification._
import BirthdayService._

object AcceptanceTest extends SimpleTestSuite {

  class StubRepository(es: List[Employee]) extends Repository {
    def loadEmployees(): IO[List[Employee]] = IO {
      es
    }
  }

  test("will send greetings when its somebody's birthday") {
    val employee = Employee("aldo", "raine", "1900/10/08", "a@b.com")
    val stubRepository = new StubRepository(List(employee))
    val receivers = new collection.mutable.ListBuffer[Employee]
    val stubSendMessage: SendMessage = e => { receivers += e; IO.unit }
    val today = XDate("2008/10/08")

    val program = sendGreetings(stubRepository, stubSendMessage, today)
    program.unsafeRunSync()

    assert(receivers.size == 1, "message not sent?")
    assert(receivers.contains(employee), "to wrong employee?")
  }

  test("will not send emails when nobody's birthday") {
    val employee = Employee("aldo", "raine", "1900/10/08", "a@b.com")
    val stubRepository = new StubRepository(List(employee))
    val receivers = new collection.mutable.ListBuffer[Employee]
    val stubSendMessage: SendMessage = e => { receivers += e; IO.unit }
    val today = XDate("2008/01/01")

    val program = sendGreetings(stubRepository, stubSendMessage, today)
    program.unsafeRunSync()

    assert(receivers.size == 0, "what? messages?")
  }
}
