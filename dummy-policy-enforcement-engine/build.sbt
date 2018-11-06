name := "dummy-policy-enforcement-engine"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.7"


libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "2.3.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.3.0"
)
