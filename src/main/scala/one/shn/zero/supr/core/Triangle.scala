package one.shn.zero.supr.core

import cats.data.NonEmptyVector

sealed trait Triangle {

  def ++(base: Vector[Int]): Either[String, Cons] =
    if (base.length == this.height + 1) Right(Cons(NonEmptyVector(base.head, base.tail), this))
    else Left(s"Row size is ${base.length} where it should be ${this.height + 1}.")

  def height: Int

}

case object Empty extends Triangle {
  override val height: Int = 0
}

case class Cons(base: NonEmptyVector[Int], peak: Triangle) extends Triangle {
  override val height: Int = peak.height + 1
}
