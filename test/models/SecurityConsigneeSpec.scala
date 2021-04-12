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

class SecurityConsigneeSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "SecurityConsignee XML" - {

    "at GoodsItem level" - {

      "must deserialise" in {

        forAll(arbitrary[SecurityConsignee]) {
          consignee =>
            val xml = {
              <TRACONSECGOO013>
                {
                consignee.name.fold(NodeSeq.Empty) { name =>
                <NamTRACONSECGOO017>{name}</NamTRACONSECGOO017>
                } ++
                consignee.streetAndNumber.fold(NodeSeq.Empty) { streetAndNumber =>
                  <StrNumTRACONSECGOO019>{streetAndNumber}</StrNumTRACONSECGOO019>
                } ++
                consignee.postCode.fold(NodeSeq.Empty) { postCode =>
                  <PosCodTRACONSECGOO018>{postCode}</PosCodTRACONSECGOO018>
                } ++
                consignee.city.fold(NodeSeq.Empty) { city =>
                  <CityTRACONSECGOO014>{city}</CityTRACONSECGOO014>
                } ++
                consignee.countryCode.fold(NodeSeq.Empty) { countryCode =>
                  <CouCodTRACONSECGOO015>{countryCode}</CouCodTRACONSECGOO015>
                } ++
                consignee.nadLanguageCode.fold(NodeSeq.Empty) { nadLangCode =>
                  <TRACONSECGOO013LNG>{nadLangCode}</TRACONSECGOO013LNG>
                } ++
                consignee.eori.fold(NodeSeq.Empty) { eori =>
                  <TINTRACONSECGOO020>{eori}</TINTRACONSECGOO020>
                }
                }
              </TRACONSECGOO013>
            }

            val result = XmlReader.of[SecurityConsignee](SecurityConsignee.xmlReader).read(xml).toOption.value

            result mustBe consignee
        }
      }
    }

    "at root level" - {

      "must deserialise" in {

        forAll(arbitrary[SecurityConsignee]) {
          consignee =>
            val xml = {
              <TRACONSEC029>
                {consignee.name.fold(NodeSeq.Empty) { name =>
                <NameTRACONSEC033>{name}</NameTRACONSEC033>
              } ++
                consignee.streetAndNumber.fold(NodeSeq.Empty) { streetAndNumber =>
                  <StrNumTRACONSEC035>{streetAndNumber}</StrNumTRACONSEC035>
                } ++
                consignee.postCode.fold(NodeSeq.Empty) { postCode =>
                  <PosCodTRACONSEC034>{postCode}</PosCodTRACONSEC034>
                } ++
                consignee.city.fold(NodeSeq.Empty) { city =>
                  <CitTRACONSEC030>{city}</CitTRACONSEC030>
                } ++
                consignee.countryCode.fold(NodeSeq.Empty) { countryCode =>
                  <CouCodTRACONSEC031>{countryCode}</CouCodTRACONSEC031>
                } ++
                consignee.nadLanguageCode.fold(NodeSeq.Empty) { nadLangCode =>
                  <TRACONSEC029LNG>{nadLangCode}</TRACONSEC029LNG>
                } ++
                  consignee.eori.fold(NodeSeq.Empty) { eori =>
                    <TINTRACONSEC036>{eori}</TINTRACONSEC036>
                  }
              }
              </TRACONSEC029>
            }

            val result = XmlReader.of[SecurityConsignee](SecurityConsignee.xmlReaderRootLevel).read(xml).toOption.value

            result mustBe consignee
        }
      }
    }
  }
}
