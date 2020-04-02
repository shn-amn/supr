package one.shn.zero.supr.core
import one.shn.zero.supr.core.Pathfinder.Direction.Direction

import scala.language.postfixOps

object Pathfinder {

  object Direction extends Enumeration {
    type Direction = ValueSet
    val Left, Right = values
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

  def minimalPath(triangle: Triangle) = resolve(triangle) min

}
