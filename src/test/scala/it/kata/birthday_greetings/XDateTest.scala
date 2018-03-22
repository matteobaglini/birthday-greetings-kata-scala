package it.kata.birthday_greetings

import minitest._

object XDateTest extends SimpleTestSuite {
  // NOTE: "getters" and "equality" tests are useless because XDate is a case class

  test("isSameDate") {
    val date = XDate("1789/01/24")
    val sameDay = XDate("2001/01/24")
    val notSameDay = XDate("1789/01/25")
    val notSameMonth = XDate("1789/02/25")

    assert(date.isSameDay(sameDay), "same")
    assert(!date.isSameDay(notSameDay), "not same day")
    assert(!date.isSameDay(notSameMonth), "not same month")
  }
}
