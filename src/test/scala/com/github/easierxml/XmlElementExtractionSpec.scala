package com.github.easierxml

import java.util.stream.{Collectors, Stream}
import javaslang.control.Try

import com.github.easierxml.UnitSpec._
import org.w3c.dom.Element

import scala.collection.JavaConverters._

class XmlElementExtractionSpec extends UnitSpec {
  /*===== SETUP =====*/


  /*===== TESTS =====*/
  "Xml.using(Document).at(String).getValue()" should "return a Try.Failure with an exception for a Document and an element xPath when element is not present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue

    val xPath = s"/$element1Name/$element2Name/$element3Name"

    val extractedValue: Try[String] = Xml.using(originalDocument).at(xPath).getValue

    originalDocument.getDocumentElement should be(element1)
    extractedValue shouldBe a[Try.Failure[_]]
    extractedValue.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Success with a value for a Document and an element xPath when one element is present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue
    val element3: Element = originalDocument.createElement(element3Name)
    val elementValue = nonEmptyAlphaStrGen.sampleValue
    element3.setTextContent(elementValue)
    element2.appendChild(element3)

    val xPath = s"/$element1Name/$element2Name/$element3Name"

    val extractedValue: Try[String] = Xml.using(originalDocument).at(xPath).getValue

    originalDocument.getDocumentElement should be(element1)
    extractedValue shouldBe a[Try.Success[_]]
    extractedValue.onSuccess(withConsumer { value =>
      value should equal(elementValue)
    })
  }

  it should "return a Try.Failure with an exception for a Document and an element xPath when many elements are present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue

    val element3a: Element = originalDocument.createElement(element3Name)
    val element3aValue = nonEmptyAlphaStrGen.sampleValue
    element3a.setTextContent(element3aValue)
    element2.appendChild(element3a)

    val element3b: Element = originalDocument.createElement(element3Name)
    val element3bValue = nonEmptyAlphaStrGen.sampleValue
    element3b.setTextContent(element3bValue)
    element2.appendChild(element3b)

    val element3c: Element = originalDocument.createElement(element3Name)
    val element3cValue = nonEmptyAlphaStrGen.sampleValue
    element3c.setTextContent(element3cValue)
    element2.appendChild(element3c)

    val xPath = s"/$element1Name/$element2Name/$element3Name"

    val extractedValue: Try[String] = Xml.using(originalDocument).at(xPath).getValue

    originalDocument.getDocumentElement should be(element1)
    extractedValue shouldBe a[Try.Failure[_]]
    extractedValue.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  "Xml.using(Document).at(String).getValues()" should "return a Try.Failure with an exception for a Document and an element xPath when element is not present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue

    val xPath = s"/$element1Name/$element2Name/$element3Name"

    val extractedValue: Try[Stream[String]] = Xml.using(originalDocument).at(xPath).getValues

    originalDocument.getDocumentElement should be(element1)
    extractedValue shouldBe a[Try.Failure[_]]
    extractedValue.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Success with a one-element Stream for a Document and an element xPath when one element is present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue
    val element3: Element = originalDocument.createElement(element3Name)
    val elementValue = nonEmptyAlphaStrGen.sampleValue
    element3.setTextContent(elementValue)
    element2.appendChild(element3)

    val xPath = s"/$element1Name/$element2Name/$element3Name"

    val extractedValue: Try[Stream[String]] = Xml.using(originalDocument).at(xPath).getValues

    originalDocument.getDocumentElement should be(element1)
    extractedValue shouldBe a[Try.Success[_]]
    extractedValue.onSuccess(withConsumer { stream =>
      val collected = stream.collect(Collectors.toList())
      collected.size() should be(1)
      collected.asScala.head should equal(elementValue)
    })
  }

  it should "return a Try.Success with a multi-element Stream for a Document and an element xPath when many elements are present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue

    val element3a: Element = originalDocument.createElement(element3Name)
    val element3aValue = nonEmptyAlphaStrGen.sampleValue
    element3a.setTextContent(element3aValue)
    element2.appendChild(element3a)

    val element3b: Element = originalDocument.createElement(element3Name)
    val element3bValue = nonEmptyAlphaStrGen.sampleValue
    element3b.setTextContent(element3bValue)
    element2.appendChild(element3b)

    val element3c: Element = originalDocument.createElement(element3Name)
    val element3cValue = nonEmptyAlphaStrGen.sampleValue
    element3c.setTextContent(element3cValue)
    element2.appendChild(element3c)

    val xPath = s"/$element1Name/$element2Name/$element3Name"

    val extractedValue: Try[Stream[String]] = Xml.using(originalDocument).at(xPath).getValues

    originalDocument.getDocumentElement should be(element1)
    extractedValue shouldBe a[Try.Success[_]]
    extractedValue.onSuccess(withConsumer { stream =>
      val collected = stream.collect(Collectors.toList())
      collected.size() should be(3)
      collected.get(0) should equal(element3aValue)
      collected.get(1) should equal(element3bValue)
      collected.get(2) should equal(element3cValue)
    })
  }

}
