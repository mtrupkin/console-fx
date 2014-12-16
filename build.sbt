name := """console-fx"""

version := "1.0"

scalaVersion := "2.11.4"

resolvers ++= Seq(
	Resolver.url("me.mtrupkin ivy repo", url("http://dl.bintray.com/mtrupkin/ivy/"))(Resolver.ivyStylePatterns)
)

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.6" % "test"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.4.0-M2"

libraryDependencies += "me.mtrupkin.console" %% "console-core" % "0.5-SNAPSHOT"

