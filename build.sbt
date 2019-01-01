
name := "homeDataIntegration"
organization := "de.softwareschmied"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.4"

resolvers += Resolver.mavenLocal

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.7"
libraryDependencies += "de.softwareschmied" %% "solarweb-interface" % "0.0.1-SNAPSHOT"
libraryDependencies += "de.softwareschmied" %% "myhomecontrol-interface" % "0.0.1-SNAPSHOT"
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.0.0" % "test")