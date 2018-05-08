package it.kata.birthday_greetings

import cats.implicits._
import cats.effect._

object FlatFileEmployeeRepository {

  def fromFile[F[_]](fileName: String)(
      implicit S: Sync[F]): EmployeeRepository[F] =
    new EmployeeRepository[F] {

      def loadEmployees(): F[List[Employee]] = {
        for {
          lines <- loadLines()
          employees = lines
            .drop(1) // skip header
            .map(parseEmployee(_))
        } yield employees
      }

      private def loadLines(): F[List[String]] =
        S.bracket(S.delay(io.Source.fromFile(fileName))) { source =>
          S.delay(source.getLines.toList)
        } { source =>
          S.delay(source.close)
        }

      private def parseEmployee(line: String): Employee = {
        val employeeData = line.split(", ")
        Employee(employeeData(1),
                 employeeData(0),
                 employeeData(2),
                 employeeData(3))
      }
    }
}
