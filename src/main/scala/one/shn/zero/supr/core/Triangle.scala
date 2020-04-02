package one.shn.zero.supr.core

import cats.data.NonEmptyVector
import shapeless._

sealed trait Triangle[+R <: HList] extends Product with Serializable {
  def base: R
//  def ::(base: Int :: R): Cons[R] = Cons(base, this)
}

case object Empty extends Triangle[HNil] {
  override val base: HNil = HNil
}

case class Cons[R <: HList](base: Int :: R, peak: Triangle[R]) extends Triangle[Int :: R]
