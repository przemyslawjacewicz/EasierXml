package com.github.easierxml

import java.util.stream.Stream
import javaslang.control.Try

import com.github.easierxml.UnitSpec._
import org.w3c.dom.{Document, Element}

class XmlInvalidInputUsingElementSpec extends UnitSpec {
  /*===== SETUP =====*/
  private[this] val toBeInserted = "insert me!"

  /*===== TESTS =====*/
  /* extraction */
  "Xml.using(Element).at(String).getValue()" should "return a Try.Failure with an exception for an invalid Element and an invalid xPath String" in {
    val element = stub[Element]
    val xPath = "i am an invalid xPath"
    val value: Try[String] = Xml.using(element).at(xPath).getValue

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid Element and a valid attribute xPath String" in {
    val element = stub[Element]
    val xPath = attributeXPathGen.sampleValue
    val value: Try[String] = Xml.using(element).at(xPath).getValue

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid Element and a valid element xPath String" in {
    val element = stub[Element]
    val xPath = elementXPathGen.sampleValue
    val value: Try[String] = Xml.using(element).at(xPath).getValue

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Element and an invalid xPath String" in {
    val element = emptyDocument().createElement("root")
    val xPath = "i am an invalid xPath !"
    val value: Try[Stream[String]] = Xml.using(element).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  "Xml.using(Element).at(String).getValues()" should "return a Try.Failure with an exception for an invalid Element and an invalid xPath String" in {
    val element = stub[Element]
    val xPath = "i am an invalid xPath"
    val value: Try[Stream[String]] = Xml.using(element).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid Element and a valid attribute xPath String" in {
    val element = stub[Element]
    val xPath = attributeXPathGen.sampleValue
    val value: Try[Stream[String]] = Xml.using(element).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid Element and a element valid xPath String" in {
    val element = stub[Element]
    val xPath = elementXPathGen.sampleValue
    val value: Try[Stream[String]] = Xml.using(element).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Element and an invalid xPath String" in {
    val element = emptyDocument().createElement("root")
    val xPath = "i am an invalid xPath !"
    val value: Try[Stream[String]] = Xml.using(element).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  /* insertion */
  "Xml.using(Element).at(String).setValue(String)" should "return a Try.Failure with an exception for an invalid Element and an invalid xPath String" in {
    val document = stub[Element]
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(document).at(xPath).setValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Element and an invalid xPath String" in {
    val document = emptyDocument().createElement("root")
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(document).at(xPath).setValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  "Xml.using(Element).at(String).addValue(String)" should "return a Try.Failure with an exception for an invalid Element and an invalid xPath String" in {
    val element = stub[Element]
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(element).at(xPath).addValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Element and an invalid xPath String" in {
    val element = emptyDocument().createElement("root")
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(element).at(xPath).addValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

}