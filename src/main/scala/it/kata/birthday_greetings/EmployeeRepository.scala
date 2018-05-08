package it.kata.birthday_greetings

import cats.effect._

trait EmployeeRepository {

  def loadEmployees(): IO[List[Employee]]
}
