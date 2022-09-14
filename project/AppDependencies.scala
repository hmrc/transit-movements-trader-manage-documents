import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  private val catsVersion = "2.8.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % "7.3.0",
    "com.dmanchester"         %% "playfop"                    % "1.0",
    "com.lucidchart"          %% "xtract"                     % "2.0.1"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"           %% "scalatest"                % "3.2.12",
    "com.typesafe.play"       %% "play-test"                % current,
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "5.1.0",
    "org.mockito"             %  "mockito-core"             % "4.8.0",
    "org.scalatestplus"       %% "mockito-4-5"              % "3.2.12.0",
    "org.scalatestplus"       %% "scalacheck-1-16"          % "3.2.12.0",
    "org.pegdown"             %  "pegdown"                  % "1.6.0",
    "com.github.tomakehurst"  %  "wiremock-standalone"      % "2.27.2",
    "com.ironcorelabs"        %% "cats-scalatest"           % "3.1.1" ,
    "org.apache.pdfbox"       %  "pdfbox"                   % "2.0.26",
    "org.jsoup"               %  "jsoup"                    % "1.15.3",
    "com.vladsch.flexmark"    %  "flexmark-all"             % "0.62.2"
  ).map(_ % "test")

  val overrides: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-kernel" % catsVersion
  )
}
