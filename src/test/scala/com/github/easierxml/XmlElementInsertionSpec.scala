package com.github.easierxml

import java.util.function.Consumer
import javaslang.control.Try
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.{XPathConstants, XPathFactory}

import com.github.easierxml.UnitSpec._
import org.w3c.dom.{Document, Element, NodeList}

class XmlElementInsertionSpec extends UnitSpec {
  /*===== SETUP =====*/
  private[this] val theXPath = XPathFactory.newInstance().newXPath()

  /*===== TESTS =====*/
  /* set */
  "Xml.using(Document).at(String).setValue(String).getDocument()" should "return a Try.Success with a new Document for an empty Document and an element xPath when element is not present" in {
    val originalDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()

    val xPath = s"${elementXPathGen.sampleValue}"

    val toBeInserted = "insert me!"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).setValue(toBeInserted)

    originalDocument.getDocumentElement should be(null)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(new Consumer[Document] {
      override def accept(document: Document): Unit = {
        document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

        val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
        nodeList.getLength should be(1)

        val node = nodeList.item(0)
        node.getTextContent should be(toBeInserted)
      }
    })
  }

  it should "return a Try.Success with a modified Document for a non-empty Document and an element xPath when element is not present" in {
    val originalDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()

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

    val toBeInserted = "insert me!"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).setValue(toBeInserted)

    originalDocument.getDocumentElement should be(element1)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(new Consumer[Document] {
      override def accept(document: Document): Unit = {
        document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

        val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
        nodeList.getLength should be(1)

        val node = nodeList.item(0)
        node.getTextContent should be(toBeInserted)
      }
    })
  }

  it should "return a Try.Success with a modified Document for a Document and an element xPath when one element is present" in {
    val originalDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()

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

    element4.setTextContent("old value was here!")

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name"

    val toBeInserted = "insert me!"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).setValue(toBeInserted)

    originalDocument.getDocumentElement should be(element1)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(new Consumer[Document] {
      override def accept(document: Document): Unit = {
        document.getDocumentElement.getNodeName should be(xPath.split("/").filter(s => s.nonEmpty).head)

        val nodeList = theXPath.evaluate(xPath, document.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
        nodeList.getLength should be(1)

        val node = nodeList.item(0)
        node.getTextContent should be(toBeInserted)
      }
    })
  }

  it should "return a Try.Failure with a exception for a Document and an element xPath when many elements are present" in {
    val originalDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()

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
    element4a.setTextContent("old value was here!")
    val element4b: Element = originalDocument.createElement(element4Name)
    element3.appendChild(element4b)
    element4b.setTextContent("old value was here!")

    val xPath = s"/$element1Name/$element2Name/$element3Name/$element4Name"

    val toBeInserted = "insert me!"

    val newDocument: Try[Document] = Xml.using(originalDocument).at(xPath).setValue(toBeInserted)

    originalDocument.getDocumentElement should be(element1)
    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(new Consumer[Throwable] {
      override def accept(t: Throwable): Unit = {
        t shouldBe an[XmlContentException]
      }
    })
  }

  /* add */
  "Xml.using(Document).at(String).addValue(String).getDocument()" should "return a Try.Success with a modified Document for a Document and an element xPath when element is not present" in {
    fail("Not yet implemented!")
  }

  it should "return a Try.Success with a modified Document for a Document and an element xPath when one element is present" in {
    fail("Not yet implemented!")
  }

  it should "return a Try.Success with a modified Document for a Document and an element xPath when many elements are present" in {
    fail("Not yet implemented!")
  }

}
