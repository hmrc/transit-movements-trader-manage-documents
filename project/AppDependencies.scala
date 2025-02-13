import sbt.*
import sbt.librarymanagement.InclExclRule

object AppDependencies {

  private val bootstrapVersion = "9.7.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-30"  % bootstrapVersion,
    "org.apache.xmlgraphics"  %  "fop"                        % "2.10",
    "net.sf.barcode4j"        %  "barcode4j"                  % "2.1",
    "net.sf.barcode4j"        %  "barcode4j-fop-ext"          % "2.1",
    "xerces"                  %  "xercesImpl"                 % "2.12.2",
    "javax.xml.bind"          %  "jaxb-api"                   % "2.3.1",
    "org.typelevel"           %% "cats-core"                  % "2.12.0"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"           %% "scalatest"                % "3.2.19",
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "7.0.1",
    "org.mockito"             %  "mockito-core"             % "5.11.0",
    "org.scalatestplus"       %% "mockito-4-11"             % "3.2.18.0",
    "org.scalatestplus"       %% "scalacheck-1-17"          % "3.2.18.0",
    "org.apache.pdfbox"       %  "pdfbox"                   % "2.0.30",
    "org.jsoup"               %  "jsoup"                    % "1.18.1",
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"   % bootstrapVersion
  ).map(_ % "test")

  val overrides: Seq[ModuleID] = Seq(
    "avalon-framework" % "avalon-framework-impl" % "4.3",
    "commons-io"       % "commons-io"            % "2.18.0"
  )

  val exclusions: Seq[InclExclRule] = Seq(
    ExclusionRule("servletapi", "servletapi"),
    ExclusionRule("xalan", "xalan"),
    ExclusionRule("xerces", "xerces"),
    ExclusionRule("xml-apis", "xml-apis")
  )
}
