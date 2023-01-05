/*
 * Copyright 2023 HM Revenue & Customs
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
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json

import scala.xml.NodeSeq

class TraderAtDestinationSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "Trader at Destination" - {

    "JSON" - {

      "must deserialise to a Trader at Destination with an EORI" - {

        "when all address fields are present" in {

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

            Json.toJson(
              TraderAtDestinationWithEori(eori, Some(name), Some(streetAndNumber), Some(postCode), Some(city), Some(countryCode)): TraderAtDestination
            ) mustEqual json
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

    "XML" - {

      "must deserialise to a Trader at Destination with an EORI" in {

        forAll(arbitrary[TraderAtDestinationWithEori]) {
          trader =>
            val xml =
              <TRADESTRD>
            {
                trader.name.fold(NodeSeq.Empty) {
                  name =>
                    <NamTRD7>{name}</NamTRD7>
                } ++
                  trader.streetAndNumber.fold(NodeSeq.Empty) {
                    streetNumber =>
                      <StrAndNumTRD22>{streetNumber}</StrAndNumTRD22>
                  } ++
                  trader.postCode.fold(NodeSeq.Empty) {
                    postcode =>
                      <PosCodTRD23>{postcode}</PosCodTRD23>
                  } ++
                  trader.city.fold(NodeSeq.Empty) {
                    city =>
                      <CitTRD24>{city}</CitTRD24>
                  } ++
                  trader.countryCode.fold(NodeSeq.Empty) {
                    countryCode =>
                      <CouTRD25>{countryCode}</CouTRD25>
                  }
              }
            <TINTRD59>{trader.eori}</TINTRD59>
          </TRADESTRD>

            val result = XmlReader.of[TraderAtDestination].read(xml).toOption.value

            result mustBe trader
        }

      }

      "must deserialise to Trader at Destination without an EORI" in {
        forAll(arbitrary[TraderAtDestinationWithoutEori]) {
          trader =>
            val xml =
              <TRADESTRD>
                <NamTRD7>{trader.name}</NamTRD7>
                <StrAndNumTRD22>{trader.streetAndNumber}</StrAndNumTRD22>
                <PosCodTRD23>{trader.postCode}</PosCodTRD23>
                <CitTRD24>{trader.city}</CitTRD24>
                <CouTRD25>{trader.countryCode}</CouTRD25>
              </TRADESTRD>

            val result = XmlReader.of[TraderAtDestination].read(xml).toOption.value

            result mustBe trader
        }
      }
    }
  }

}
