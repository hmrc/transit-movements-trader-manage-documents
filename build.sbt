import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

val appName = "transit-movements-trader-manage-documents"

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "3.5.0"
ThisBuild / scalafmtOnCompile := true

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin, ScalaxbPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    dependencyOverrides ++= AppDependencies.overrides,
    excludeDependencies ++= AppDependencies.exclusions,
    ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*handlers.*;.*repositories.*;" +
      ".*BuildInfo.*;.*javascript.*;.*Routes.*;.*GuiceInjector;" +
      ".*ControllerConfiguration;.*LanguageSwitchController",
    ScoverageKeys.coverageExcludedPackages := ".*views.xml.*;scalaxb.*;generated.*",
    ScoverageKeys.coverageMinimumStmtTotal := 90,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
  .settings(PlayKeys.playDefaultPort := 9484)
  .settings(scalacOptions := Seq("-Wconf:src=routes/.*:s", "-Wconf:src=src_managed/.*:s"))
  .settings(
    Compile / scalaxb / scalaxbXsdSource := new File("./conf/xsd"),
    Compile / scalaxb / scalaxbDispatchVersion := "1.1.3",
    Compile / scalaxb / scalaxbPackageName := "generated"
  )

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test") // the "test->test" allows reusing test code and test dependencies
  .settings(
    libraryDependencies ++= AppDependencies.test,
    dependencyOverrides ++= AppDependencies.overrides,
    excludeDependencies ++= AppDependencies.exclusions,
    DefaultBuildSettings.itSettings()
  )
