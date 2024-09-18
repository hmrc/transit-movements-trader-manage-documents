import sbtscalaxb.ScalaxbPlugin.*
import scoverage.ScoverageKeys

val appName         = "transit-movements-trader-manage-documents"
val silencerVersion = "1.7.14"

lazy val RFC37 = "rfc37"
lazy val RFC37Config = config(RFC37) extend Compile

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin, ScalaxbPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    majorVersion := 0,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    dependencyOverrides ++= AppDependencies.overrides,
    excludeDependencies ++= AppDependencies.exclusions,
    ThisBuild / scalafmtOnCompile := true,
    ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*handlers.*;.*repositories.*;" +
      ".*BuildInfo.*;.*javascript.*;.*Routes.*;.*GuiceInjector;" +
      ".*ControllerConfiguration;.*LanguageSwitchController",
    ScoverageKeys.coverageExcludedPackages := ".*views.xml.*;scalaxb.*;generated.*",
    ScoverageKeys.coverageMinimumStmtTotal := 75,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
  .settings(scalaVersion := "3.5.0")
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(inConfig(Test)(testSettings) *)
  .settings(PlayKeys.playDefaultPort := 9484)
  .settings(scalacOptions := Seq("-Wconf:src=routes/.*:s", "-Wconf:src=src_managed/.*:s"))
  .settings(customScalaxbSettings *)

lazy val testSettings: Seq[Def.Setting[?]] = Seq(
  fork := true,
  javaOptions ++= Seq(
    "-Dconfig.resource=test.application.conf",
    "-Dlogger.resource=logback-test.xml"
  )
)

def customScalaxbSettings: Seq[Def.Setting[?]] =
  inConfig(RFC37Config)(baseScalaxbSettings ++ inTask(scalaxb)(customScalaxbSettingsFor(RFC37))) ++
    Seq(
      Compile / sourceGenerators += (RFC37Config / scalaxb).taskValue
    )

def customScalaxbSettingsFor(base: String): Seq[Def.Setting[?]] = Seq(
  sourceManaged := (Compile / sourceManaged).value,
  scalaxbXsdSource := new File(s"./conf/xsd/$base"),
  scalaxbDispatchVersion := "1.1.3",
  scalaxbPackageName := s"generated.$base"
)
