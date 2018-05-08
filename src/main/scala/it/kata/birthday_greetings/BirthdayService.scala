package it.kata.birthday_greetings

import cats._
import cats.implicits._

object BirthdayService {
  def sendGreetings[F[_]](today: XDate)(
      implicit M: Monad[F],
      employeeRepository: EmployeeRepository[F],
      messageGateway: MessageGateway[F]): F[Unit] = {

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
