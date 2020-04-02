# supr
## A triangular pathfinder

This is the first solution that I could think of, 
not particularly functional, but seems to work, 
at least for the given example.
I/O is not yet implemented.

The biggest problem of this solution is that its minimal use of the type system. 
Everywhere the right size of runtime vectors is simply assumed, 
rather than being enforced at compile-time.

Also, the recursive solution is not tail-recursive,
which can lead to stack overflows for bigger input.
