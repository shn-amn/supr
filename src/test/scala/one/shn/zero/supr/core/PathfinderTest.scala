package one.shn.zero.supr.core

import one.shn.zero.supr.core.Pathfinder.{BasePointResolution, Direction}
import org.scalatest.wordspec.AnyWordSpec

import scala.language.postfixOps

class PathfinderTest extends AnyWordSpec {
  "Pathfinder" when {
    "given the example problem" should {
      val rows = Vector(7) :: Vector(6, 3) :: Vector(3, 8, 5) :: Vector(11, 2, 10, 9) :: Nil
      val exampleProblemEither =
        rows.foldLeft[Either[String, Triangle]](Right(Empty))((triangle, row) => triangle flatMap (_ ++ row))
      val givenSolution = BasePointResolution(Vector(Direction.Left, Direction.Left, Direction.Right), 18)
      "accept the problem as valid" in {
        assert(exampleProblemEither.isRight)
      }
      val exampleProblem = exampleProblemEither.toOption.get
      "reject a row with bad number of integers" in {
        assert(exampleProblem ++ Vector(1, 2, 3, 4) isLeft)
        assert(exampleProblem ++ Vector(1, 2, 3, 4, 5, 6) isLeft)
      }
      val minPath = Pathfinder minimalPath exampleProblem
      "find the given answer" in {
        assert(minPath == givenSolution)
      }
      "find the right indices for a path" in {
        assert(Pathfinder.pathToIndices(minPath.path) == Seq(0, 0, 0, 1))
      }
      "find the right values for some indices" in {
        assert(Pathfinder.indicesToValues(exampleProblem, Seq(0, 0, 0, 1)) == Seq(7, 6, 3, 2))
      }
      "be able to resolve a resolve a 500-row triangle" in {
        def unsafeAppend(triangle: Triangle, row: Vector[Int]): Triangle = (triangle ++ row).toOption.get
        val big = (1 to 1000 map (1 to _ toVector) foldLeft (Empty: Triangle))(unsafeAppend)
        assert(Pathfinder.minimalPath(big).sum == 1000)
      }
    }
  }
}
