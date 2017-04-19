package com.github.easierxml

import java.util.stream.{Collectors, Stream}
import javaslang.control.Try

import com.github.easierxml.UnitSpec.{emptyDocument, nonEmptyAlphaStrGen, _}
import org.w3c.dom.Element

import scala.collection.JavaConverters._

class XmlAttributeExtractionSpec extends UnitSpec {
  /*===== SETUP =====*/


  /*===== TESTS =====*/
  "Xml.using(Document).at(String).getValue()" should "return a Try.Failure with an exception for a Document and an attribute xPath when attribute is not present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val attributeName = nonEmptyAlphaStrGen.sampleValue
    val attributeValue = nonEmptyAlphaStrGen.sampleValue

    val xPath = s"/$element1Name/$element2Name/@$attributeName"

    val extractedValue: Try[String] = Xml.using(originalDocument).at(xPath).getValue

    originalDocument.getDocumentElement should be(element1)
    extractedValue shouldBe a[Try.Failure[_]]
    extractedValue.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Success with a value for a Document and an attribute xPath when attribute is present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val attributeName = nonEmptyAlphaStrGen.sampleValue
    val attributeValue = nonEmptyAlphaStrGen.sampleValue
    element2.setAttribute(attributeName, attributeValue)

    val xPath = s"/$element1Name/$element2Name/@$attributeName"

    val extractedValue: Try[String] = Xml.using(originalDocument).at(xPath).getValue

    originalDocument.getDocumentElement should be(element1)
    extractedValue shouldBe a[Try.Success[_]]
    extractedValue.onSuccess(withConsumer { value =>
      value should equal(attributeValue)
    })
  }

  "Xml.using(Document).at(String).getValues()" should "return a Try.Failure with an exception for a Document and an attribute xPath when attribute is not present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val attributeName = nonEmptyAlphaStrGen.sampleValue

    val xPath = s"/$element1Name/$element2Name/@$attributeName"

    val extractedValue: Try[Stream[String]] = Xml.using(originalDocument).at(xPath).getValues

    originalDocument.getDocumentElement should be(element1)
    extractedValue shouldBe a[Try.Failure[_]]
    extractedValue.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Success with a one-element Stream for a Document and an attribute xPath when attribute is present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val attributeName = nonEmptyAlphaStrGen.sampleValue
    val attributeValue = nonEmptyAlphaStrGen.sampleValue
    element2.setAttribute(attributeName, attributeValue)

    val xPath = s"/$element1Name/$element2Name/@$attributeName"

    val extractedValue: Try[Stream[String]] = Xml.using(originalDocument).at(xPath).getValues

    originalDocument.getDocumentElement should be(element1)
    extractedValue shouldBe a[Try.Success[_]]
    extractedValue.onSuccess(withConsumer { stream =>
      val collected = stream.collect(Collectors.toList())
      collected.size() should be(1)
      collected.asScala.head should equal(attributeValue)
    })
  }

}
