import sbt.*

object AppDependencies {

  private val bootstrapVersion = "8.3.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-30"  % bootstrapVersion,
    "org.apache.xmlgraphics"  %  "fop"                        % "2.9",
    "net.sf.barcode4j"        %  "barcode4j"                  % "2.1",
    "net.sf.barcode4j"        %  "barcode4j-fop-ext"          % "2.1",
    "com.lucidchart"          %% "xtract"                     % "2.3.0",
    "xerces"                  %  "xercesImpl"                 % "2.12.2"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"           %% "scalatest"                % "3.2.17",
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "5.1.0",
    "org.mockito"             %  "mockito-core"             % "5.2.0",
    "org.scalatestplus"       %% "mockito-4-11"             % "3.2.17.0",
    "org.scalatestplus"       %% "scalacheck-1-17"          % "3.2.17.0",
    "com.ironcorelabs"        %% "cats-scalatest"           % "3.1.1" ,
    "org.apache.pdfbox"       %  "pdfbox"                   % "2.0.30",
    "org.jsoup"               %  "jsoup"                    % "1.15.4",
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"   % bootstrapVersion
  ).map(_ % "test")

  val overrides: Seq[ModuleID] = Seq(
    "avalon-framework" % "avalon-framework-impl" % "4.3" exclude("servletapi", "servletapi")
  )
}
