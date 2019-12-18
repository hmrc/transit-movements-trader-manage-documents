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

package connectors

import com.github.tomakehurst.wiremock.client.WireMock._
import generators.ReferenceModelGenerators
import models.reference.AdditionalInformation
import models.reference.Country
import models.reference.DocumentType
import models.reference.KindOfPackage
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier
import utils.WireMockHelper

import scala.concurrent.ExecutionContext.Implicits.global

class ReferenceDataConnectorSpec
    extends FreeSpec
    with MustMatchers
    with GuiceOneAppPerSuite
    with WireMockHelper
    with ScalaFutures
    with IntegrationPatience
    with ScalaCheckPropertyChecks
    with ReferenceModelGenerators {

  implicit lazy val arbitraryHC: Arbitrary[HeaderCarrier] =
    Arbitrary(Gen.const(HeaderCarrier()))

  implicit override lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(
        "microservice.services.transit-movements-trader-reference-data.port" -> server.port
      )
      .build()

  private lazy val connector: ReferenceDataConnector = app.injector.instanceOf[ReferenceDataConnector]

  private val errorStatuses = Gen.chooseNum(201, 599, 400, 499, 500, 501, 502, 503)
  "countries" - {

    "must return a sequence of countries" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[Country]]) {
        (hc, countries) =>
          server.stubFor(
            get(urlEqualTo("/transit-movements-trader-reference-data/countries-full-list"))
              .willReturn(
                ok(Json.toJson(countries).toString)
              )
          )

          whenReady(connector.countries()(implicitly, hc)) {
            result =>
              result mustEqual countries
          }
      }
    }

    "must throw an exception" - {

      "when the server returns a non 200 status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo("/transit-movements-trader-reference-data/countries-full-list"))
                .willReturn(
                  status(returnStatus)
                )
            )

            whenReady(connector.countries()(implicitly, hc).failed) {
              _ mustBe an[Exception]
            }
        }
      }
    }
  }

  "kinds of package" - {

    "must return a sequence of kinds of package" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[KindOfPackage]]) {
        (hc, kindsOfPackage) =>
          server.stubFor(
            get(urlEqualTo("/transit-movements-trader-reference-data/kinds-of-package"))
              .willReturn(
                ok(Json.toJson(kindsOfPackage).toString)
              )
          )

          whenReady(connector.kindsOfPackage()(implicitly, hc)) {
            result =>
              result mustEqual kindsOfPackage
          }
      }
    }

    "must throw an exception" - {

      "when the server returns a non 200 status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo("/transit-movements-trader-reference-data/kinds-of-package"))
                .willReturn(
                  status(returnStatus)
                )
            )

            whenReady(connector.kindsOfPackage()(implicitly, hc).failed) {
              _ mustBe an[Exception]
            }
        }
      }
    }
  }

  "document types" - {

    "must return a sequence of kinds of package" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[DocumentType]]) {
        (hc, documentTypes) =>
          server.stubFor(
            get(urlEqualTo("/transit-movements-trader-reference-data/document-types"))
              .willReturn(
                ok(Json.toJson(documentTypes).toString)
              )
          )

          whenReady(connector.documentTypes()(implicitly, hc)) {
            result =>
              result mustEqual documentTypes
          }
      }
    }

    "must throw an exception" - {

      "when the server returns a non 200 status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo("/transit-movements-trader-reference-data/document-types"))
                .willReturn(
                  status(returnStatus)
                )
            )

            whenReady(connector.documentTypes()(implicitly, hc).failed) {
              _ mustBe an[Exception]
            }
        }
      }
    }
  }

  "additional information" - {

    "must return a sequence of additional information" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[AdditionalInformation]]) {
        (hc, additionalInformations) =>
          server.stubFor(
            get(urlEqualTo("/transit-movements-trader-reference-data/additional-information"))
              .willReturn(
                ok(Json.toJson(additionalInformations).toString)
              )
          )

          whenReady(connector.additionalInformation()(implicitly, hc)) {
            result =>
              result mustEqual additionalInformations
          }
      }
    }

    "must throw an exception" - {

      "when the server returns a non 200 status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo("/transit-movements-trader-reference-data/additional-information"))
                .willReturn(
                  status(returnStatus)
                )
            )

            whenReady(connector.additionalInformation()(implicitly, hc).failed) {
              _ mustBe an[Exception]
            }
        }
      }
    }
  }
}
