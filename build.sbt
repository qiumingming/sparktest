import sbt._

name := "helloworld"

organization := "6estates"

version := "1.0"

scalaVersion := "2.10.4"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-language:reflectiveCalls",
  "-unchecked", "-feature"
)

logLevel := Level.Error

resolvers ++= Seq(
  "nexus" at "https://repos.6estates.com/nexus/content/groups/public",
  "Cloudera repos" at "https://repository.cloudera.com/artifactory/cloudera-repos",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/releases/",
  "netLib" at "https://mvnrepository.com/artifact/com.github.fommil.netlib/netlib-native_system-linux-x86_64",
  "hortonworks" at "http://repo.hortonworks.com/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.5.2" % "provided" excludeAll ExclusionRule(organization = "javax.servlet"),
  "com.6estates" % "apiclient-filecenter" % "0.1.6"
)

publishMavenStyle := true

pomIncludeRepository := { x => false }

assemblyJarName in assembly := "6spark-" + version.value + ".jar"
test in assembly := {}

assemblyMergeStrategy in assembly := {
  //case PathList("org", "apache","http",xs @ _*)         => MergeStrategy.first
  case PathList("META-INF", xs@_*) =>

    (xs map {
      _.toLowerCase
    }) match {
      //case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) => MergeStrategy.discard
      case _ => MergeStrategy.discard
    }
  case _ => MergeStrategy.first
}

assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  cp filter {f=>{
    f.data.getName == "httpclient-4.1.3.jar" ||
      f.data.getName == "httpclient-4.3.jar" ||
      f.data.getName == "httpclient-4.2.6.jar" ||
      f.data.getName == "httpcore-4.2.5.jar" ||
      f.data.getName == "httpcore-4.3.jar" ||
      f.data.getName == "httpcore-4.2.4.jar" ||
      f.data.getName == "httpcore-4.3.1.jar" ||
      f.data.getName == "httpcore-4.4.5.jar" ||
      f.data.getName == "httpcore-4.4.4.jar" ||
      f.data.getName == "httpcore-4.4.1.jar"
  }}
}