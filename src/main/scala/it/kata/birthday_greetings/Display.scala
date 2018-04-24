package it.kata.birthday_greetings

import java.io.PrintStream
import Console.{RED, RESET}
import cats.effect.IO

object Display {

  trait Display {
    def printDone(): IO[Unit]
    def printError(e: Throwable): IO[Unit]
  }

  def buildConsoleDisplay(out: PrintStream): Display = new Display {

    def printDone(): IO[Unit] = IO {
      out.println("Done!")
    }

    def printError(e: Throwable): IO[Unit] = IO {
      out.println(s"${RED}Error: ${e.getMessage}${RESET}")
    }
  }
}
