import sbt._

class AdServerProject(info: ProjectInfo) extends DefaultWebProject(info) { 

  // Framework for unit testing
  val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"

  // Servlet
  val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided"

}
