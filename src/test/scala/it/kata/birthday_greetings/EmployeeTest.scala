package it.kata.birthday_greetings

import minitest._

object EmployeeTest extends SimpleTestSuite {
  // NOTE: "equality" test is useless because Employee is a case class

  test("birthday") {
    val employee = Employee("foo", "bar", "1990/01/31", "a@b.c")
    assert(!employee.isBirthday(XDate("2008/01/30")), "not his birthday")
    assert(employee.isBirthday(XDate("2008/01/31")), "his birthday")
  }

}
