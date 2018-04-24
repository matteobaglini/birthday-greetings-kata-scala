package it.kata.birthday_greetings

import cats.effect.IO

object Display {

  trait Display {
    def printDone(): IO[Unit]
    def printError(e: Throwable): IO[Unit]
  }

  def buildConsoleDisplay(): Display = new Display {

    def printDone(): IO[Unit] = IO {
      println("Done!")
    }

    def printError(e: Throwable): IO[Unit] = IO {
      println(Console.RED + s"Error: ${e.getMessage}" + Console.RESET)
    }
  }
}
