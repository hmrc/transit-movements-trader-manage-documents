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
          consignor =>
            val xml = {
              <TRACONSECGOO013>
                <NamTRACONSECGOO017>{consignor.name}</NamTRACONSECGOO017>
                <StrNumTRACONSECGOO019>{consignor.streetAndNumber}</StrNumTRACONSECGOO019>
                <PosCodTRACONSECGOO018>{consignor.postCode}</PosCodTRACONSECGOO018>
                <CityTRACONSECGOO014>{consignor.city}</CityTRACONSECGOO014>
                <CouCodTRACONSECGOO015>{consignor.countryCode}</CouCodTRACONSECGOO015>
                {
                consignor.nadLanguageCode.fold(NodeSeq.Empty) { nadLangCode =>
                  <TRACONSECGOO013LNG>{nadLangCode}</TRACONSECGOO013LNG>
                } ++
                  consignor.eori.fold(NodeSeq.Empty) { eori =>
                    <TINTRACONSECGOO020>{eori}</TINTRACONSECGOO020>
                  }
                }
              </TRACONSECGOO013>
            }

            val result = XmlReader.of[SecurityConsignee](SecurityConsignee.xmlReader).read(xml).toOption.value

            result mustBe consignor
        }
      }

      "must fail to deserialise" in {

        val xml = <TRACONSECGOO013></TRACONSECGOO013>

        val result = XmlReader.of[SecurityConsignee](SecurityConsignee.xmlReader).read(xml).toOption

        result mustBe None
      }
    }

    "at root level" - {

      "must deserialise" in {

        forAll(arbitrary[SecurityConsignee]) {
          consignee =>
            val xml = {
              <TRACONSEC029>
                <NameTRACONSEC033>{consignee.name}</NameTRACONSEC033>
                <StrNumTRACONSEC035>{consignee.streetAndNumber}</StrNumTRACONSEC035>
                <PosCodTRACONSEC034>{consignee.postCode}</PosCodTRACONSEC034>
                <CitTRACONSEC030>{consignee.city}</CitTRACONSEC030>
                <CouCodTRACONSEC031>{consignee.countryCode}</CouCodTRACONSEC031>
                {
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

      "must fail to deserialise" in {

        val xml = <TRACONSEC029></TRACONSEC029>

        val result = XmlReader.of[SecurityConsignee](SecurityConsignee.xmlReaderRootLevel).read(xml).toOption

        result mustBe None
      }
    }
  }
}
