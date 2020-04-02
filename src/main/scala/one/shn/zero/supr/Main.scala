package one.shn.zero.supr

import java.util.concurrent.{ExecutorService, Executors}

import cats.data.NonEmptyVector
import cats.effect.{Blocker, ExitCode, IO, IOApp}
import cats.implicits._
import fs2.{Stream, io, text}
import one.shn.zero.supr.core.{Empty, Pathfinder, Triangle}

import scala.language.postfixOps
import scala.util.Try

object Main extends IOApp {

  private def parseInt(str: String): Either[String, Int] =
    Try(str.toInt).toEither.left.map(_ => s"Value '$str' is not an integer.")

  private def input(blocker: Blocker): IO[Triangle] =
    io.stdin[IO](4096, blocker)
      .through(text.utf8Decode)
      .through(text.lines)
      .map(_.stripLineEnd split " +" toVector)
      .takeWhile(row => row.nonEmpty && row.head.nonEmpty)
      .evalScan[IO, Triangle](Empty) { (triangle, row) =>
        row traverse parseInt flatMap (triangle ++ _) match {
          case Left(error) => IO(println(s"$error Retry!")) as triangle
          case Right(newTriangle) => IO pure newTriangle
        }
      }
      .compile.last map (_.get)

  def composeMinimalPath(triangle: Triangle): String = {
    val minPath = Pathfinder minimalPath triangle
    val values = Pathfinder pathToValues (triangle, minPath.path)
    s"""Minimal path is: ${minPath.path mkString " >> "} (${values mkString " + "} = ${minPath.sum})."""
  }

  override def run(args: List[String]): IO[ExitCode] =
    Blocker fromExecutorService IO(Executors newFixedThreadPool 1) use { blocker =>
      IO(println("Enter triangle rows and an empty line to finish.")) *>
        input(blocker) flatMap (triangle => IO(println(composeMinimalPath(triangle))))
    } as ExitCode.Success

}