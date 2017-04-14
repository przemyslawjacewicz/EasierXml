package com.github.easierxml

import java.util.function.Consumer
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.{XPath, XPathFactory}

import org.scalacheck.Gen
import org.scalamock.scalatest.MockFactory
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import org.w3c.dom.Document

abstract class UnitSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks with MockFactory

object UnitSpec {

  implicit class GenWithSampleValue[A](gen: Gen[A]) {
    def sampleValue: A = gen.sample match {
      case Some(v) => v
      case None => sampleValue
    }
  }

  val nonEmptyAlphaStrGen: Gen[String] = Gen.alphaStr suchThat ((_: String).nonEmpty)

  val attributeXPathGen: Gen[String] = for (levels <- Gen.choose(0, 6)) yield {
    (0 to levels map (_ => nonEmptyAlphaStrGen.sampleValue)).mkString("/", "/", s"/@${nonEmptyAlphaStrGen.sampleValue}")
  }

  val elementXPathGen: Gen[String] = for (levels <- Gen.choose(0, 6)) yield {
    (0 to levels map (_ => nonEmptyAlphaStrGen.sampleValue)).mkString("/", "/", "")
  }

  val theXPath: XPath = XPathFactory.newInstance().newXPath()

  def emptyDocument(): Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()

  def withConsumer[A](test: A => Unit): Consumer[A] = new Consumer[A] {
    override def accept(a: A): Unit = test(a)
  }

}