/*
 * Copyright 2023 HM Revenue & Customs
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
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class SecurityConsigneeSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "SecurityConsignee XML" - {

    "at GoodsItem level" - {

      "must deserialise" in {

        forAll(arbitrary[SecurityConsigneeWithoutEori]) {
          consignee =>
            val xml =
              <TRACONSECGOO013>
                <NamTRACONSECGOO017>{consignee.name}</NamTRACONSECGOO017>
                <StrNumTRACONSECGOO019>{consignee.streetAndNumber}</StrNumTRACONSECGOO019>
                <PosCodTRACONSECGOO018>{consignee.postCode}</PosCodTRACONSECGOO018>
                <CityTRACONSECGOO014>{consignee.city}</CityTRACONSECGOO014>
                <CouCodTRACONSECGOO015>{consignee.countryCode}</CouCodTRACONSECGOO015>
              </TRACONSECGOO013>

            val result = XmlReader.of[SecurityConsignee](SecurityConsignee.xmlReader).read(xml).toOption.value

            result mustBe consignee
        }
      }

      "must deserialise ConsignorWithEori" in {
        val eori = nonEmptyString.sample.value
        val xml =
          <TRACONSECGOO013>
            <TINTRACONSECGOO020>{eori}</TINTRACONSECGOO020>
          </TRACONSECGOO013>

        val result = XmlReader.of[SecurityConsignee](SecurityConsignee.xmlReader).read(xml).toOption.value

        result mustBe SecurityConsigneeWithEori(eori)
      }
    }

    "at root level" - {

      "must deserialise" in {

        forAll(arbitrary[SecurityConsigneeWithoutEori]) {
          consignee =>
            val xml =
              <TRACONSEC029>
                <NameTRACONSEC033>{consignee.name}</NameTRACONSEC033>
                <StrNumTRACONSEC035>{consignee.streetAndNumber}</StrNumTRACONSEC035>
                <PosCodTRACONSEC034>{consignee.postCode}</PosCodTRACONSEC034>
                <CitTRACONSEC030>{consignee.city}</CitTRACONSEC030>
                <CouCodTRACONSEC031>{consignee.countryCode}</CouCodTRACONSEC031>
              </TRACONSEC029>

            val result = XmlReader.of[SecurityConsignee](SecurityConsignee.xmlReaderRootLevel).read(xml).toOption.value

            result mustBe consignee
        }
      }

      "must deserialise ConsignorWithEori" in {
        val eori = nonEmptyString.sample.value
        val xml =
          <TRACONSEC029>
            <TINTRACONSEC036>{eori}</TINTRACONSEC036>
          </TRACONSEC029>

        val result = XmlReader.of[SecurityConsignee](SecurityConsignee.xmlReaderRootLevel).read(xml).toOption.value

        result mustBe SecurityConsigneeWithEori(eori)
      }
    }
  }
}
