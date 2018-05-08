package it.kata.birthday_greetings

import cats.effect._

object BirthdayService {
  def sendGreetings(today: XDate)(employeeRepository: EmployeeRepository,
                                  messageGateway: MessageGateway): IO[Unit] = {

    for {
      loaded <- employeeRepository.loadEmployees()
      birthdays = haveBirthday(loaded, today)
      _ <- messageGateway.sendMessages(birthdays)
    } yield ()
  }

  private def haveBirthday(loaded: List[Employee],
                           today: XDate): List[Employee] = {
    loaded.filter(employee => employee.isBirthday(today))
  }
}
