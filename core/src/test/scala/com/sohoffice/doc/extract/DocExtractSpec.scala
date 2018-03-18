package com.sohoffice.doc.extract

import java.io._
import java.net.URLClassLoader
import java.util.Properties

import org.scalatest._
import org.scalatest.Matchers._

import scala.collection.JavaConverters._
import scala.io.Source
import scala.tools.nsc.ScalaDoc

class DocExtractSpec extends DocExtractBaseSpec {

  behavior of "DocExtractDoclet"

  it should "handle case class" in {
    val source = new File("core/src/test/scala/example/FooCaseClass.scala")

    val prop = runScaladoc(source)
    prop.keySet should contain allElementsOf Seq(
      "example.FooCaseClass",
      "example.FooCaseClass.name",
      "example.FooCaseClass.age",
      "example.FooCaseClass.method")

    println("handle case class")
  }

  it should "handle object" in {
    val source = new File("core/src/test/scala/example/FooObject.scala")

    val prop = runScaladoc(source)
    prop.keySet should contain theSameElementsAs Seq(
      "example.FooObject",
      "example.FooObject.method",
      "example.FooObject.method(Int)",
      "example.FooObject.method(Int)#arg",
      "example.FooObject.FooBarObject",
      "example.FooObject.FooBarObject.FooBarBazCaseClass",
      "example.FooObject.FooBarObject.FooBarBazCaseClass.name")

    println("handle object")
  }

  it should "handle class" in {
    val source = new File("core/src/test/scala/example/FooClass.scala")

    val prop = runScaladoc(source)
    prop.keySet should contain theSameElementsAs Seq(
      "example.FooClass",
      "example.FooClass.method",
      "example.FooClass.method(String)",
      "example.FooClass.method(String)#nickname",
      "example.FooClass.lazyValue")

    println("handle class")
  }

  it should "handle multiline comments" in {
    val source = new File("core/src/test/scala/example/MultiLineClass.scala")

    val prop = runScaladoc(source)
    prop.keySet should contain theSameElementsAs Seq(
      "example.MultiLineClass",
      "example.MultiLineClass.method",
      "example.MultiLineClass.lazyValue")

    assert(prop == Map(
      "example.MultiLineClass" -> "MultiLineClass has line1 .\nIt also has line2.",
      "example.MultiLineClass.method" -> "method has line1 .\nAnd line2",
      "example.MultiLineClass.lazyValue" -> "value has line1 .\nAnd line2"))

    println("handle multiline comments")
  }

  private def runScaladoc(source: File): Map[String, String] = {
    // make sure this method can not be executed simultaneously
    this.synchronized {
      val out = File.createTempFile("docExtract", ".properties")
      try {
        System.setProperty("docExtractTarget", out.getAbsolutePath)

        val cl = ClassLoader.getSystemClassLoader
        val urls = cl.asInstanceOf[URLClassLoader].getURLs
        val classpath = urls.map(url => url.getFile).mkString(":")

        ScalaDoc.process(Array(
          "-classpath", classpath,
          "-doc-generator", "com.sohoffice.doc.extract.DocExtractDoclet",
          source.getAbsolutePath))
        println(s"Finish running scaladoc.")
        println("------------------------")
        Source.fromFile(out, "UTF8").getLines().foreach(s => println(s"  $s"))
        println("------------------------")

        val props = new Properties()
        props.load(new FileReader(out))

        props.stringPropertyNames().asScala.map { propName =>
          val v = props.getProperty(propName)
          propName -> v
        }.toMap
      } finally {
        out.deleteOnExit()
      }
    }
  }
}
