package it.kata.birthday_greetings

import cats.implicits._
import cats.effect._

trait MessageGateway {

  def sendMessages(employees: List[Employee]): IO[Unit] =
    employees.traverse_(sendMessage(_))

  def sendMessage(employee: Employee): IO[Unit]
}
