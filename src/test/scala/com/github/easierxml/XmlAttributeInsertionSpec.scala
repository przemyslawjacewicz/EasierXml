package com.github.easierxml

import java.util.function.Consumer
import javaslang.control.Try
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.{XPathConstants, XPathFactory}

import com.github.easierxml.UnitSpec._
import org.w3c.dom.{Document, NodeList}

class XmlAttributeInsertionSpec extends UnitSpec {
  /*===== SETUP =====*/


  /*===== TESTS =====*/
  /* set */
  "Xml.using(Document).at(String).setValue(String).getDocument()" should "return a Try.Success with a modified Document for a Document and an attribute xPath when attribute is not present" in {
    val dbf = DocumentBuilderFactory.newInstance()
    val builder = dbf.newDocumentBuilder()
    val document = builder.newDocument()

    val xPath = s"${attributeXPathGen.sampleValue}"
    info(s"xPath=$xPath")

    val toBeInserted = "insert me!"

    val newDocument: Try[Document] = Xml.using(document).at(xPath).setValue(toBeInserted)

    document.getDocumentElement shouldBe(null)
    newDocument shouldBe a[Try.Success[_]]
    newDocument.onSuccess(new Consumer[Document] {
      override def accept(d: Document): Unit = {
        info(s"root name: ${xPath.split("/").filter(s => s.nonEmpty).head}")
        d.getDocumentElement.getNodeName should be( xPath.split("/").filter(s => s.nonEmpty).head )

        val theXPath = XPathFactory.newInstance().newXPath()

        val nodeList = theXPath.evaluate(xPath, d.getDocumentElement, XPathConstants.NODESET).asInstanceOf[NodeList]
        nodeList.getLength should be(1)

        val node = nodeList.item(0)
        node.getTextContent should be(toBeInserted)
      }
    })
  }

  it should "return a Try.Success with a modified Document for a Document and an attribute xPath when attribute is present" in {
    fail("Not yet implemented!")
  }

  /* add */
  "Xml.using(Document).at(String).addValue(String).getDocument()" should "return a Try.Success with a modified Document for a Document and an attribute xPath when attribute is not present" in {
    fail("Not yet implemented!")
  }

  it should "return a Try.Success with a modified Document for a Document and an attribute xPath when attribute is present" in {
    fail("Not yet implemented!")
  }

}
