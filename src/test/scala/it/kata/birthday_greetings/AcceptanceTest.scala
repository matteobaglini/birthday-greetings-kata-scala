package it.kata.birthday_greetings

import minitest._
import cats.effect.IO

import BirthdayService._

object AcceptanceTest extends SimpleTestSuite {

  class FakeEmployeeRepository(es: List[Employee]) extends EmployeeRepository {
    def loadEmployees(): IO[List[Employee]] = IO {
      es
    }
  }

  class FakeMessageGateway extends MessageGateway {

    val receivers = new collection.mutable.ListBuffer[Employee]

    def sendMessage(e: Employee): IO[Unit] = IO {
      receivers += e
    }
  }

  test("will send greetings when its somebody's birthday") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val employees = List(john, mary)
    val today = XDate("2008/10/08")

    implicit val fakeEmployeeRepository = new FakeEmployeeRepository(employees)
    implicit val fakeMessageGateway = new FakeMessageGateway()

    sendGreetings(today)
      .unsafeRunSync()

    assert(fakeMessageGateway.receivers.size == 1, "message not sent?")
    assert(fakeMessageGateway.receivers.contains(john), "to wrong employee?")
  }

  test("will not send emails when nobody's birthday") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val employees = List(john, mary)
    val today = XDate("2008/01/01")

    implicit val fakeEmployeeRepository = new FakeEmployeeRepository(employees)
    implicit val fakeMessageGateway = new FakeMessageGateway()

    sendGreetings(today)
      .unsafeRunSync()

    assert(fakeMessageGateway.receivers.size == 0, "what? messages?")
  }
}
