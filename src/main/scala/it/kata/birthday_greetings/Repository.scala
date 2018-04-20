package it.kata.birthday_greetings

import java.nio.file.Paths
import cats.effect.IO
import fs2.{io, text}

object Repository {

  trait EmployeeRepository[F[_]] {
    def loadEmployees(): F[List[Employee]]
  }

  def buildFileRepositoy(fileName: String): EmployeeRepository[IO] =
    new EmployeeRepository[IO] {
      def loadEmployees(): IO[List[Employee]] =
        io.file
          .readAll[IO](Paths.get(fileName), 4096)
          .through(text.utf8Decode)
          .through(text.lines)
          .drop(1) // skip header
          .map(line => line.split(", "))
          .map(data => Employee(data(1), data(0), data(2), data(3)))
          .compile
          .toList
    }
}
