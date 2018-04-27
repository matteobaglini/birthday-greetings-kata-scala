package it.kata.birthday_greetings

import minitest._
import cats.data._

import EmployeeRepository._
import GreetingsGateway._
import Display._
import BirthdayService._

object AcceptanceTest extends SimpleTestSuite {

  case class TestState(loaded: List[Employee],
                       sent: List[Employee],
                       displayed: List[String])
  type Test[A] = State[TestState, A]
  type TestResult[A] = EitherT[Test, Throwable, A]

  class InMemoryEmployeeRepository() extends EmployeeRepository[TestResult] {

    def loadEmployees(): TestResult[List[Employee]] =
      EitherT.right(State.get.map(_.loaded))
  }

  class InMemoryGreetingsGateway extends GreetingsGateway[TestResult] {

    def send(e: Employee): TestResult[Unit] =
      EitherT.right(State.modify(s => s.copy(sent = s.sent :+ e)))
  }

  class InMemoryDisplay extends Display[TestResult] {

    def printDone(): TestResult[Unit] =
      EitherT.right(
        State.modify(s => s.copy(displayed = s.displayed :+ "done")))

    def printError(e: Throwable): TestResult[Unit] =
      EitherT.right(
        State.modify(s => s.copy(displayed = s.displayed :+ e.getMessage)))
  }

  implicit val repository = new InMemoryEmployeeRepository()
  implicit val gateway = new InMemoryGreetingsGateway()
  implicit val display = new InMemoryDisplay()

  test("will send greetings when its somebody's birthday") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val init = TestState(List(john, mary), List(), List())

    val result =
      runTestResult(init, sendGreetings[TestResult](XDate("2008/10/08")))

    assertEquals(result.sent.length, 1)
    assertEquals(john, result.sent(0))
  }

  test("will not send emails when nobody's birthday") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val init = TestState(List(john, mary), List(), List())

    val result =
      runTestResult(init, sendGreetings[TestResult](XDate("2008/01/01")))

    assertEquals(result.sent.length, 0)
  }

  private def runTestResult[A](init: TestState, tr: TestResult[A]): TestState =
    tr.value.runS(init).value
}
