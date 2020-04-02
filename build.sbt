name := "supr"
organization := "one.shn.zero"
version := "0.1"

scalaVersion := "2.13.1"

val cats = List("cats-core") map ("org.typelevel" %% _ % "2.0.0")
val tests = List("scalatest") map ("org.scalatest" %% _ % "3.1.1" % "test")

libraryDependencies ++= cats ++ tests
