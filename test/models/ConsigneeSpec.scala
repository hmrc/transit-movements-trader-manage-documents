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
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.xml.NodeSeq

class ConsigneeSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "Consignee XML" - {

    "at GoodsItem level" - {

      "must deserialise" in {

        forAll(arbitrary[Consignee]) {
          consignee =>
            val xml =
              <TRACONCE2>
                <NamCE27>{consignee.name}</NamCE27>
                <StrAndNumCE222>{consignee.streetAndNumber}</StrAndNumCE222>
                <PosCodCE223>{consignee.postCode}</PosCodCE223>
                <CitCE224>{consignee.city}</CitCE224>
                <CouCE225>{consignee.countryCode}</CouCE225>
                {
                consignee.nadLanguageCode.fold(NodeSeq.Empty) {
                  nadLangCode =>
                    <NADLNGGICE>{nadLangCode}</NADLNGGICE>
                } ++
                  consignee.eori.fold(NodeSeq.Empty) {
                    eori =>
                      <TINCE259>{eori}</TINCE259>
                  }
              }
              </TRACONCE2>

            val result = XmlReader.of[Consignee](Consignee.xmlReaderGoodsLevel).read(xml).toOption.value

            result mustBe consignee
        }
      }

      "must fail to deserialise" in {

        val xml = <TRACONCE2></TRACONCE2>

        val result = XmlReader.of[Consignee](Consignee.xmlReaderGoodsLevel).read(xml).toOption

        result mustBe None
      }
    }

    "at root level" - {

      "must deserialise" in {

        forAll(arbitrary[Consignee]) {
          consignee =>
            val xml =
              <TRACONCE1>
                <NamCE17>{consignee.name}</NamCE17>
                <StrAndNumCE122>{consignee.streetAndNumber}</StrAndNumCE122>
                <PosCodCE123>{consignee.postCode}</PosCodCE123>
                <CitCE124>{consignee.city}</CitCE124>
                <CouCE125>{consignee.countryCode}</CouCE125>
                {
                consignee.nadLanguageCode.fold(NodeSeq.Empty) {
                  nadLangCode =>
                    <NADLNGCE>{nadLangCode}</NADLNGCE>
                } ++
                  consignee.eori.fold(NodeSeq.Empty) {
                    eori =>
                      <TINCE159>{eori}</TINCE159>
                  }
              }
              </TRACONCE1>

            val result = XmlReader.of[Consignee](Consignee.xmlReaderRootLevel).read(xml).toOption.value

            result mustBe consignee
        }
      }

      "must fail to deserialise" in {

        val xml = <TRACONCE1></TRACONCE1>

        val result = XmlReader.of[Consignee](Consignee.xmlReaderRootLevel).read(xml).toOption

        result mustBe None
      }
    }
  }

}
