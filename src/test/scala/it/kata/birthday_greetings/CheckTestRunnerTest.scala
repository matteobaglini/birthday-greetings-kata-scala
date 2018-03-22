package it.kata.birthday_greetings

import minitest._

object CheckTestRunnerTest extends SimpleTestSuite {
  test("pass") {
    assertEquals(2, 1 + 1)
  }

  test("fail") {
    assertEquals(2, 1)
  }
}
