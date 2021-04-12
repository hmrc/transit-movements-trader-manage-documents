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
                {
                consignor.name.fold(NodeSeq.Empty) { name =>
                  <NamTRACORSECGOO025>{name}</NamTRACORSECGOO025>
                } ++
                  consignor.streetAndNumber.fold(NodeSeq.Empty) { streetAndNumber =>
                    <StrNumTRACORSECGOO027>{streetAndNumber}</StrNumTRACORSECGOO027>
                  } ++
                  consignor.postCode.fold(NodeSeq.Empty) { postCode =>
                    <PosCodTRACORSECGOO026>{postCode}</PosCodTRACORSECGOO026>
                  } ++
                  consignor.city.fold(NodeSeq.Empty) { city =>
                    <CitTRACORSECGOO022>{city}</CitTRACORSECGOO022>
                  } ++
                  consignor.countryCode.fold(NodeSeq.Empty) { countryCode =>
                    <CouCodTRACORSECGOO023>{countryCode}</CouCodTRACORSECGOO023>
                  } ++
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
    }

    "at root level" - {

      "must deserialise" in {

        forAll(arbitrary[SecurityConsignor]) {
          consignor =>
            val xml = {
              <TRACORSEC037>{
                consignor.name.fold(NodeSeq.Empty) { name =>
                  <NamTRACORSEC041>{name}</NamTRACORSEC041>
                } ++
                consignor.streetAndNumber.fold(NodeSeq.Empty) { streetAndNumber =>
                  <StrNumTRACORSEC043>{streetAndNumber}</StrNumTRACORSEC043>
                } ++
                consignor.postCode.fold(NodeSeq.Empty) { postCode =>
                  <PosCodTRACORSEC042>{postCode}</PosCodTRACORSEC042>
                } ++
                consignor.city.fold(NodeSeq.Empty) { city =>
                  <CitTRACORSEC038>{city}</CitTRACORSEC038>
                } ++
                consignor.countryCode.fold(NodeSeq.Empty) { countryCode =>
                  <CouCodTRACORSEC039>{countryCode}</CouCodTRACORSEC039>
                } ++
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
    }
  }
}
