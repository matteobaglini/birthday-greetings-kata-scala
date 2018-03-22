package it.kata.birthday_greetings

case class Employee(firstName: String,
                    lastName: String,
                    birthDate: XDate,
                    email: String) {

  def isBirthday(today: XDate): Boolean = today.isSameDay(birthDate)
}

object Employee {
  def apply(firstName: String,
            lastName: String,
            birthDate: String,
            email: String): Employee =
    new Employee(firstName, lastName, XDate(birthDate), email)
}
