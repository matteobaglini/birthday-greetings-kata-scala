package it.kata.birthday_greetings

import cats.data._

import EmployeeRepository._
import GreetingsGateway._
import Display._

object TestSupport {

  case class TestState(loaded: List[Employee],
                       sent: List[Employee],
                       displayed: List[String])

  type Test[A] = State[TestState, A]
  type TestResult[A] = EitherT[Test, Throwable, A]

  class InMemoryEmployeeRepository() extends EmployeeRepository[TestResult] {

    def loadEmployees(): TestResult[List[Employee]] =
      EitherT.right(State.get.map(_.loaded))
  }

  class FaultedEmployeeRepository(errorMessage: String)
      extends EmployeeRepository[TestResult] {

    def loadEmployees(): TestResult[List[Employee]] =
      EitherT.leftT(new Exception(errorMessage))
  }

  class InMemoryGreetingsGateway extends GreetingsGateway[TestResult] {

    def send(e: Employee): TestResult[Unit] =
      EitherT.right(State.modify(s => s.copy(sent = s.sent :+ e)))
  }

  class FaultedGreetingsGateway(errorMessage: String)
      extends GreetingsGateway[TestResult] {

    def send(e: Employee): TestResult[Unit] =
      EitherT.leftT(new Exception(errorMessage))
  }

  class InMemoryDisplay extends Display[TestResult] {

    def printDone(): TestResult[Unit] =
      EitherT.right(State.modify(s => s.copy(displayed = s.displayed :+ "ok")))

    def printError(e: Throwable): TestResult[Unit] =
      EitherT.right(
        State.modify(s => s.copy(displayed = s.displayed :+ e.getMessage)))
  }
}
