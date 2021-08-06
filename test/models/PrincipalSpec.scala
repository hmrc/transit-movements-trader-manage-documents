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

class PrincipalSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "Principal" - {
    "XML" - {

      "must deserialise" in {
        forAll(arbitrary[Principal]) {
          principal =>
            val xml =
              <TRAPRIPC1>
                <NamPC17>{principal.name}</NamPC17>
                <StrAndNumPC122>{principal.streetAndNumber}</StrAndNumPC122>
                <PosCodPC123>{principal.postCode}</PosCodPC123>
                <CitPC124>{principal.city}</CitPC124>
                <CouPC125>{principal.countryCode}</CouPC125>
                {
                principal.eori.fold(NodeSeq.Empty) {
                  eori =>
                    <TINPC159>{eori}</TINPC159>
                } ++
                  principal.tir.fold(NodeSeq.Empty) {
                    tir =>
                      <HITPC126>{tir}</HITPC126>
                  }
              }
              </TRAPRIPC1>

            val result = XmlReader.of[Principal].read(xml).toOption.value

            result mustBe principal
        }
      }

      "must fail to deserialise when a mandatory field is missing" in {
        val xml =
          <TRAPRIPC1>
            <NamPC17></NamPC17>
          </TRAPRIPC1>

        val result = XmlReader.of[Principal].read(xml).toOption

        result mustBe None
      }
    }
  }

}
