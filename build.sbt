name := """policy-enforcement-engine"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= {
 	Seq(
  		jdbc,
  		cache,
  		ws,
  		"org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  		"com.fasterxml.jackson.core" % "jackson-core" % "2.9.4",
  		"com.fasterxml.jackson.core" % "jackson-databind" % "2.9.4",
  		"com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.4",
  		"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.4",  		
  		"org.webjars" % "bootstrap" % "4.0.0-2",
        "io.swagger" %% "swagger-play2" % "1.6.0",
        "org.webjars" %% "webjars-play" % "2.6.3",
        "org.webjars" % "swagger-ui" % "3.13.0",
  		"com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B4"
	)
}
libraryDependencies += "org.codehaus.janino" % "janino" % "3.0.8"
libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-log4j12")) }
libraryDependencies += guice

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
