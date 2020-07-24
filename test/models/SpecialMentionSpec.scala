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

import java.time.LocalDate

import com.lucidchart.open.xtract.XmlReader
import generators.ModelGenerators
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json
import org.scalacheck.Arbitrary.arbitrary

class SpecialMentionSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "SpecialMentionEc" - {

    "must deserialise when `export from EC` is true and code is country specific" in {

      forAll(countrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> true
          )

          val expectedMention = SpecialMentionEc(additionalInformation)

          json.validate[SpecialMentionEc] mustEqual JsSuccess(expectedMention)
      }
    }

    "must fail to deserialise when `export from EC` is false" in {

      forAll(countrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> false
          )

          json.validate[SpecialMentionEc] mustEqual JsError("exportFromEc must be true")
      }
    }

    "must fail to deserialise when the code is not country-specific" in {

      forAll(nonCountrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> true
          )

          json.validate[SpecialMentionEc] mustBe a[JsError]
      }
    }

    "must serialise" in {

      forAll(countrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> true
          )

          Json.toJson(SpecialMentionEc(additionalInformation))(SpecialMentionEc.writes) mustEqual json
      }
    }
  }

  "SpecialMentionOutsideEc" - {

    "must deserialise when `export from EC` is false and code is country specific" in {

      forAll(countrySpecificCodeGen, stringWithMaxLength(2)) {
        case (additionalInformation, country) =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> false,
            "exportFromCountry"          -> country
          )

          val expectedMention = SpecialMentionNonEc(additionalInformation, country)

          json.validate[SpecialMentionNonEc] mustEqual JsSuccess(expectedMention)
      }
    }

    "must fail to deserialise when 'export from EC` is true" in {

      forAll(countrySpecificCodeGen, stringWithMaxLength(2)) {
        case (additionalInformation, country) =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> true,
            "exportFromCountry"          -> country
          )

          json.validate[SpecialMentionNonEc] mustEqual JsError("exportFromEc must be false")
      }
    }

    "must fail to deserialise when code is not country specific" in {

      forAll(nonCountrySpecificCodeGen, stringWithMaxLength(2)) {
        case (additionalInformation, country) =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> false,
            "exportFromCountry"          -> country
          )

          json.validate[SpecialMentionEc] mustBe a[JsError]
      }
    }

    "must serialise" in {

      forAll(countrySpecificCodeGen, stringWithMaxLength(2)) {
        (additionalInformation, country) =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> false,
            "exportFromCountry"          -> country
          )

          Json.toJson(SpecialMentionNonEc(additionalInformation, country))(SpecialMentionNonEc.writes) mustEqual json
      }
    }
  }

  "Special Mention No Country" - {

    "must deserialise when the code is not country specific" in {

      forAll(nonCountrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation
          )

          val expectedMention = SpecialMentionNoCountry(additionalInformation)

          json.validate[SpecialMentionNoCountry] mustEqual JsSuccess(expectedMention)
      }
    }

    "must fail to deserialise when the code is country specific" in {

      forAll(countrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation
          )

          json.validate[SpecialMentionNoCountry] mustBe a[JsError]
      }
    }

    "must serialise" in {

      forAll(nonCountrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation
          )

          Json.toJson(SpecialMentionNoCountry(additionalInformation))(SpecialMentionNoCountry.writes) mustEqual json
      }
    }
  }

  "Special Mention" - {

    "must deserialise to a Special Mention EC" in {

      forAll(countrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> true
          )

          val expectedMention = SpecialMentionEc(additionalInformation)

          json.validate[SpecialMention] mustEqual JsSuccess(expectedMention)
      }
    }

    "must deserialise to a Special Mention Non-EC" in {

      forAll(countrySpecificCodeGen, stringWithMaxLength(2)) {
        case (additionalInformation, country) =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> false,
            "exportFromCountry"          -> country
          )

          val expectedMention = SpecialMentionNonEc(additionalInformation, country)

          json.validate[SpecialMention] mustEqual JsSuccess(expectedMention)
      }
    }

    "must deserialise to a Special Mention No Country" in {

      forAll(nonCountrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation
          )

          val expectedMention = SpecialMentionNoCountry(additionalInformation)

          json.validate[SpecialMention] mustEqual JsSuccess(expectedMention)
      }
    }

    "must serialise from a Special Mention EC" in {

      forAll(countrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> true
          )

          Json.toJson(SpecialMentionEc(additionalInformation): SpecialMention) mustEqual json
      }
    }

    "must serialise from a Special Mention Non-EC" in {

      forAll(countrySpecificCodeGen, stringWithMaxLength(2)) {
        (additionalInformation, country) =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> false,
            "exportFromCountry"          -> country
          )

          Json.toJson(SpecialMentionNonEc(additionalInformation, country): SpecialMention) mustEqual json
      }
    }

    "must serialise from a Special Mention No Country" in {

      forAll(nonCountrySpecificCodeGen) {
        additionalInformation =>
          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation
          )

          Json.toJson(SpecialMentionNoCountry(additionalInformation): SpecialMention) mustEqual json
      }
    }
  }

  "XML" - {

    "SpecialMentionEc" - {

      "must deserialise when `export from EC` is true and code is country specific" in {

        forAll(arbitrary[SpecialMentionEc]) {
          specialMentionEc =>
            val xml = {
              <SPEMENMT2>
                <AddInfCodMT23>{specialMentionEc.additionalInformationCoded}</AddInfCodMT23>
                <ExpFroECMT24>1</ExpFroECMT24>
              </SPEMENMT2>
            }

            val result = XmlReader.of[SpecialMentionEc].read(xml).toOption.value

            result mustBe specialMentionEc
        }
      }

      "must fail to deserialise when 'export from EC` is false" in {
        forAll(arbitrary[SpecialMentionEc]) {
          specialMentionEc =>
            val xml = {
              <SPEMENMT2>
                <AddInfCodMT23>{specialMentionEc.additionalInformationCoded}</AddInfCodMT23>
                <ExpFroECMT24>0</ExpFroECMT24>
              </SPEMENMT2>
            }

            val result = XmlReader.of[SpecialMentionEc].read(xml).toOption

            result mustBe None
        }
      }

      "must fail to deserialise when code is not country specific" in {

        val xml = {
          <SPEMENMT2>
            <AddInfCodMT23>Invalid code</AddInfCodMT23>
            <ExpFroECMT24>1</ExpFroECMT24>
          </SPEMENMT2>
        }

        val result = XmlReader.of[SpecialMentionEc].read(xml).toOption

        result mustBe None
      }
    }
  }

}
