import play.core.PlayVersion.current
import sbt.*

object AppDependencies {

  private val catsVersion = "2.9.0"
  private val bootstrapVersion = "7.23.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % bootstrapVersion,
    "com.dmanchester"         %% "playfop"                    % "1.0",
    "net.sf.barcode4j"        %  "barcode4j"                  % "2.1",
    "net.sf.barcode4j"        %  "barcode4j-fop-ext"          % "2.1",
    "com.lucidchart"          %% "xtract"                     % "2.2.1"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"           %% "scalatest"                % "3.2.17",
    "com.typesafe.play"       %% "play-test"                % current,
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "5.1.0",
    "org.mockito"             %  "mockito-core"             % "5.2.0",
    "org.scalatestplus"       %% "mockito-4-11"             % "3.2.17.0",
    "org.scalatestplus"       %% "scalacheck-1-17"          % "3.2.17.0",
    "org.pegdown"             %  "pegdown"                  % "1.6.0",
    "com.github.tomakehurst"  %  "wiremock-standalone"      % "2.27.2",
    "com.ironcorelabs"        %% "cats-scalatest"           % "3.1.1" ,
    "org.apache.pdfbox"       %  "pdfbox"                   % "2.0.30",
    "org.jsoup"               %  "jsoup"                    % "1.15.4",
    "com.vladsch.flexmark"    %  "flexmark-all"             % "0.62.2",
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"   % bootstrapVersion
  ).map(_ % "test")

  val overrides: Seq[ModuleID] = Seq(
    "org.typelevel"          %% "cats-core"           % catsVersion,
    "org.typelevel"          %% "cats-kernel"         % catsVersion,
    "org.apache.xmlgraphics" %  "batik-bridge"        % "1.17"
  )
}
