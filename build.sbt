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

mainClass in assembly := Some("play.core.server.ProdServerStart")
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

assemblyMergeStrategy in assembly := {
  case manifest if manifest.contains("MANIFEST.MF") =>
    // We don't need manifest files since sbt-assembly will create
    // one with the given settings
    MergeStrategy.discard
  case referenceOverrides if referenceOverrides.contains("reference-overrides.conf") =>
    // Keep the content for all reference-overrides.conf files
    MergeStrategy.concat
  case x =>
    // For all the other files, use the default sbt-assembly merge strategy
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}


enablePlugins(sbtdocker.DockerPlugin, JavaServerAppPackaging)



dockerfile in docker := {
    // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("java:8-jre-alpine")
    copy(artifact, artifactTargetPath)
    expose(9000)
    entryPoint("java", "-Dplay.http.secret.key='wspl4r'", "-jar", artifactTargetPath)
  }

}

