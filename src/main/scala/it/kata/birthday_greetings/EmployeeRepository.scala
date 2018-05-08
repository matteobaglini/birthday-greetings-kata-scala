package it.kata.birthday_greetings

trait EmployeeRepository[F[_]] {

  def loadEmployees(): F[List[Employee]]
}
