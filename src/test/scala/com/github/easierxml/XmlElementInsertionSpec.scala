package com.github.easierxml

import javaslang.control.Try
import javax.xml.xpath.XPathConstants

import com.github.easierxml.UnitSpec._
import org.w3c.dom.{Document, Element, NodeList}

class XmlElementInsertionSpec extends UnitSpec {
  /*===== SETUP =====*/
  private[this] val toBeInserted = "insert me!"

  /*===== TESTS =====*/
  /* set */
  "Xml.using(Document).at(String).setValue(String).getDocument()" should "return a Try.Success with a new Document for an empty Document and an element xPath with one node when element is not present" in {
    val originalDocument = emptyDocument()

    val xPath = s"/${nonEmptyAlphaStrGen.sampleValue}"

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

  it should "return a Try.Success with a new Document for an empty Document and an element xPath with two nodes when element is not present" in {
    val originalDocument = emptyDocument()

    val xPath = s"/${nonEmptyAlphaStrGen.sampleValue}/${nonEmptyAlphaStrGen.sampleValue}"

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

  it should "return a Try.Success with a new Document for an empty Document and an element xPath with more than two nodes when element is not present" in {
    val originalDocument = emptyDocument()

    val xPath = s"/${nonEmptyAlphaStrGen.sampleValue}/${nonEmptyAlphaStrGen.sampleValue}${elementXPathGen.sampleValue}"

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

  it should "return a Try.Success with a new Document for a non-empty Document and an element xPath when element is not present" in {
    val originalDocument = emptyDocument()

    val element1Name = nonEmptyAlphaStrGen.sampleValue
    val element1: Element = originalDocument.createElement(element1Name)
    originalDocument.appendChild(element1)

    val element2Name = nonEmptyAlphaStrGen.sampleValue
    val element2: Element = originalDocument.createElement(element2Name)
    element1.appendChild(element2)

    val element3Name = nonEmptyAlphaStrGen.sampleValue

    val element4Name = nonEmptyAlphaStrGen.sampleValue

    val element5Name = nonEmptyAlphaStrGen.sampleValue

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name/@$element5Name"

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

  it should "return a Try.Success with a new Document for a Document and an element xPath when one element is present" in {
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

    element4.setTextContent("existing value is here!")

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name"

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

  it should "return a Try.Failure with a exception for a Document and an element xPath when many elements are present" in {
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
    val element4a: Element = originalDocument.createElement(element4Name)
    element3.appendChild(element4a)
    element4a.setTextContent("existing value 1!")
    val element4b: Element = originalDocument.createElement(element4Name)
    element3.appendChild(element4b)
    element4b.setTextContent("existing value 2!")

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).setValue(toBeInserted)

    originalDocument.getDocumentElement should be(element1)
    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  /* add */
  "Xml.using(Document).at(String).addValue(String).getDocument()" should "return a Try.Success with a new Document for an empty Document and an element xPath with one node when element is not present" in {
    val originalDocument = emptyDocument()

    val xPath = s"/${nonEmptyAlphaStrGen.sampleValue}"

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

  it should "return a Try.Success with a new Document for an empty Document and an element xPath with two nodes when element is not present" in {
    val originalDocument = emptyDocument()

    val xPath = s"/${nonEmptyAlphaStrGen.sampleValue}/${nonEmptyAlphaStrGen.sampleValue}"

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

  it should "return a Try.Success with a new Document for an empty Document and an element xPath with more than two nodes when element is not present" in {
    val originalDocument = emptyDocument()

    val xPath = s"/${nonEmptyAlphaStrGen.sampleValue}/${nonEmptyAlphaStrGen.sampleValue}${elementXPathGen.sampleValue}"

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

  it should "return a Try.Success with a new Document for a Document and an element xPath when one element is present" in {
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

    val existingValue = "existing value!"
    element4.setTextContent(existingValue)

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).addValue(toBeInserted)

    originalDocument.getDocumentElement should be(element1)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(withConsumer { document =>
      document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

      val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
      nodeList.getLength should be(2)

      nodeList.item(0).getTextContent should be(existingValue)
      nodeList.item(1).getTextContent should be(toBeInserted)
    })
  }

  it should "return a Try.Success with a new Document for a Document and an element xPath when elements are present" in {
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

    val element4a: Element = originalDocument.createElement(element4Name)
    element4a.setTextContent("existing value!")
    element3.appendChild(element4a)

    val element4b: Element = originalDocument.createElement(element4Name)
    element4b.setTextContent("existing value!")
    element3.appendChild(element4b)

    val element4c: Element = originalDocument.createElement(element4Name)
    element4c.setTextContent("existing value!")
    element3.appendChild(element4c)

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).addValue(toBeInserted)

    originalDocument.getDocumentElement should be(element1)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(withConsumer { document =>
      document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

      val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
      nodeList.getLength should be(4)

      nodeList.item(0).getTextContent should be("existing value!")
      nodeList.item(1).getTextContent should be("existing value!")
      nodeList.item(2).getTextContent should be("existing value!")
      nodeList.item(3).getTextContent should be(toBeInserted)
    })

  }

}