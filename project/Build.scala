import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "todolistMongo"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
       "org.mongodb" %% "casbah" % "2.6.0"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
