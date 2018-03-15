package com.sohoffice.sbtDocExtract

import sbt._
import Keys._

object DocExtractPlugin extends AutoPlugin {

  object autoImport extends DocExtractKeys

  import autoImport._

  // the plugin will run only when you execute it manually
  override def trigger = noTrigger

  override def projectSettings: Seq[Def.Setting[_]] = {
    val myTarget = docExtractTarget in docExtract
    Seq(
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-compiler" % scalaVersion.value,
        "com.sohoffice" %% "doc-extract" % "0.1-SNAPSHOT"),
      docExtract := {
        val jars = (fullClasspathAsJars in (Compile, doc)).value.map(_.data).toVector
        val options = ForkOptions()
          .withRunJVMOptions(Vector(
            s"-DdocExtractTarget=${myTarget.value}"))
          .withBootJars(jars)

        val args: Seq[String] = Seq(
          "scala.tools.nsc.ScalaDoc",
          // "-verbose",
          // "-d", (crossTarget.value / "api").toString,
          "-classpath", (fullClasspath in (Compile, doc)).value.map(_.data.toString).mkString(":"),
          "-doc-generator", "com.sohoffice.doc.extract.DocExtractDoclet") ++ (sources in Compile).value.map(s => s"""${s.toString}""")

        val log = Keys.streams.value.log
        args.foreach(s => log.debug(s))

        Fork.java(options, args)
      },
      docExtractTarget in docExtract := (crossTarget.value / "docExtract.properties").getAbsolutePath)
  }
}
