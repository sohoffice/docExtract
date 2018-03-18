package com.sohoffice.doc.extract.sbt

import java.io.File

import sbt.{ SettingKey, TaskKey }

trait DocExtractKeys {

  val docExtract = TaskKey[Unit]("docExtract", "Extract descriptions from scaladoc")
  val docExtractTarget = SettingKey[String]("docExtractTarget", "The file to export the extracted descriptions, relative to target directory. Use 'STDOUT' or 'STDERR' for console output.")
  val docExtractTargetFile = SettingKey[Either[String, File]]("docExtractTargetFile", "Full file path produced from docExtractTarget, if applied")

}
