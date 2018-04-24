package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}
import cats.effect.IO

object Repository {

  trait Repository {
    def loadEmployees(): IO[List[Employee]]
  }

  def buildFileRepositoy(fileName: String): Repository = new Repository {
    def loadEmployees(): IO[List[Employee]] = IO {
      val in = new BufferedReader(new FileReader(fileName))

      val lines = new collection.mutable.ListBuffer[String]
      var str = ""
      str = in.readLine // skip header
      while ({ str = in.readLine; str != null }) lines += str

      lines
        .map(_.split(", "))
        .map(data => Employee(data(1), data(0), data(2), data(3)))
        .toList
    }
  }
}
