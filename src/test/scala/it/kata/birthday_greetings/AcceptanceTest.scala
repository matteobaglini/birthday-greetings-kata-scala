package it.kata.birthday_greetings

import minitest._
import TestSupport._
import BirthdayService._

object AcceptanceTest extends SimpleTestSuite {

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
    assertEquals("ok", result.displayed.head)
  }

  test("will not send emails when nobody's birthday") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val init = TestState(List(john, mary), List(), List())

    val result =
      runTestResult(init, sendGreetings[TestResult](XDate("2008/01/01")))

    assertEquals(result.sent.length, 0)
    assertEquals("ok", result.displayed.head)
  }

  test("error on laoding enployees") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val init = TestState(List(john, mary), List(), List())

    implicit val repository = new FaultedEmployeeRepository("loading...boom!")
    val result =
      runTestResult(init, sendGreetings[TestResult](XDate("2008/10/08")))

    assertEquals(result.sent.length, 0)
    assertEquals("loading...boom!", result.displayed.head)
  }

  test("error on sending greetings") {
    val john = Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com")
    val mary = Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com")
    val init = TestState(List(john, mary), List(), List())

    implicit val gateway = new FaultedGreetingsGateway("sending...boom!")
    val result =
      runTestResult(init, sendGreetings[TestResult](XDate("2008/10/08")))

    assertEquals(result.sent.length, 0)
    assertEquals("sending...boom!", result.displayed.head)
  }

  private def runTestResult[A](init: TestState, tr: TestResult[A]): TestState =
    tr.value.runS(init).value
}
