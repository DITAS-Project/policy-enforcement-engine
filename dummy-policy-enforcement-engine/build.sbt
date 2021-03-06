name := "dummy-policy-enforcement-engine"
organization := "com.ditas"
version := "1.0"

scalaVersion := "2.11.12"


resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "2.3.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.3.0"
)
