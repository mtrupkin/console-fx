import bintray.Keys._

name := """console-fx"""

scalaVersion := "2.11.6"

organization := "me.mtrupkin.console"

licenses += ("MIT", url("http://www.opensource.org/licenses/mit-license.html"))

resolvers ++= Seq(
	Resolver.url("me.mtrupkin ivy repo", url("http://dl.bintray.com/mtrupkin/ivy/"))(Resolver.ivyStylePatterns)
)

libraryDependencies ++= Seq(
  "me.mtrupkin.console" %% "console-core" % "0.8-SNAPSHOT",
  "org.scalafx" %% "scalafx" % "8.0.20-R6",
  "org.scalafx" %% "scalafxml-core-sfx8" % "0.2.2")

bintraySettings

releaseSettings

publishMavenStyle := false

repository in bintray := "ivy"

bintrayOrganization in bintray := None