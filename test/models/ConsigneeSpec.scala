/*
 * Copyright 2020 HM Revenue & Customs
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
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalacheck.Arbitrary.arbitrary

import scala.xml.NodeSeq

class ConsigneeSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "Consignee" - {

    "XML" - {

      "must deserialise" in {

        forAll(arbitrary[Consignee]) {
          consignee =>
            val xml = {
              <TRACONCE1>
                <NamCE17>{consignee.name}</NamCE17>
                <StrAndNumCE122>{consignee.streetAndNumber}</StrAndNumCE122>
                <PosCodCE123>{consignee.postCode}</PosCodCE123>
                <CitCE124>{consignee.city}</CitCE124>
                <CouCE125>{consignee.countryCode}</CouCE125>
                {
                  consignee.nadLanguageCode.fold(NodeSeq.Empty) { nadLangCode =>
                    <NADLNGCE>{nadLangCode}</NADLNGCE>
                  } ++
                  consignee.eori.fold(NodeSeq.Empty) { eori =>
                    <TINCE159>{eori}</TINCE159>
                  }
                }
              </TRACONCE1>
            }

            val result = XmlReader.of[Consignee].read(xml).toOption.value

            result mustBe consignee
        }
      }

      "must fail to deserialise" in {

        val xml = <TRACONCO1></TRACONCO1>

        val result = XmlReader.of[Consignee].read(xml).toOption

        result mustBe None
      }
    }
  }

}
