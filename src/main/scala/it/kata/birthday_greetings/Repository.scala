package it.kata.birthday_greetings

import java.nio.file.Paths
import cats.effect.Sync
import fs2.{io, text}

object Repository {

  trait EmployeeRepository[F[_]] {
    def loadEmployees(): F[List[Employee]]
  }

  def buildFileRepositoy[F[_]](fileName: String)(
      implicit F: Sync[F]): EmployeeRepository[F] =
    new EmployeeRepository[F] {
      def loadEmployees(): F[List[Employee]] =
        io.file
          .readAll[F](Paths.get(fileName), 4096)
          .through(text.utf8Decode)
          .through(text.lines)
          .drop(1) // skip header
          .map(line => line.split(", "))
          .map(data => Employee(data(1), data(0), data(2), data(3)))
          .compile
          .toList
    }
}
