package com.github.easierxml

import java.nio.charset.Charset
import java.nio.file.{Files, Paths}
import java.util.stream.Stream
import javaslang.control.Try

import com.github.easierxml.UnitSpec._
import org.w3c.dom.Document

class XmlInvalidInputUsingPathSpec extends UnitSpec {
  /*===== SETUP =====*/
  private[this] val toBeInserted = "insert me!"

  /*===== TESTS =====*/
  /* extraction */
  "Xml.using(Path).at(String).getValue()" should "return a Try.Failure with an exception for an invalid Path and an invalid xPath String" in {
    val file = Paths.get("invalid.xml")
    val xPath = "i am an invalid xPath"
    val value: Try[String] = Xml.using(file).at(xPath).getValue

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid Path and a valid attribute xPath String" in {
    val file = Paths.get("invalid.xml")
    val xPath = attributeXPathGen.sampleValue
    val value: Try[String] = Xml.using(file).at(xPath).getValue

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid Path and a valid element xPath String" in {
    val file = Paths.get("invalid.xml")
    val xPath = elementXPathGen.sampleValue
    val value: Try[String] = Xml.using(file).at(xPath).getValue

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Path and an invalid xPath String" in {
    val file = Files.createTempFile("", "")
    Files.write(file, "<root/>".getBytes(Charset.forName("UTF-8")))
    val xPath = "i am an invalid xPath !"
    val value: Try[Stream[String]] = Xml.using(file).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  "Xml.using(Path).at(String).getValues()" should "return a Try.Failure with an exception for an invalid Path and an invalid xPath String" in {
    val file = Paths.get("invalid.xml")
    val xPath = "i am an invalid xPath"
    val value: Try[Stream[String]] = Xml.using(file).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid Path and a valid attribute xPath String" in {
    val file = Paths.get("invalid.xml")
    val xPath = attributeXPathGen.sampleValue
    val value: Try[Stream[String]] = Xml.using(file).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for an invalid Path and a element valid xPath String" in {
    val file = Paths.get("invalid.xml")
    val xPath = elementXPathGen.sampleValue
    val value: Try[Stream[String]] = Xml.using(file).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Path and an invalid xPath String" in {
    val file = Files.createTempFile("", "")
    Files.write(file, "<root/>".getBytes(Charset.forName("UTF-8")))
    val xPath = "i am an invalid xPath !"
    val value: Try[Stream[String]] = Xml.using(file).at(xPath).getValues

    value shouldBe a[Try.Failure[_]]
    value.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  /* insertion */
  "Xml.using(Path).at(String).setValue(String)" should "return a Try.Failure with an exception for an invalid Path and an invalid xPath String" in {
    val file = Paths.get("invalid.xml")
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(file).at(xPath).setValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Path and an invalid xPath String" in {
    val file = Files.createTempFile("", "")
    Files.write(file, "<root/>".getBytes(Charset.forName("UTF-8")))
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(file).at(xPath).setValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  "Xml.using(Path).at(String).addValue(String)" should "return a Try.Failure with an exception for an invalid Path and an invalid xPath String" in {
    val file = Paths.get("invalid.xml")
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(file).at(xPath).addValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

  it should "return a Try.Failure with an exception for a valid Path and an invalid xPath String" in {
    val file = Files.createTempFile("", "")
    Files.write(file, "<root/>".getBytes(Charset.forName("UTF-8")))
    val xPath = "i am an invalid xPath"
    val newDocument: Try[Document] = Xml.using(file).at(xPath).addValue(toBeInserted)

    newDocument shouldBe a[Try.Failure[_]]
    newDocument.onFailure(withConsumer { ex =>
      ex shouldBe an[XmlContentException]
    })
  }

}