# supr
A programme to find a minimal-sum path in a valued triangle.

## Usage
In project path, execute `sbt run`. 
Of course, you could also use `cat << EOF | sbt run` instead.

## Algorithmic complexity
The programme runs in **O**(_n_) for _n_ being the total number of nodes.
This is **O(_h²_) for _h_ the height (number of rows) of the triangle.
It is the theoretical limit of complexity, 
for the simple reason that all nodes must be visited.

## Shortcomings
I am not happy with the data structure I used to represent the triangle.
A simplified version of my data type is the following:
```scala
sealed trait Triangle
case object Empty extends Triangle
case class Cons(base: NonEmptyVector[Int], peak: Triangle) extends Triangle
```
The problem with this representation is that it does not reflect the fact that 
the length of `base` array is equal to the height of the `triangle` in a `Cons`.

Of course, this requirement can be enforced by the likes of `Either`s or exceptions
(in the actual version, I used `Either`s).
However, these are _runtime_ solutions, in the sense that
if you append `NonEmptyVector.of(1, 2, 3)` to `Empty`, your code will still compile,
albeit at runtime you have a more or less nice error message.

It would be much nicer to enforce the constraint of base size on compile time.
Some functional languages, like Idris, support _dependent types_,
types that are specified by a value.
If Scala supported dependant types, 
there could have been a solution like this:
```scala
sealed trait Triangle
case object Triangle[0] extends Triangle
case class Triangle[n + 1](base: FixedLengthVector[n + 1][Int], peak: Triangle[n]) extends Triangle
```
But of course, Scala is not _that_ advanced!

I did try to do implement some sort of fixed-length list using Shapeless `HList`s, 
but then I noticed that I do not have much time and abandoned this.
I am still confident that this must be possible, 
with enough time and more proficiency in Shapeless than I have.

I welcome any suggestions!