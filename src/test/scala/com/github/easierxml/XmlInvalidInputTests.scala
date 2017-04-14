package com.github.easierxml

import java.util.stream.Stream
import javaslang.control.Try

import com.github.easierxml.UnitSpec._
import org.w3c.dom.Document

class XmlInvalidInputTests extends UnitSpec {
  /*===== SETUP =====*/
  private[this] val toBeInserted = "insert me!"

  /*===== TESTS =====*/
  /* extraction */
  "Xml.using(Document).at(String).getValue()" should "return a Try.Failure with an exception for an invalid Document and an invalid xPath String" in {
    fail("Not yet implemented!")
  }

  it should "return a Try.Failure with an exception for an invalid Document and a valid xPath String" in {
    fail("Not yet implemented!")
  }

  it should "return a Try.Failure with an exception for a valid Document and an invalid xPath String" in {
    fail("Not yet implemented!")
  }

  "Xml.using(Document).at(String).getValues()" should "return a Try.Failure with an exception for an invalid Document and an invalid xPath String" in {
    val document = stub[Document]
    val xPath = "i am an invalid xPath"
    val value: Try[Stream[String]] = Xml.using(document).at(xPath).getValues()

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid Document and a valid attribute xPath String" in {
    val document = stub[Document]
    val xPath = attributeXPathGen.sampleValue
    val value: Try[Stream[String]] = Xml.using(document).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid Document and a element valid xPath String" in {
    val document = stub[Document]
    val xPath = elementXPathGen.sampleValue
    val value: Try[Stream[String]] = Xml.using(document).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Document and an invalid xPath String" in {
    val document = emptyDocument()
    document.appendChild(document.createElement("root"))
    val xPath = "i am an invalid xPath !"
    val value: Try[Stream[String]] = Xml.using(document).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  /* insertion */
  "Xml.using(Document).at(String).setValue(String)" should "return a Try.Failure with an exception for an invalid Document and an invalid xPath String" in {
    val document = stub[Document]
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(document).at(xPath).setValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Document and an invalid xPath String" in {
    val document = emptyDocument()
    document.appendChild(document.createElement("root"))
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(document).at(xPath).setValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  "Xml.using(Document).at(String).addValue(String)" should "return a Try.Failure with an exception for an invalid Document and an invalid xPath String" in {
    val document = stub[Document]
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(document).at(xPath).addValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Document and an invalid xPath String" in {
    val document = emptyDocument()
    document.appendChild(document.createElement("root"))
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(document).at(xPath).addValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

}