package it.kata.birthday_greetings

import cats.effect._

object FlatFileEmployeeRepository {

  def fromFile(fileName: String): EmployeeRepository = new EmployeeRepository {

    def loadEmployees(): IO[List[Employee]] = {
      loadLines().map { lines =>
        lines
          .drop(1) // skip header
          .map(parseEmployee(_))
      }
    }

    private def loadLines(): IO[List[String]] =
      IO(io.Source.fromFile(fileName)).bracket { source =>
        IO(source.getLines.toList)
      } { source =>
        IO(source.close)
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
