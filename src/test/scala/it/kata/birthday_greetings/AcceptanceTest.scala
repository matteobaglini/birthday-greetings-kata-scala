package it.kata.birthday_greetings

import minitest._
import cats.effect.IO

import Repository._
import GreetingsNotification._
import BirthdayService._

object AcceptanceTest extends SimpleTestSuite {

  test("will send greetings when its somebody's birthday") {
    val employee = Employee("aldo", "raine", "1900/10/08", "a@b.com")
    val stubLoadEmployees: LoadEmployees = () => IO.pure(List(employee))

    val receivers = new collection.mutable.ListBuffer[Employee]
    val stubSendMessage: SendMessage = e => { receivers += e; IO.unit }
    val today = XDate("2008/10/08")

    sendGreetings(stubLoadEmployees, stubSendMessage, today)

    assert(receivers.size == 1, "message not sent?")
    assert(receivers.contains(employee), "to wrong employee?")
  }

  test("will not send emails when nobody's birthday") {
    val employee = Employee("aldo", "raine", "1900/10/08", "a@b.com")
    val stubLoadEmployees: LoadEmployees = () => IO.pure(List(employee))

    val receivers = new collection.mutable.ListBuffer[Employee]
    val stubSendMessage: SendMessage = e => { receivers += e; IO.unit }
    val today = XDate("2008/01/01")

    sendGreetings(stubLoadEmployees, stubSendMessage, today)

    assert(receivers.size == 0, "what? messages?")
  }
}
