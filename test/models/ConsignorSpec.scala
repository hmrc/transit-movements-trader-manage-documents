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
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalacheck.Arbitrary.arbitrary

import scala.xml.NodeSeq

class ConsignorSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "Consignor XML" - {

    "at GoodsItem level" - {

      "must deserialise" in {

        forAll(arbitrary[Consignor]) {
          consignor =>
            val xml =
              <TRACONCO2>
                <NamCO27>{consignor.name}</NamCO27>
                <StrAndNumCO222>{consignor.streetAndNumber}</StrAndNumCO222>
                <PosCodCO223>{consignor.postCode}</PosCodCO223>
                <CitCO224>{consignor.city}</CitCO224>
                <CouCO225>{consignor.countryCode}</CouCO225>
                {
                consignor.nadLanguageCode.fold(NodeSeq.Empty) {
                  nadLangCode =>
                    <NADLNGGTCO>{nadLangCode}</NADLNGGTCO>
                } ++
                  consignor.eori.fold(NodeSeq.Empty) {
                    eori =>
                      <TINCO259>{eori}</TINCO259>
                  }
              }
              </TRACONCO2>

            val result = XmlReader.of[Consignor](Consignor.xmlReaderGoodsLevel).read(xml).toOption.value

            result mustBe consignor
        }
      }

      "must fail to deserialise" in {

        val xml = <TRACONCO2></TRACONCO2>

        val result = XmlReader.of[Consignor](Consignor.xmlReaderGoodsLevel).read(xml).toOption

        result mustBe None
      }
    }

    "at root level" - {

      "must deserialise" in {

        forAll(arbitrary[Consignor]) {
          consignor =>
            val xml =
              <TRACONCO1>
                <NamCO17>{consignor.name}</NamCO17>
                <StrAndNumCO122>{consignor.streetAndNumber}</StrAndNumCO122>
                <PosCodCO123>{consignor.postCode}</PosCodCO123>
                <CitCO124>{consignor.city}</CitCO124>
                <CouCO125>{consignor.countryCode}</CouCO125>
                {
                consignor.nadLanguageCode.fold(NodeSeq.Empty) {
                  nadLangCode =>
                    <NADLNGCO>{nadLangCode}</NADLNGCO>
                } ++
                  consignor.eori.fold(NodeSeq.Empty) {
                    eori =>
                      <TINCO159>{eori}</TINCO159>
                  }
              }
              </TRACONCO1>

            val result = XmlReader.of[Consignor](Consignor.xmlReaderRootLevel).read(xml).toOption.value

            result mustBe consignor
        }
      }

      "must fail to deserialise" in {

        val xml = <TRACONCO1></TRACONCO1>

        val result = XmlReader.of[Consignor](Consignor.xmlReaderRootLevel).read(xml).toOption

        result mustBe None
      }
    }
  }

}
