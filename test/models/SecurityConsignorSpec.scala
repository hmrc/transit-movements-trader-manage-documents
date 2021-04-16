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

package models

import com.lucidchart.open.xtract.XmlReader
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.xml.NodeSeq

class SecurityConsignorSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "SecurityConsignor XML" - {

    "at GoodsItem level" - {

      "must deserialise ConsignorWithoutEori" in {

        forAll(arbitrary[SecurityConsignorWithoutEori]) {
          consignor =>
            val xml = {
              <TRACORSECGOO021>
                <NamTRACORSECGOO025>{consignor.name}</NamTRACORSECGOO025>
                <StrNumTRACORSECGOO027>{consignor.streetAndNumber}</StrNumTRACORSECGOO027>
                <PosCodTRACORSECGOO026>{consignor.postCode}</PosCodTRACORSECGOO026>
                <CitTRACORSECGOO022>{consignor.city}</CitTRACORSECGOO022>
                <CouCodTRACORSECGOO023>{consignor.countryCode}</CouCodTRACORSECGOO023>
              </TRACORSECGOO021>
            }

            val result = XmlReader.of[SecurityConsignor](SecurityConsignor.xmlReader).read(xml).toOption.value

            result mustBe consignor
        }
      }

      "must deserialise ConsignorWithEori" in {
        val eori = nonEmptyString.sample.value
        val xml = {
          <TRACORSECGOO021>
                <TINTRACORSECGOO028>{eori}</TINTRACORSECGOO028>
              </TRACORSECGOO021>
        }

        val result = XmlReader.of[SecurityConsignor](SecurityConsignor.xmlReader).read(xml).toOption.value

        result mustBe SecurityConsignorWithEori(eori)
      }
    }
  }

  "at root level" - {

    "must deserialise" in {

      forAll(arbitrary[SecurityConsignorWithoutEori]) {
        consignor =>
          val xml = {
            <TRACORSEC037>
                <NamTRACORSEC041>{consignor.name}</NamTRACORSEC041>
                <StrNumTRACORSEC043>{consignor.streetAndNumber}</StrNumTRACORSEC043>
                <PosCodTRACORSEC042>{consignor.postCode}</PosCodTRACORSEC042>
                <CitTRACORSEC038>{consignor.city}</CitTRACORSEC038>
                <CouCodTRACORSEC039>{consignor.countryCode}</CouCodTRACORSEC039>
              </TRACORSEC037>
          }

          val result = XmlReader.of[SecurityConsignor](SecurityConsignor.xmlReaderRootLevel).read(xml).toOption.value

          result mustBe consignor
      }
    }

    "must deserialise ConsignorWithEori" in {
      val eori = nonEmptyString.sample.value
      val xml = {
        <TRACORSEC037>
            <TINTRACORSEC044>{eori}</TINTRACORSEC044>
          </TRACORSEC037>
      }

      val result = XmlReader.of[SecurityConsignor](SecurityConsignor.xmlReaderRootLevel).read(xml).toOption.value

      result mustBe SecurityConsignorWithEori(eori)
    }
  }
}
