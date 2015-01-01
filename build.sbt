name := """console-fx"""

version := "1.0"

scalaVersion := "2.11.4"

resolvers ++= Seq(
	Resolver.url("me.mtrupkin ivy repo", url("http://dl.bintray.com/mtrupkin/ivy/"))(Resolver.ivyStylePatterns)
)

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.6" % "test"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.4.0-M2"

libraryDependencies += "me.mtrupkin.console" %% "console-core" % "0.5"

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.20-R6"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

libraryDependencies += "org.scalafx" %% "scalafxml-core-sfx8" % "0.2.2"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2"

