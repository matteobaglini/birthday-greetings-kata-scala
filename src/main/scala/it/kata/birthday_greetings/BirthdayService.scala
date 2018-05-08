package it.kata.birthday_greetings

import cats.effect._

object BirthdayService {
  def sendGreetings(today: XDate)(
      implicit employeeRepository: EmployeeRepository[IO],
      messageGateway: MessageGateway[IO]): IO[Unit] = {

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
