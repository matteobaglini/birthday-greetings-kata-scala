package it.kata.birthday_greetings

import minitest._
import cats.data._
import cats.implicits._

import EmployeeRepository._
import GreetingsGateway._
import BirthdayService._

object AcceptanceTest extends SimpleTestSuite {

  case class TestState(loaded: List[Employee], sent: List[Employee])
  type Test[A] = State[TestState, A]

  class InMemoryEmployeeRepository() extends EmployeeRepository[Test] {

    def loadEmployees(): Test[List[Employee]] =
      State.get.map(_.loaded)
  }

  class InMemoryGreetingsGateway extends GreetingsGateway[Test] {

    def sendAll(es: List[Employee]): Test[Unit] =
      es.traverse_(e => send(e))

    def send(e: Employee): Test[Unit] =
      State.modify(s => s.copy(sent = s.sent :+ e))
  }

  implicit val repository = new InMemoryEmployeeRepository()
  implicit val gateway = new InMemoryGreetingsGateway()

  test("will send greetings when its somebody's birthday") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val init = TestState(List(john, mary), List())

    val result = sendGreetings[Test](XDate("2008/10/08"))
      .runS(init)
      .value

    assertEquals(result.sent.length, 1)
    assertEquals(john, result.sent(0))
  }

  test("will not send emails when nobody's birthday") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val init = TestState(List(john, mary), List())

    val result = sendGreetings[Test](XDate("2008/01/01"))
      .runS(init)
      .value

    assertEquals(result.sent.length, 0)
  }
}
