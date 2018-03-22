package it.kata.birthday_greetings

object Program {
  def main(args: Array[String]): Unit = {
    val service = new BirthdayService
    service.sendGreetings("employee_data.txt", XDate(), "localhost", 25)
  }
}
