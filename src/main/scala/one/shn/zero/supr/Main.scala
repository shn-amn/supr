package one.shn.zero.supr

import java.util.concurrent.{ExecutorService, Executors}

import cats.data.NonEmptyVector
import cats.effect.{Blocker, ExitCode, IO, IOApp}
import cats.implicits._
import fs2.{Stream, io}
import one.shn.zero.supr.core.{Empty, Pathfinder, Triangle}

import scala.language.postfixOps
import scala.util.Try

object Main extends IOApp {

  private def parseInt(str: String): Either[String, Int] =
    Try(str.toInt).toEither.left.map(_ => s"Value '$str' is not an integer.")

  private def input(blocker: Blocker): IO[Triangle] =
    io.stdinUtf8[IO](4096, blocker)
      .map(_.stripLineEnd split " +" toVector)
      .takeWhile(row => row.nonEmpty && row.head.nonEmpty)
      .evalScan[IO, Triangle](Empty) { (triangle, row) =>
        row traverse parseInt flatMap (triangle ++ _) match {
          case Left(error) => IO(println(error)) as triangle
          case Right(newTriangle) => IO pure newTriangle
        }
      }
//      .map(_ traverse parseInt)
//      .evalTap {
//        case Left(str) => IO(println(s"Value $str is not an integer."))
//        case _ => IO.pure((): Unit)
//      }
//      .mapFilter(_.toOption)
//      .map(someInts => NonEmptyVector(someInts.head, someInts.tail))
//      .evalScan[IO, Triangle](Empty) { (triangle, row) =>
//        row :: triangle match {
//          case Left(error) => IO(println(error)) as triangle
//          case Right(newTriangle) => IO pure newTriangle
//        }
//      }
//      .evalTap(x => IO(println(x)))
      .compile.last map (_.get)

  override def run(args: List[String]): IO[ExitCode] =
    Blocker fromExecutorService IO(Executors newFixedThreadPool 1) use { blocker =>
      input(blocker) flatMap (x => IO(println(Pathfinder minimalPath x)))
    } as ExitCode.Success

}