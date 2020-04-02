package one.shn.zero.supr.core

import cats.data.NonEmptyVector

sealed trait Triangle {
  def ::(base: NonEmptyVector[Int]): Cons = Cons(base, this)
}

case object Empty extends Triangle
case class Cons(base: NonEmptyVector[Int], peak: Triangle) extends Triangle
