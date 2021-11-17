/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils

import com.lucidchart.open.xtract.ParseFailure
import com.lucidchart.open.xtract.XmlReader
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import utils.BigDecimalXMLReader._

class BigDecimalXMLReaderSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "BigDecimalXMLReader" - {

    "must deserialize XML to BigDecimal with correct format" in {

      forAll(arbitrary[BigDecimal]) {
        bigDecimal =>
          val xml = <testXml>{bigDecimal}</testXml>

          val result = XmlReader.of[BigDecimal].read(xml).toOption.value

          result mustBe bigDecimal
      }
    }

    "must return ParseFailure when failing to deserialize XML to BigDecimal" in {

      val xml = <testXml>Invalid value</testXml>

      val result = XmlReader.of[BigDecimal].read(xml)

      result mustBe an[ParseFailure]
    }
  }

}
