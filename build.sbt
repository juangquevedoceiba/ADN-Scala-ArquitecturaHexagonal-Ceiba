name := """adn-scala-superbanco"""
organization := "ceiba"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.5"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.28.0"
libraryDependencies += "javax.validation" % "validation-api" % "2.0.1.Final"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.6.1"
libraryDependencies += "io.monix" %% "monix" % "3.4.0"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "ceiba.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "ceiba.binders._"
