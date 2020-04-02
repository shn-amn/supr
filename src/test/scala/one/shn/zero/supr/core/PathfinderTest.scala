package one.shn.zero.supr.core

import cats.data.NonEmptyVector
import one.shn.zero.supr.core.Pathfinder.{BasePointResolution, Direction}
import one.shn.zero.supr.core.Triangle
import org.scalatest.wordspec.AnyWordSpec

class PathfinderTest extends AnyWordSpec {
  "Pathfinder" when {
    "given the example problem" should {
      "find the given answer." in {
        val exampleProblem =
          NonEmptyVector(11, Vector(2, 10, 9)) ::
          NonEmptyVector(3, Vector(8, 5)) ::
          NonEmptyVector(6, Vector(3)) ::
          NonEmptyVector(7, Vector.empty) ::
          Empty
        val givenSolution = BasePointResolution(Vector(Direction.Left, Direction.Left, Direction.Right), 18)
        assert(Pathfinder.minimalPath(exampleProblem) == givenSolution)
      }
    }
  }
}
