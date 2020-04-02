package one.shn.zero.supr.core
import one.shn.zero.supr.core.Pathfinder.Direction.Direction
import shapeless._

import scala.language.postfixOps

object Pathfinder {

  object Direction extends Enumeration {
    type Direction = Value
    val Left, Right = Value
  }

  case class BasePointResolution(path: Vector[Direction], sum: Int)
  implicit val order: Ordering[BasePointResolution] = Ordering by (_.sum)

  def resolve[R <: HList](triangle: Triangle[R]): R = triangle match {
    case Empty => HNil
    case Cons(base, Empty) => base
//    case Cons(base, Cons(v :: HNil, Empty)) => base.map(Poly())
  }

}
