package com.github.easierxml

class XmlInvalidInputTests extends UnitSpec {
  /*===== SETUP =====*/


  /*===== TESTS =====*/

  /* extraction */
  "Xml.using(Document).at(String).getValue()" should "return a Try.Failure with an exception for an empty Document and an empty xPath String" in {
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

}