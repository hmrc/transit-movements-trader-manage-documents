import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % "5.24.0",
    "org.typelevel"           %% "cats-core"                  % "1.6.1",
    "com.dmanchester"         %% "playfop"                    % "1.0",
    "net.sf.barcode4j"        %  "barcode4j"                  % "2.1",
    "net.sf.barcode4j"        %  "barcode4j-fop-ext"          % "2.1",
    "com.lucidchart"          %% "xtract"                     % "2.2.1"
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                % "3.2.9",
    "com.typesafe.play"       %% "play-test"                % current,
    "org.pegdown"             %  "pegdown"                  % "1.6.0",
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "5.1.0",
    "org.scalatestplus"       %% "mockito-3-2"              % "3.1.2.0",
    "org.scalatestplus"       %% "scalacheck-1-15"          % "3.2.9.0",
    "org.mockito"             % "mockito-core"              % "3.12.4",
    "com.github.tomakehurst"  %  "wiremock-standalone"      % "2.27.2",
    "com.ironcorelabs"        %% "cats-scalatest"           % "2.4.1" ,
    "org.apache.pdfbox"       %  "pdfbox"                   % "2.0.24",
    "org.jsoup"               % "jsoup"                     % "1.14.2",
    "com.vladsch.flexmark"    % "flexmark-all"              % "0.36.8"


  ).map(_ % "test")
}
