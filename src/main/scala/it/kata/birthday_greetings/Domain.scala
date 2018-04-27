package it.kata.birthday_greetings

object Domain {

  def filter(today: XDate, es: List[Employee]): List[Employee] =
    es.filter(_.isBirthday(today))
}
