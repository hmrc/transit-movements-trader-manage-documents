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
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.{FreeSpec, MustMatchers}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsSuccess, Json}

class TraderAtDestinationSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators {

  "Trader at Destination" - {

    "must deserialise to a Trader at Destination with an EORI" - {

      "when all address fields are present" in {

        forAll(arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[String]) {
          (eori, name, streetAndNumber, postCode, city, countryCode)  =>

            val json = Json.obj(
              "eori"            -> eori,
              "name"            -> name,
              "streetAndNumber" -> streetAndNumber,
              "postCode"        -> postCode,
              "city"            -> city,
              "countryCode"     -> countryCode
            )

            val expectedResult = TraderAtDestinationWithEori(eori, Some(name), Some(streetAndNumber), Some(postCode), Some(city), Some(countryCode))

            json.validate[TraderAtDestination] mustEqual JsSuccess(expectedResult)
        }
      }

      "when only the EORI is present" in {

        forAll(arbitrary[String]) {
          eori =>

            val json = Json.obj("eori" -> eori)

            val expectedResult = TraderAtDestinationWithEori(eori, None, None, None, None, None)

            json.validate[TraderAtDestination] mustEqual JsSuccess(expectedResult)
        }
      }
    }

    "must deserialise to a Trader without EORI" in {

      forAll(arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[String]) {
        (name, streetAndNumber, postCode, city, countryCode) =>

          val json = Json.obj(
            "name"            -> name,
            "streetAndNumber" -> streetAndNumber,
            "postCode"        -> postCode,
            "city"            -> city,
            "countryCode"     -> countryCode
          )

          val expectedResult = TraderAtDestinationWithoutEori(name, streetAndNumber, postCode, city, countryCode)

          json.validate[TraderAtDestination] mustEqual JsSuccess(expectedResult)
      }
    }

    "must serialise from a Trader with EORI" in {

      forAll(arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[String]) {
        (eori, name, streetAndNumber, postCode, city, countryCode) =>

          val json = Json.obj(
            "eori"            -> eori,
            "name"            -> name,
            "streetAndNumber" -> streetAndNumber,
            "postCode"        -> postCode,
            "city"            -> city,
            "countryCode"     -> countryCode
          )

          Json.toJson(TraderAtDestinationWithEori(eori, Some(name), Some(streetAndNumber), Some(postCode), Some(city), Some(countryCode)): TraderAtDestination) mustEqual json
      }
    }

    "must serialise from a Trader without EORI" in {

      forAll(arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[String]) {
        (name, streetAndNumber, postCode, city, countryCode) =>

          val json = Json.obj(
            "name"            -> name,
            "streetAndNumber" -> streetAndNumber,
            "postCode"        -> postCode,
            "city"            -> city,
            "countryCode"     -> countryCode
          )

          Json.toJson(TraderAtDestinationWithoutEori(name, streetAndNumber, postCode, city, countryCode): TraderAtDestination) mustEqual json
      }
    }
  }
}
