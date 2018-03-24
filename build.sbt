name := """policy-enforcement-engine"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= {
	val sparkVersion = "2.2.1"
 	Seq(
  		jdbc,
  		cache,
  		ws,
  		"org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  		"com.fasterxml.jackson.core" % "jackson-core" % "2.8.7",
  		"com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7",
  		"com.fasterxml.jackson.core" % "jackson-annotations" % "2.8.7",
  		"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.7",
                "org.apache.hadoop" % "hadoop-client" % "3.0.0",
  		"org.apache.spark" % "spark-core_2.11" % sparkVersion exclude("org.apache.hadoop","hadoop-client"),
  		"org.webjars" %% "webjars-play" % "2.5.0-1",
  		"org.webjars" % "bootstrap" % "3.3.6",
  		"org.apache.spark" % "spark-sql_2.11" % sparkVersion,
                "io.swagger" %% "swagger-play2" % "1.5.3",
                "org.webjars" %% "webjars-play" % "2.5.0-4",
                "org.webjars" % "swagger-ui" % "2.2.0",
  		"com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3"
	)
}
libraryDependencies += "org.codehaus.janino" % "janino" % "3.0.7"
libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-log4j12")) }

