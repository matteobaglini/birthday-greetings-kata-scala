package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}
import cats.effect.IO

object Repository {

  trait Repository {
    def loadEmployees(): IO[List[Employee]]
  }

  def buildFileRepositoy(fileName: String): Repository = new Repository {
    def loadEmployees(): IO[List[Employee]] = IO {
      val employees = new collection.mutable.ListBuffer[Employee]
      val in = new BufferedReader(new FileReader(fileName))
      var str = ""
      str = in.readLine // skip header
      while ({ str = in.readLine; str != null }) {
        val employeeData = str.split(", ")
        val employee = Employee(employeeData(1),
                                employeeData(0),
                                employeeData(2),
                                employeeData(3))
        employees += employee
      }
      employees.toList
    }
  }
}
