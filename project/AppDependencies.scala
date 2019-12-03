import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(

    "uk.gov.hmrc"             %% "bootstrap-play-26"        % "1.2.0"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-play-26"        % "1.2.0",
    "org.scalatest"           %% "scalatest"                % "3.0.8",
    "com.typesafe.play"       %% "play-test"                % current,
    "org.pegdown"             %  "pegdown"                  % "1.6.0",
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "3.1.2",
    "org.scalacheck"          %% "scalacheck"               % "1.14.1"
  ).map(_ % "test")
}
