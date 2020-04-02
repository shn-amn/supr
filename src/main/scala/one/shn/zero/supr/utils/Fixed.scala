package one.shn.zero.supr.utils

import cats.{Applicative, Functor}
import shapeless._
import shapeless.HNil

sealed trait Fixed[+A]
case object Empty extends Fixed[Nothing]
case class Cons[+A, F <: Fixed[A]](a: A, f: F) extends Fixed[A]

object Fixed extends Functor[Fixed] {
  override def map[A, B](fa:  Fixed[A])(f:  A => B): Fixed[B] = fa match {
    case Empty => Empty
    case Cons(head, tail) => Cons(f(head), map(tail)(f))
  }
}

sealed trait Triangle extends Product with Serializable

case object Baseless extends Triangle

case class Based[T <: Triangle](peak: T, base: ) extends Triangle[Int :: A]



