/*
 * Copyright 2019 HM Revenue & Customs
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

import generators.ModelGenerators
import org.scalatest.{FreeSpec, MustMatchers}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsSuccess, Json}

class SpecialMentionSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators {

  "SpecialMentionEc" - {

    "must deserialise when `export from EC` is true" in {

      forAll(stringWithMaxLength(5)) {
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

      forAll(stringWithMaxLength(5)) {
        additionalInformation =>

          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> false
          )

          json.validate[SpecialMentionEc] mustEqual JsError("exportFromEc must be true")
      }
    }

    "must serialise" in  {

      forAll(stringWithMaxLength(5)) {
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

    "must deserialise when `export from EC` is false" in {

      forAll(stringWithMaxLength(5), stringWithMaxLength(2)) {
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

      forAll(stringWithMaxLength(5), stringWithMaxLength(2)) {
        case (additionalInformation, country) =>

          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> true,
            "exportFromCountry"          -> country
          )

          json.validate[SpecialMentionNonEc] mustEqual JsError("exportFromEc must be false")
      }
    }

    "must serialise" in  {

      forAll(stringWithMaxLength(5), stringWithMaxLength(2)) {
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

  "Special Mention" - {

    "must deserialise to a Special Mention EC" in {

      forAll(stringWithMaxLength(5)) {
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

      forAll(stringWithMaxLength(5), stringWithMaxLength(2)) {
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

    "must serialise from a Special Mention EC" in  {

      forAll(stringWithMaxLength(5)) {
        additionalInformation =>

          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> true
          )

          Json.toJson(SpecialMentionEc(additionalInformation): SpecialMention) mustEqual json
      }
    }

    "must serialise from a Special Mention Non-EC" in  {

      forAll(stringWithMaxLength(5), stringWithMaxLength(2)) {
        (additionalInformation, country) =>

          val json = Json.obj(
            "additionalInformationCoded" -> additionalInformation,
            "exportFromEc"               -> false,
            "exportFromCountry"          -> country
          )

          Json.toJson(SpecialMentionNonEc(additionalInformation, country): SpecialMention) mustEqual json
      }
    }
  }
}
