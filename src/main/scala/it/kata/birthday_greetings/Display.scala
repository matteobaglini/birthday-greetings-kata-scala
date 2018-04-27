package it.kata.birthday_greetings

import Console.RESET

import cats.implicits._
import cats.mtl._
import cats.mtl.implicits._
import cats.effect._

object Display {

  def apply[F[_]]()(implicit
                    MR: ApplicativeAsk[F, Config],
                    S: Sync[F]): Display[F] =
    new ConsoleDisplay[F]()

  trait Display[F[_]] {
    def printDone(): F[Unit]
    def printError(e: Throwable): F[Unit]
  }

  class ConsoleDisplay[F[_]](implicit
                             MR: ApplicativeAsk[F, Config],
                             S: Sync[F])
      extends Display[F] {

    def printDone(): F[Unit] =
      S.delay(println(s"Done! Good job man!${RESET}"))

    def printError(e: Throwable): F[Unit] =
      for {
        config <- MR.ask
        _ <- S.delay(
          println(s"${config.errorColor} Oh no! ${e.getMessage}.${RESET}"))
      } yield ()
  }
}
