package it.kata.birthday_greetings

object Domain {

  def hasBirthday(today: XDate, es: List[Employee]): List[Employee] =
    es.filter(_.isBirthday(today))
}
