package it.kata.birthday_greetings

import minitest._
import cats.data._

import BirthdayService._

object AcceptanceTest extends SimpleTestSuite {

  case class TestData(loaded: List[Employee],
                      receivers: List[Employee] = List())

  type TestResult[A] = State[TestData, A]

  class InMemoryEmployeeRepository() extends EmployeeRepository[TestResult] {

    def loadEmployees(): TestResult[List[Employee]] =
      State.get.map(_.loaded)
  }

  class InMemoryMessageGateway extends MessageGateway[TestResult] {

    def sendMessage(e: Employee): TestResult[Unit] =
      State.modify(s => s.copy(receivers = s.receivers :+ e))
  }

  implicit val employeeRepository = new InMemoryEmployeeRepository()
  implicit val messageGateway = new InMemoryMessageGateway()

  test("will send greetings when its somebody's birthday") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val employees = TestData(List(john, mary))
    val today = XDate("2008/10/08")

    val result = sendGreetings[TestResult](today)
      .runS(employees)
      .value

    assert(result.receivers.size == 1, "message not sent?")
    assert(result.receivers.contains(john), "to wrong employee?")
  }

  test("will not send emails when nobody's birthday") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val employees = TestData(List(john, mary))
    val today = XDate("2008/01/01")

    val result = sendGreetings[TestResult](today)
      .runS(employees)
      .value

    assert(result.receivers.size == 0, "what? messages?")
  }
}
