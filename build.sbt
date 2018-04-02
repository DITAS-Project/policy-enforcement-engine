name := """policy-enforcement-engine"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= {
 	Seq(
  		jdbc,
  		ehcache,
  		ws,
  		"org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
                "com.typesafe.play" %% "play-json" % "2.6.9",
  		"org.webjars" % "bootstrap" % "4.0.0-2",
                "io.swagger" %% "swagger-play2" % "1.6.0",
                "org.webjars" %% "webjars-play" % "2.6.3",
                "org.webjars" % "swagger-ui" % "3.13.0",
  		"com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B4",
                "org.codehaus.janino" % "janino" % "3.0.8",
                guice
	)
}
libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-log4j12")) }

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
