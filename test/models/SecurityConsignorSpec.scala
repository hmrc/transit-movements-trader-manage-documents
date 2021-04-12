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

      "must deserialise" in {

        forAll(arbitrary[SecurityConsignor]) {
          consignor =>
            val xml = {
              <TRACORSECGOO021>
                <NamTRACORSECGOO025>{consignor.name}</NamTRACORSECGOO025>
                <StrNumTRACORSECGOO027>{consignor.streetAndNumber}</StrNumTRACORSECGOO027>
                <PosCodTRACORSECGOO026>{consignor.postCode}</PosCodTRACORSECGOO026>
                <CitTRACORSECGOO022>{consignor.city}</CitTRACORSECGOO022>
                <CouCodTRACORSECGOO023>{consignor.countryCode}</CouCodTRACORSECGOO023>
                {
                consignor.nadLanguageCode.fold(NodeSeq.Empty) { nadLangCode =>
                  <TRACORSECGOO021LNG>{nadLangCode}</TRACORSECGOO021LNG>
                } ++
                  consignor.eori.fold(NodeSeq.Empty) { eori =>
                    <TINTRACORSECGOO028>{eori}</TINTRACORSECGOO028>
                  }
                }
              </TRACORSECGOO021>
            }

            val result = XmlReader.of[SecurityConsignor](SecurityConsignor.xmlReader).read(xml).toOption.value

            result mustBe consignor
        }
      }

      "must fail to deserialise" in {

        val xml = <TRACORSECGOO021></TRACORSECGOO021>

        val result = XmlReader.of[SecurityConsignor](SecurityConsignor.xmlReader).read(xml).toOption

        result mustBe None
      }
    }

    "at root level" - {

      "must deserialise" in {

        forAll(arbitrary[SecurityConsignor]) {
          consignor =>
            val xml = {
              <TRACORSEC037>
                <NamTRACORSEC041>{consignor.name}</NamTRACORSEC041>
                <StrNumTRACORSEC043>{consignor.streetAndNumber}</StrNumTRACORSEC043>
                <PosCodTRACORSEC042>{consignor.postCode}</PosCodTRACORSEC042>
                <CitTRACORSEC038>{consignor.city}</CitTRACORSEC038>
                <CouCodTRACORSEC039>{consignor.countryCode}</CouCodTRACORSEC039>
                {
                consignor.nadLanguageCode.fold(NodeSeq.Empty) { nadLangCode =>
                  <TRACORSEC037LNG>{nadLangCode}</TRACORSEC037LNG>
                } ++
                  consignor.eori.fold(NodeSeq.Empty) { eori =>
                    <TINTRACORSEC044>{eori}</TINTRACORSEC044>
                  }
                }
              </TRACORSEC037>
            }

            val result = XmlReader.of[SecurityConsignor](SecurityConsignor.xmlReaderRootLevel).read(xml).toOption.value

            result mustBe consignor
        }
      }

      "must fail to deserialise" in {

        val xml = <TRACORSEC037></TRACORSEC037>

        val result = XmlReader.of[SecurityConsignor](SecurityConsignor.xmlReaderRootLevel).read(xml).toOption

        result mustBe None
      }
    }
  }
}
