package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}
import cats.effect.IO

object Repository {

  trait Repository {
    def loadEmployees(): IO[List[Employee]]
  }

  def buildFileRepositoy(fileName: String): Repository = new Repository {

    def loadEmployees(): IO[List[Employee]] =
      readLines(fileName)
        .map(_.map(_.split(", ")))
        .map(_.map(data => Employee(data(1), data(0), data(2), data(3))))

    private def readLines(fileName: String): IO[List[String]] = IO {
      val in = new BufferedReader(new FileReader(fileName))
      try {
        val lines = new collection.mutable.ListBuffer[String]
        var str = in.readLine // skip header
        while ({ str = in.readLine; str != null }) lines += str
        lines.toList
      } finally {
        in.close()
      }
    }
  }
}
