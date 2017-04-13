package com.github.easierxml

import org.scalacheck.Gen
import org.scalamock.scalatest.MockFactory
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

abstract class UnitSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks with MockFactory

object UnitSpec {

  implicit class GenWithSampleValue[A](gen: Gen[A]) {
    def sampleValue: A = gen.sample match {
      case Some(v) => v
      case None => sampleValue
    }
  }

  val nonEmptyAlphaStrGen: Gen[String] = Gen.alphaStr suchThat ((_: String).nonEmpty)

  val attributeXPathGen: Gen[String] = for (levels <- Gen.choose(0, 6)) yield {
    (0 to levels map (_ => nonEmptyAlphaStrGen.sampleValue)).mkString("/", "/", s"/@${nonEmptyAlphaStrGen.sampleValue}")
  }

  val elementXPathGen: Gen[String] = for (levels <- Gen.choose(0, 6)) yield {
    (0 to levels map (_ => nonEmptyAlphaStrGen.sampleValue)).mkString("/", "/", "")
  }

}