package com.github.easierxml

import javaslang.control.Try
import javax.xml.xpath.XPathConstants

import com.github.easierxml.UnitSpec._
import org.w3c.dom.{Document, Element, NodeList}

class XmlAttributeInsertionSpec extends UnitSpec {
  /*===== SETUP =====*/
  private[this] val toBeInserted = "insert me!"

  /*===== TESTS =====*/
  /* set */
  "Xml.using(Document).at(String).setValue(String).getDocument()" should "return a Try.Success with a new Document for an empty Document and an attribute xPath when attribute is not present" in {
    val originalDocument = emptyDocument()

    val xPath = s"${attributeXPathGen.sampleValue}"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).setValue(toBeInserted)

    originalDocument.getDocumentElement should be(null)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(withConsumer { document =>
      document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

      val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
      nodeList.getLength should be(1)

      val node = nodeList.item(0)
      node.getTextContent should be(toBeInserted)
    })
  }

  it should "return a Try.Success with a modified Document for a non-empty Document and an attribute xPath when attribute is not present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue

    val element4Name = nonEmptyAlphaStrGen.sampleValue

    val attributeName = nonEmptyAlphaStrGen.sampleValue

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name/@$attributeName"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).setValue(toBeInserted)

    originalDocument.getDocumentElement should be(element1)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(withConsumer { document =>
      document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

      val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
      nodeList.getLength should be(1)

      val node = nodeList.item(0)
      node.getTextContent should be(toBeInserted)
    })
  }

  it should "return a Try.Success with a modified Document for a non-empty Document and an attribute xPath when attribute is present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue
    val element3: Element = originalDocument.createElement(element3Name)
    element2.appendChild(element3)

    val element4Name = nonEmptyAlphaStrGen.sampleValue
    val element4: Element = originalDocument.createElement(element4Name)
    element3.appendChild(element4)

    val attributeName = nonEmptyAlphaStrGen.sampleValue
    element4.setAttribute(attributeName, "old value was here!")

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name/@$attributeName"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).setValue(toBeInserted)

    originalDocument.getDocumentElement should be(element1)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(withConsumer { document =>
      document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

      val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
      nodeList.getLength should be(1)

      val node = nodeList.item(0)
      node.getTextContent should be(toBeInserted)
    })
  }

  /* add */
  "Xml.using(Document).at(String).addValue(String).getDocument()" should "return a Try.Success with a new Document for an empty Document and an attribute xPath when attribute is not present" in {
    val originalDocument = emptyDocument()

    val xPath = s"${attributeXPathGen.sampleValue}"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).addValue(toBeInserted)

    originalDocument.getDocumentElement should be(null)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(withConsumer { document =>
      document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

      val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
      nodeList.getLength should be(1)

      val node = nodeList.item(0)
      node.getTextContent should be(toBeInserted)
    })
  }

  it should "return a Try.Success with a modified Document for a non-empty Document and an attribute xPath when attribute is not present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue

    val element4Name = nonEmptyAlphaStrGen.sampleValue

    val attributeName = nonEmptyAlphaStrGen.sampleValue

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name/@$attributeName"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).addValue(toBeInserted)

    originalDocument.getDocumentElement should be(element1)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(withConsumer { document =>
      document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

      val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
      nodeList.getLength should be(1)

      val node = nodeList.item(0)
      node.getTextContent should be(toBeInserted)
    })
  }

  it should "return a Try.Success with a modified Document for a non-empty Document and an attribute xPath when attribute is present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue
    val element3: Element = originalDocument.createElement(element3Name)
    element2.appendChild(element3)

    val element4Name = nonEmptyAlphaStrGen.sampleValue
    val element4: Element = originalDocument.createElement(element4Name)
    element3.appendChild(element4)

    val attributeName = nonEmptyAlphaStrGen.sampleValue
    element4.setAttribute(attributeName, "old value was here!")

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name/@$attributeName"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).addValue(toBeInserted)

    originalDocument.getDocumentElement should be(element1)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(withConsumer { document =>
      document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

      val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
      nodeList.getLength should be(1)

      val node = nodeList.item(0)
      node.getTextContent should be(toBeInserted)
    })
  }

}
