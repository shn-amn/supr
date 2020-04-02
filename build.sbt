name := "supr"
organization := "one.shn.zero"
version := "1.0"

scalaVersion := "2.13.1"

val cats = "org.typelevel" %% "cats-core" % "2.0.0"
val catsEffect = "org.typelevel" %% "cats-effect" % "2.1.2"
val fs2 = List("fs2-core", "fs2-io") map ("co.fs2" %% _ % "2.2.1")
val tests = "org.scalatest" %% "scalatest" % "3.1.1" % "test"

libraryDependencies ++= List(cats, catsEffect, tests) ++ fs2
