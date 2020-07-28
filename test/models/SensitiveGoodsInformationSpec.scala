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
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.xml.NodeSeq

class SensitiveGoodsInformationSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "SensitiveGoodsInformation" - {

    "XML" - {

      "must deserialise" in {

        forAll(arbitrary[SensitiveGoodsInformation]) {
          sensitiveGoodsInformation =>
            val xml = {
              <SGICODSD2>
                {
                  sensitiveGoodsInformation.goodsCode.fold(NodeSeq.Empty) { goodsCode =>
                    <SenGooCodSD22>{goodsCode}</SenGooCodSD22>
                  }
                }
                <SenQuaSD23>{sensitiveGoodsInformation.quantity}</SenQuaSD23>
              </SGICODSD2>
            }

            val result = XmlReader.of[SensitiveGoodsInformation].read(xml).toOption.value

            result mustBe sensitiveGoodsInformation
        }
      }

      "must fail to deserialise" in {

        val xml = <SGICODSD2></SGICODSD2>

        val result = XmlReader.of[SensitiveGoodsInformation].read(xml).toOption

        result mustBe None
      }
    }
  }

}
