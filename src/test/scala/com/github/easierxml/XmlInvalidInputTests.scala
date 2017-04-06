package com.github.easierxml

import org.w3c.dom.Document
import UnitSpec._

class XmlInvalidInputTests extends UnitSpec {
  /*===== SETUP =====*/


  /*===== TESTS =====*/

  /* extraction */
  "Xml.using(Document).at(String).getValue()" should "return a Try.Failure with an exception for an empty Document and an empty xPath String" in {
    val document = stub[Document]
//    Xml.using(document).at()

    fail("Not yet implemented!")
  }

  it should "return a Try.Failure with an exception for an empty Document and a non-empty xPath String" in {
    fail("Not yet implemented!")
  }

  it should "return a Try.Failure with an exception for an non-empty Document and an empty xPath String" in {
    fail("Not yet implemented!")
  }

  "Xml.using(Document).at(String).getValues()" should "return a Try.Failure with an exception for an empty Document and an empty xPath String" in {
    fail("Not yet implemented!")
  }

  it should "return a Try.Failure with an exception for an empty Document and a non-empty xPath String" in {
    fail("Not yet implemented!")
  }

  it should "return a Try.Failure with an exception for an non-empty Document and an empty xPath String" in {
    fail("Not yet implemented!")
  }

  /* insertion */
  "Xml.using(Document).at(String).setValue(String)" should "return a Try.Failure with an exception for an empty Document and an empty xPath String" in {
    fail("Not yet implemented!")
  }

  it should "return a Try.Failure with an exception for an non-empty Document and an empty xPath String" in {
    fail("Not yet implemented!")
  }

  "Xml.using(Document).at(String).addValue(String)" should "return a Try.Failure with an exception for an empty Document and an empty xPath String" in {
    fail("Not yet implemented!")
  }

  it should "return a Try.Failure with an exception for an non-empty Document and an empty xPath String" in {
    fail("Not yet implemented!")
  }

}