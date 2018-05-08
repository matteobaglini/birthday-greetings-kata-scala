package it.kata.birthday_greetings

import cats.effect._

object FlatFileEmployeeRepository {

  def fromFile(fileName: String): EmployeeRepository = new EmployeeRepository {

    def loadEmployees(): IO[List[Employee]] = ???
  }
}
