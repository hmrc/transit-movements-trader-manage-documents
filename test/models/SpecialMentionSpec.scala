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
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json

import scala.xml.NodeSeq

class SpecialMentionSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "JSON" - {

    "must deserialise when `export from EC` is true and code is country specific" in {

      forAll(countrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEC"               -> true
          )

          val expectedMention = SpecialMention(None, additionalInformation, Some(true), None)

          json.validate[SpecialMention] mustEqual JsSuccess(expectedMention)
      }
    }

    "must deserialise when `export from EC` is false and code is country specific and a country is specified" in {

      forAll(countrySpecificCodeGen, stringWithMaxLength(2)) {
        case (additionalInformation, country) =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEC"               -> false,
            "exportFromCountry"          -> country
          )

          val expectedMention = SpecialMention(None, additionalInformation, Some(false), Some(country))

          json.validate[SpecialMention] mustEqual JsSuccess(expectedMention)
      }
    }

    "must deserialise when the code is not country specific and there is an additionalInformation" in {

      forAll(nonCountrySpecificCodeGen, nonEmptyString) {
        (additionalInformationCoded, additionalInformation) =>
          val json = Json.obj(
            "additionalInformation"      -> additionalInformation,
            "additionalInformationCoded" -> additionalInformationCoded
          )

          val expectedMention = SpecialMention(Some(additionalInformation), additionalInformationCoded, None, None)

          json.validate[SpecialMention] mustEqual JsSuccess(expectedMention)
      }
    }

    "must fail to deserialise if data cannot be parsed" in {

      forAll(countrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEC"               -> additionalInformation
          )

          json.validate[SpecialMention] mustBe a[JsError]
      }
    }
  }

  "XML" - {

    "must deserialise from an xml" in {

      forAll(arbitrary[SpecialMention]) {
        specialMention =>
          val xml = {
            <SPEMENMT2>
                {
                  specialMention.additionalInformation.fold(NodeSeq.Empty) { ai =>
                    <AddInfMT21>{ai}</AddInfMT21>
                  } ++
                  NodeSeq.fromSeq(Seq(<AddInfCodMT23>{specialMention.additionalInformationCoded}</AddInfCodMT23>)) ++
                  specialMention.exportFromEC.fold(NodeSeq.Empty) { efec =>
                    <ExpFroECMT24>{if(efec) 1 else 0}</ExpFroECMT24>
                  } ++
                  specialMention.exportFromCountry.fold(NodeSeq.Empty) { efc =>
                    <ExpFroCouMT25>{efc}</ExpFroCouMT25>
                  }
                }
              </SPEMENMT2>
          }

          val result = XmlReader.of[SpecialMention].read(xml).toOption.value

          result mustBe specialMention
      }
    }
    "must fail to deserialize if a field is invalid" in {
      val xml = {
        <SPEMENT2>
          <AddInfCodMT23>12345</AddInfCodMT23>
          <ExpFroECMT24>Invalid Value</ExpFroECMT24>
        </SPEMENT2>
      }

      val result = XmlReader.of[SpecialMention].read(xml).toOption

      result mustBe None
    }
    "must fail to deserialize if no additionalInformationCoded is present" in {
      val xml = {
        <SPEMENT2>
          <ExpFroECMT24>1</ExpFroECMT24>
        </SPEMENT2>
      }

      val result = XmlReader.of[SpecialMention].read(xml).toOption

      result mustBe None
    }
  }
}
