import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(

    "uk.gov.hmrc"             %% "bootstrap-play-26"        % "1.4.0",
    "org.typelevel"           %% "cats-core"                % "1.6.1"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-play-26"        % "1.4.0",
    "org.scalatest"           %% "scalatest"                % "3.0.8",
    "com.typesafe.play"       %% "play-test"                % current,
    "org.pegdown"             %  "pegdown"                  % "1.6.0",
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "3.1.2",
    "org.scalacheck"          %% "scalacheck"               % "1.14.1",
    "org.mockito"             %  "mockito-all"              % "1.10.19",
    "com.github.tomakehurst"  % "wiremock-standalone"       % "2.17.0",
    "com.ironcorelabs"        %% "cats-scalatest"           % "2.4.0"
  ).map(_ % "test")
}
