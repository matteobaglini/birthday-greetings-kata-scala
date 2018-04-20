package it.kata.birthday_greetings

import minitest._
import cats.Id

import Repository._
import GreetingsNotification._
import BirthdayService._

object AcceptanceTest extends SimpleTestSuite {

  class StubRepository(es: List[Employee]) extends EmployeeRepository[Id] {
    def loadEmployees(): List[Employee] =
      es
  }

  class MockGreetingsNotification extends GreetingsNotification[Id] {

    val receivers = new collection.mutable.ListBuffer[Employee]

    def sendMessage(e: Employee): Unit =
      receivers += e

    def sendMessages(es: List[Employee]): Unit =
      receivers ++= es
  }

  test("will send greetings when its somebody's birthday") {
    val employee = Employee("aldo", "raine", "1900/10/08", "a@b.com")
    implicit val stubRepository = new StubRepository(List(employee))
    implicit val mockGreetingsNotification = new MockGreetingsNotification()
    val today = XDate("2008/10/08")

    sendGreetings[Id](today)

    assert(mockGreetingsNotification.receivers.size == 1, "message not sent?")
    assert(mockGreetingsNotification.receivers.contains(employee),
           "to wrong employee?")
  }

  test("will not send emails when nobody's birthday") {
    val employee = Employee("aldo", "raine", "1900/10/08", "a@b.com")
    implicit val stubRepository = new StubRepository(List(employee))
    implicit val mockGreetingsNotification = new MockGreetingsNotification()
    val today = XDate("2008/01/01")

    sendGreetings[Id](today)

    assert(mockGreetingsNotification.receivers.size == 0, "what? messages?")
  }
}
