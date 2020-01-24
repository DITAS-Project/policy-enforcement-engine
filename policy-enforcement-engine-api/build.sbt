name := """policy-enforcement-engine"""
organization := "com.ditas"
version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.12"





libraryDependencies ++= {
  val sparkVersion = "2.3.0"
  val hadoopVersion = "2.8.2"
  Seq(
    jdbc,
    ehcache,
    ws,
    "com.typesafe.play" %% "play-json" % "2.6.9",
    "org.webjars" % "bootstrap" % "4.0.0-2",
    "io.swagger" %% "swagger-play2" % "1.6.0",
    "org.webjars" %% "webjars-play" % "2.6.3",
    "org.webjars" % "swagger-ui" % "3.13.0",
    "com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B4",
    "org.codehaus.janino" % "janino" % "3.0.8",
    guice,
    "org.apache.hadoop" % "hadoop-client" % hadoopVersion excludeAll(
      ExclusionRule(organization = "org.apache.parquet", name = "parquet-hadoop"),
      ExclusionRule(organization = "org.apache.parquet", name = "parquet-format")),
    "org.apache.spark" % "spark-core_2.11" % sparkVersion  excludeAll(
      ExclusionRule(organization = "org.apache.parquet", name = "parquet-hadoop"),
      ExclusionRule(organization = "org.apache.parquet", name = "parquet-format"),
      ExclusionRule("org.apache.hadoop","hadoop-client")),
    "org.apache.spark" % "spark-sql_2.11" % sparkVersion excludeAll(
      ExclusionRule(organization = "org.apache.parquet", name = "parquet-hadoop"),
      ExclusionRule(organization = "org.apache.parquet", name = "parquet-format")),
    "org.apache.hadoop" % "hadoop-aws" % hadoopVersion,
    "com.amazonaws" % "aws-java-sdk-bundle" % "1.11.524",
    "mysql" % "mysql-connector-java" % "6.0.6",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    "javax.servlet" % "servlet-api" % "2.5",
      specs2 % Test
  )
}

dependencyOverrides ++= {
  Seq(
    "org.apache.parquet" % "parquet-hadoop" % "1.8.2",
    "org.apache.parquet" % "parquet-format" % "2.3.1"
  )
}


libraryDependencies ~= {
  _.map(_.exclude("org.slf4j", "slf4j-log4j12"))
}
libraryDependencies ~= {
  _.map(_.exclude("org.slf4j", "impl.SimpleLoggerFactory"))
}

resolvers += Resolver.sbtPluginRepo("releases")




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


