package it.kata.birthday_greetings

import java.io.{BufferedReader, FileReader}

import cats.implicits._
import cats.mtl._
import cats.mtl.implicits._
import cats.effect._

object EmployeeRepository {

  def apply[F[_]]()(implicit
                    MR: ApplicativeAsk[F, Config],
                    S: Sync[F]): EmployeeRepository[F] =
    new FlatFileEmployeeRepository[F]()

  trait EmployeeRepository[F[_]] {
    def loadEmployees(): F[List[Employee]]
  }

  class FlatFileEmployeeRepository[F[_]](implicit
                                         MR: ApplicativeAsk[F, Config],
                                         S: Sync[F])
      extends EmployeeRepository[F] {

    def loadEmployees(): F[List[Employee]] =
      for {
        config <- MR.ask
        lines <- loadLines(config.fileName)
        es = lines.map(l => parse(l))
      } yield es

    private def loadLines(fileName: String): F[List[String]] = S.delay {
      val lines = new collection.mutable.ListBuffer[String]
      val in = new BufferedReader(new FileReader(fileName))
      var str = in.readLine // skip header
      while ({ str = in.readLine; str != null }) {
        lines += str
      }
      lines.toList
    }

    private def parse(line: String): Employee = {
      val data = line.split(", ")
      Employee(data(1), data(0), data(2), data(3))
    }
  }

}
