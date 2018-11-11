name := "dummy-policy-enforcement-engine"
organization := "com.ditas"
version := "1.0"

scalaVersion := "2.12.7"

resolvers += baseDirectory.value / .m2


libraryDependencies ++= Seq(
  "com.ditas" % "policy-enforcement-engine-interface" % "1.0",
  "org.apache.spark" % "spark-core_2.11" % "2.3.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.3.0"
)
