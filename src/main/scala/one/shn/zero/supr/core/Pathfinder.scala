package one.shn.zero.supr.core
import one.shn.zero.supr.core.Pathfinder.Direction.Direction

import scala.language.postfixOps

object Pathfinder {

  object Direction extends Enumeration {
    type Direction = Value
    val Left, Right = Value
  }

  case class BasePointResolution(path: Vector[Direction], sum: Int)
  implicit val order: Ordering[BasePointResolution] = Ordering by (_.sum)

  def resolve(triangle: Triangle): Vector[BasePointResolution] = triangle match {
    case Empty => Vector.empty
    case Cons(base, peak) =>
      resolve(peak) match {

        case emptySolution if emptySolution.isEmpty => // peak is empty and the triangle base is a singleton
          Vector(BasePointResolution(Vector.empty, base.head))

        case singletonPeak if singletonPeak.length == 1 => // peak is a singleton and base has two elements
          val peakValue = singletonPeak.head.sum
          Vector(
            BasePointResolution(Vector(Direction.Left), base.head + peakValue),
            BasePointResolution(Vector(Direction.Right), base.last + peakValue)
          )

        case longerPeakSolution =>
          val midBase = base.tail dropRight 1
          val midSolution = longerPeakSolution sliding 2 zip midBase map {
            case (Vector(upperLeft, upperRight), value) if upperLeft.sum <= upperRight.sum =>
              BasePointResolution(upperLeft.path :+ Direction.Right, upperLeft.sum + value)
            case (Vector(_, upperRight), value) =>
              BasePointResolution(upperRight.path :+ Direction.Left, upperRight.sum + value)
          } toVector

          BasePointResolution(
            longerPeakSolution.head.path :+ Direction.Left,
            longerPeakSolution.head.sum + base.head
          ) +: midSolution :+ BasePointResolution(
            longerPeakSolution.last.path :+ Direction.Right,
            longerPeakSolution.head.sum + base.last
          )
      }
  }

  def minimalPath(triangle: Triangle): BasePointResolution = resolve(triangle).min

  def pathToIndices(path: Seq[Direction]): Seq[Int] = (path scanLeft 0) {
    case (index, Direction.Left)  => index
    case (index, Direction.Right) => index + 1
  }

  def indicesToValues(triangle: Triangle, indices: Seq[Int]): Seq[Int] = {
    val message = s"Length of indices (${indices.length}) and height of triangle (${triangle.height}) must be the same."
    assert(triangle.height == indices.length, new IllegalArgumentException(message))
    def inverseGo(triangle: Triangle, inverseIndices: List[Int]): List[Int] =
      (inverseIndices, triangle) match {
        case (head :: tail, Cons(row, peak)) => row.toVector(head) :: inverseGo(peak, tail)
        case (nil, Empty)                    => Nil
        case _                               => throw new IllegalStateException(message)
    }
    inverseGo(triangle, indices.reverse.toList).reverse
  }

  def pathToValues(triangle: Triangle, path: Seq[Direction]): Seq[Int] = indicesToValues(triangle, pathToIndices(path))

}
