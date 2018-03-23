package com.sohoffice.doc.extract.sbt

import sbt._
import Keys._
import com.sohoffice.doc.extract.BuildInfo

object DocExtractPlugin extends AutoPlugin {

  lazy val DocExtractConfig = config("docExtract") describedAs("doc-extract specific configurations")

  object autoImport extends DocExtractKeys

  import autoImport._

  // the plugin will run only when you execute it manually
  override def trigger = allRequirements

  override def projectConfigurations: Seq[Configuration] = super.projectConfigurations ++ Seq(DocExtractConfig)

  override def projectSettings: Seq[Def.Setting[_]] = {
    val myTarget: Def.Initialize[String] = docExtractTargetFile {
      case Left(s) => s
      case Right(f) => f.getAbsolutePath
    }
    Seq(
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-compiler" % scalaVersion.value % DocExtractConfig,
        "com.sohoffice" %% "doc-extract" % BuildInfo.version % DocExtractConfig),
      docExtract := {
        val jars = (fullClasspath in(Compile, doc)).value.map(_.data).toVector ++
          update.value.select(configurationFilter("docExtract"))
//        val jars = (fullClasspath in(Compile, doc)).value.map(_.data).toVector
        Keys.streams.value.log.debug(s"docExtract jars: $jars")
        val options = ForkOptions()
          .withRunJVMOptions(Vector(
            s"-DdocExtractTarget=${myTarget.value}"))
          .withBootJars(jars)

        val args: Seq[String] = Seq(
          "scala.tools.nsc.ScalaDoc",
          // "-verbose",
          // "-d", (crossTarget.value / "api").toString,
          "-classpath", (fullClasspath in(Compile, doc)).value.map(_.data.toString).mkString(":"),
          "-doc-generator", "com.sohoffice.doc.extract.DocExtractDoclet") ++ (sources in Compile).value.map(s => s"""${s.toString}""")

        docExtractTargetFile.value match {
          case Right(file) =>
            IO.delete(file)
          case Left(s) =>
        }

        val log = Keys.streams.value.log
        args.foreach(s => log.debug(s"docExtract args: $s"))

        Fork.java(options, args)
      },
      docExtractTarget := "docExtract.properties",
      docExtractTargetFile := {
        docExtractTarget.value match {
          case s if s == "STDOUT" || s == "STDERR" =>
            Left(s)
          case filename =>
            Right(target.value / filename)
        }
      }
    )
  }
}
