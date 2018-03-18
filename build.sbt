inThisBuild(
  Seq(
    version in ThisBuild := "0.0.3-SNAPSHOT",
    organization in ThisBuild := "com.sohoffice"
  )
)

lazy val bintrayCommonSettings = Seq(
  publishMavenStyle := false,
  licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
  bintrayVcsUrl := Some("git@github.com:sohoffice/docExtract.git"),
  bintrayRepository := "sbt-plugins",
  bintrayOrganization in bintray := None
)

resolvers += Resolver.bintrayRepo("scalaz", "releases")

resolvers += Resolver.typesafeRepo("releases")

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val docExtract = project.in(file("."))
  .aggregate(core, plugin)
  .settings(sourcesInBase := false)
  .settings(noPublishSettings: _*)

lazy val core = project.in(file("core"))
  .settings(
    bintrayCommonSettings,
    name := "doc-extract",
    scalaVersion := "2.12.4",
    crossScalaVersions := Seq("2.11.11", "2.12.4"),
    // scalaVersion := "2.11.11",
    libraryDependencies += {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((major, 11)) =>
          "org.scala-lang" % s"scala-compiler" % s"$major.11.11"
        case Some((major, 12)) =>
          "org.scala-lang" % s"scala-compiler" % s"$major.12.4"
        case x =>
          throw new IllegalArgumentException(s"Please improve the cross build setting, $x is unexpected.")
      }
    },
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )

lazy val plugin = project.in(file("sbtPlugin"))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.sohoffice.doc.extract"
  )
  .settings(
    bintrayCommonSettings,
    name := "sbt-doc-extract",
    description := "Extract case class and properties description from scaladoc as a resource bundle.",
    sbtPlugin := true
  )
