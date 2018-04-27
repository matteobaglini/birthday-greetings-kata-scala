package it.kata.birthday_greetings

import BirthdayService._

import cats.data.ReaderT
import cats.effect.IO
import cats.mtl.implicits._

object Program {
  def main(args: Array[String]): Unit = {

    val config = Config("employee_data.txt", "localhost", 25)

    sendGreetings[Result](XDate())
      .run(config)
      .unsafeRunSync()
  }

  type Result[A] = ReaderT[IO, Config, A]
}
