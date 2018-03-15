package com.sohoffice.doc.extract

import java.io.{ File, PrintWriter, Writer }

import scala.tools.nsc.doc.Universe
import scala.tools.nsc.doc.doclet.{ Generator, Universer }
import scala.tools.nsc.doc.model.DocTemplateEntity

class DocExtractDoclet extends Generator with Universer {
  private implicit val impUniverse: Universe = universe

  private val output = DocExtractOutput.ALL

  override protected def generateImpl(): Unit = {
    val target = System.getProperty("docExtractTarget")
    println(s"Extracting document to $target")
    implicit val writer: Writer = prepareWriter(target)
    generateFor(universe.rootPackage)
  }

  /**
   * Produce output for one entity, and move on to the children.
   *
   * @param tpl
   */
  def generateFor(tpl: DocTemplateEntity)(implicit writer: Writer): Unit = {
    output.generate(tpl).foreach(s => {
      writer.write(s)
      writer.write(System.lineSeparator())
      // flush immediately to make sure lines do not interfere each other
      writer.flush()
    })
    tpl.templates collect { case d: DocTemplateEntity => d } map generateFor
  }

  def prepareWriter(target: String): Writer = {
    target match {
      case null | "" | "STDOUT" =>
        new PrintWriter(System.out)
      case "STDERR" =>
        new PrintWriter(System.err)
      case _ =>
        val targetFile = new File(target)
        if (!targetFile.exists() && !targetFile.getParentFile.mkdirs()) {
          println(s"Fail to create target directory ${targetFile.getParentFile}, continue for now.")
        }
        new PrintWriter(target, "UTF8")
    }
  }
}
