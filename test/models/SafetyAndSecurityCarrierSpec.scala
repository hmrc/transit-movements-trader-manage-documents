/*
 * Copyright 2022 HM Revenue & Customs
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

class SafetyAndSecurityCarrierSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "SafetyAndSecurityCarrier XML" - {

    "must deserialise" in {

      forAll(arbitrary[SafetyAndSecurityCarrierWithoutEori]) {
        consignee =>
          val xml =
            <CARTRA100>
                <NamCARTRA121>{consignee.name}</NamCARTRA121>
                <StrAndNumCARTRA254>{consignee.streetAndNumber}</StrAndNumCARTRA254>
                <PosCodCARTRA121>{consignee.postCode}</PosCodCARTRA121>
                <CitCARTRA789>{consignee.city}</CitCARTRA789>
                <CouCodCARTRA587>{consignee.countryCode}</CouCodCARTRA587>
              </CARTRA100>

          val result = XmlReader.of[SafetyAndSecurityCarrier](SafetyAndSecurityCarrier.xmlReader).read(xml).toOption.value

          result mustBe consignee
      }
    }

    "must deserialise SafetyAndSecurityCarrierWithEori" in {
      val eori = nonEmptyString.sample.value
      val xml =
        <CARTRA100>
            <TINCARTRA254>{eori}</TINCARTRA254>
          </CARTRA100>

      val result = XmlReader.of[SafetyAndSecurityCarrier](SafetyAndSecurityCarrier.xmlReader).read(xml).toOption.value

      result mustBe SafetyAndSecurityCarrierWithEori(eori)
    }
  }
}
