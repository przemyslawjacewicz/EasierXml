package com.github.easierxml

import java.util.stream.Stream
import javaslang.control.Try

import com.github.easierxml.UnitSpec.{attributeXPathGen, elementXPathGen, withConsumer, _}
import org.w3c.dom.Document

class XmlInvalidInputUsingStringSpec extends UnitSpec {
  /*===== SETUP =====*/
  private[this] val toBeInserted = "insert me!"

  /*===== TESTS =====*/
  /* extraction */
  "Xml.using(String).at(String).getValue()" should "return a Try.Failure with an exception for an invalid String and an invalid xPath String" in {
    val xml = "<root"
    val xPath = "i am an invalid xPath"
    val value: Try[String] = Xml.using(xml).at(xPath).getValue

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid String and a valid attribute xPath String" in {
    val xml = "<root"
    val xPath = attributeXPathGen.sampleValue
    val value: Try[String] = Xml.using(xml).at(xPath).getValue

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid String and a valid element xPath String" in {
    val xml = "<root"
    val xPath = elementXPathGen.sampleValue
    val value: Try[String] = Xml.using(xml).at(xPath).getValue

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid String and an invalid xPath String" in {
    val xml = "<root/>"
    val xPath = "i am an invalid xPath !"
    val value: Try[Stream[String]] = Xml.using(xml).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  "Xml.using(String).at(String).getValues()" should "return a Try.Failure with an exception for an invalid String and an invalid xPath String" in {
    val xml = "<root"
    val xPath = "i am an invalid xPath"
    val value: Try[Stream[String]] = Xml.using(xml).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid String and a valid attribute xPath String" in {
    val xml = "<root"
    val xPath = attributeXPathGen.sampleValue
    val value: Try[Stream[String]] = Xml.using(xml).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid String and a element valid xPath String" in {
    val xml = "<root"
    val xPath = elementXPathGen.sampleValue
    val value: Try[Stream[String]] = Xml.using(xml).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid String and an invalid xPath String" in {
    val xml = "<root/>"
    val xPath = "i am an invalid xPath !"
    val value: Try[Stream[String]] = Xml.using(xml).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  /* insertion */
  "Xml.using(String).at(String).setValue(String)" should "return a Try.Failure with an exception for an invalid String and an invalid xPath String" in {
    val xml = "<root"
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(xml).at(xPath).setValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid String and an invalid xPath String" in {
    val xml = "<root"
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(xml).at(xPath).setValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  "Xml.using(String).at(String).addValue(String)" should "return a Try.Failure with an exception for an invalid String and an invalid xPath String" in {
    val xml = "<root"
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(xml).at(xPath).addValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid String and an invalid xPath String" in {
    val xml = "<root"
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(xml).at(xPath).addValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

}
