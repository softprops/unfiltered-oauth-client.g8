import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val uf_version = "0.2.2-SNAPSHOT"
  
  // unfiltered
  lazy val uff = "net.databinder" %% "unfiltered-filter" % uf_version
  lazy val ufj = "net.databinder" %% "unfiltered-jetty" % uf_version
  
  val doa = "net.databinder" %% "dispatch-oauth" % "0.7.8-SNAPSHOT"
  
  // logging
  val javaNetRepo = "Java.net Repository for Maven" at "http://download.java.net/maven/2"
  val newReleaseToolsRepository = ScalaToolsSnapshots
  val avsl = "org.clapper" %% "avsl" % "0.3.1"
}