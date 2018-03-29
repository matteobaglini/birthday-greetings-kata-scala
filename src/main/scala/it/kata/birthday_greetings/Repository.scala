package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}

object Repository {

  def loadEmployees(fileName: String): List[Employee] = {
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
