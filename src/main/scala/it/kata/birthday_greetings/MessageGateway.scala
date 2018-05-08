package it.kata.birthday_greetings

import cats._
import cats.implicits._

trait MessageGateway[F[_]] {

  def sendMessages(employees: List[Employee])(
      implicit A: Applicative[F]): F[Unit] =
    employees.traverse_(sendMessage(_))

  def sendMessage(employee: Employee): F[Unit]
}
