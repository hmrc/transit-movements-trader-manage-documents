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

package services

import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.status
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import generators.ReferenceModelGenerators
import models.reference._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.JsPath
import play.api.libs.json.Json
import play.api.libs.json.JsonValidationError
import uk.gov.hmrc.http.HeaderCarrier
import utils.WireMockHelper

import scala.concurrent.ExecutionContext.Implicits.global

class ReferenceDataServiceSpec
    extends FreeSpec
    with MustMatchers
    with GuiceOneAppPerSuite
    with WireMockHelper
    with ScalaFutures
    with IntegrationPatience
    with ScalaCheckPropertyChecks
    with ReferenceModelGenerators
    with ValidatedMatchers
    with ValidatedValues {

  implicit lazy val arbitraryHC: Arbitrary[HeaderCarrier] =
    Arbitrary(Gen.const(HeaderCarrier()))

  implicit override lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(
        "microservice.services.transit-movements-trader-reference-data.port" -> server.port
      )
      .build()

  private lazy val service: ReferenceDataService = app.injector.instanceOf[ReferenceDataService]

  private val errorStatuses = Gen.chooseNum(400, 599, 400, 499, 500, 501, 502, 503)

  "countries" - {

    val countriesUrl = "/transit-movements-trader-reference-data/countries-full-list"

    "must return a sequence of countries" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[Country]]) {
        (hc, countries) =>
          server.stubFor(
            get(urlEqualTo(countriesUrl))
              .willReturn(
                ok(Json.toJson(countries).toString)
              )
          )

          whenReady(service.countries()(implicitly, hc)) {
            result =>
              result.valid.value mustEqual countries
          }
      }
    }

    "must return a Reference Data Retrieval Error" - {

      "when the server returns a 4xx or 5xx status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo(countriesUrl))
                .willReturn(
                  status(returnStatus).withBody("body")
                )
            )

            whenReady(service.countries()(implicitly, hc)) {
              result =>
                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataRetrievalError("country", returnStatus, "body"))
            }
        }
      }
    }

    "must return a Json Validation Error" - {

      "when the server returns data that cannot be read as a sequence of countries" in {

        forAll(arbitrary[HeaderCarrier]) {
          hc =>
            val invalidJson = Json.obj("foo" -> "bar")

            server.stubFor(
              get(urlEqualTo(countriesUrl))
                .willReturn(
                  ok(invalidJson.toString)
                )
            )

            whenReady(service.countries()(implicitly, hc)) {
              result =>
                val expectedError = (JsPath, Seq(JsonValidationError("error.expected.jsarray")))

                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(JsonError("country", Seq(expectedError)))
            }
        }
      }
    }
  }

  "kindsOfPackage" - {

    val kindsOfPackageUrl = "/transit-movements-trader-reference-data/kinds-of-package"

    "must return a sequence of kinds of package" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[KindOfPackage]]) {
        (hc, kindsOfPackage) =>
          server.stubFor(
            get(urlEqualTo(kindsOfPackageUrl))
              .willReturn(
                ok(Json.toJson(kindsOfPackage).toString)
              )
          )

          whenReady(service.kindsOfPackage()(implicitly, hc)) {
            result =>
              result.valid.value mustEqual kindsOfPackage
          }
      }
    }

    "must return a Reference Data Retrieval Error" - {

      "when the server returns a 4xx or 5xx status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo(kindsOfPackageUrl))
                .willReturn(
                  status(returnStatus).withBody("body")
                )
            )

            whenReady(service.kindsOfPackage()(implicitly, hc)) {
              result =>
                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataRetrievalError("kindOfPackage", returnStatus, "body"))
            }
        }
      }
    }

    "when the server returns data that cannot be read as a sequence of kinds of package" in {

      forAll(arbitrary[HeaderCarrier]) {
        hc =>
          val invalidJson = Json.obj("foo" -> "bar")

          server.stubFor(
            get(urlEqualTo(kindsOfPackageUrl))
              .willReturn(
                ok(invalidJson.toString)
              )
          )

          whenReady(service.kindsOfPackage()(implicitly, hc)) {
            result =>
              val expectedError = (JsPath, Seq(JsonValidationError("error.expected.jsarray")))

              result.invalidValue.toChain.toList must contain theSameElementsAs Seq(JsonError("kindOfPackage", Seq(expectedError)))
          }
      }
    }
  }

  "documentTypes" - {

    val documentTypesUrl = "/transit-movements-trader-reference-data/document-types"

    "must return a sequence of kinds of package" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[DocumentType]]) {
        (hc, documentTypes) =>
          server.stubFor(
            get(urlEqualTo(documentTypesUrl))
              .willReturn(
                ok(Json.toJson(documentTypes).toString)
              )
          )

          whenReady(service.documentTypes()(implicitly, hc)) {
            result =>
              result.valid.value mustEqual documentTypes
          }
      }
    }

    "must return a Reference Data Retrieval Error" - {

      "when the server returns a 4xx or 5xx status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo(documentTypesUrl))
                .willReturn(
                  status(returnStatus).withBody("body")
                )
            )

            whenReady(service.documentTypes()(implicitly, hc)) {
              result =>
                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataRetrievalError("documentType", returnStatus, "body"))
            }
        }
      }
    }

    "when the server returns data that cannot be read as a sequence of kinds of package" in {

      forAll(arbitrary[HeaderCarrier]) {
        hc =>
          val invalidJson = Json.obj("foo" -> "bar")

          server.stubFor(
            get(urlEqualTo(documentTypesUrl))
              .willReturn(
                ok(invalidJson.toString)
              )
          )

          whenReady(service.documentTypes()(implicitly, hc)) {
            result =>
              val expectedError = (JsPath, Seq(JsonValidationError("error.expected.jsarray")))

              result.invalidValue.toChain.toList must contain theSameElementsAs Seq(JsonError("documentType", Seq(expectedError)))
          }
      }
    }
  }

  "additionalInformation" - {

    val additionalInformationUrl = "/transit-movements-trader-reference-data/additional-information"

    "must return a sequence of kinds of package" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[AdditionalInformation]]) {
        (hc, additionalInfo) =>
          server.stubFor(
            get(urlEqualTo(additionalInformationUrl))
              .willReturn(
                ok(Json.toJson(additionalInfo).toString)
              )
          )

          whenReady(service.additionalInformation()(implicitly, hc)) {
            result =>
              result.valid.value mustEqual additionalInfo
          }
      }
    }

    "must return a Reference Data Retrieval Error" - {

      "when the server returns a 4xx or 5xx status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo(additionalInformationUrl))
                .willReturn(
                  status(returnStatus).withBody("body")
                )
            )

            whenReady(service.additionalInformation()(implicitly, hc)) {
              result =>
                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(
                  ReferenceDataRetrievalError("additionalInformation", returnStatus, "body"))
            }
        }
      }
    }

    "when the server returns data that cannot be read as a sequence of kinds of package" in {

      forAll(arbitrary[HeaderCarrier]) {
        hc =>
          val invalidJson = Json.obj("foo" -> "bar")

          server.stubFor(
            get(urlEqualTo(additionalInformationUrl))
              .willReturn(
                ok(invalidJson.toString)
              )
          )

          whenReady(service.additionalInformation()(implicitly, hc)) {
            result =>
              val expectedError = (JsPath, Seq(JsonValidationError("error.expected.jsarray")))

              result.invalidValue.toChain.toList must contain theSameElementsAs Seq(JsonError("additionalInformation", Seq(expectedError)))
          }
      }
    }
  }

  "transportMode" - {

    val endpoint = "/transit-movements-trader-reference-data/transport-mode"

    "must return a sequence" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[TransportMode]]) {
        (hc, data) =>
          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(Json.toJson(data).toString)
              )
          )

          whenReady(service.transportMode()(implicitly, hc)) {
            result =>
              result.valid.value mustEqual data
          }
      }
    }

    "must return a Reference Data Retrieval Error" - {

      "when the server returns a 4xx or 5xx status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo(endpoint))
                .willReturn(
                  status(returnStatus).withBody("body")
                )
            )

            whenReady(service.transportMode()(implicitly, hc)) {
              result =>
                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataRetrievalError("transportMode", returnStatus, "body"))
            }
        }
      }
    }

    "when the server returns data that cannot be read as a sequence" in {

      forAll(arbitrary[HeaderCarrier]) {
        hc =>
          val invalidJson = Json.obj("foo" -> "bar")

          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(invalidJson.toString)
              )
          )

          whenReady(service.transportMode()(implicitly, hc)) {
            result =>
              val expectedError = (JsPath, Seq(JsonValidationError("error.expected.jsarray")))

              result.invalidValue.toChain.toList must contain theSameElementsAs Seq(JsonError("transportMode", Seq(expectedError)))
          }
      }
    }
  }

  "controlResultCode" - {

    val endpoint = "/transit-movements-trader-reference-data/control-result"

    "must return a sequence" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[ControlResultData]]) {
        (hc, data) =>
          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(Json.toJson(data).toString)
              )
          )

          whenReady(service.controlResult()(implicitly, hc)) {
            result =>
              result.valid.value mustEqual data
          }
      }
    }

    "must return a Reference Data Retrieval Error" - {

      "when the server returns a 4xx or 5xx status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo(endpoint))
                .willReturn(
                  status(returnStatus).withBody("body")
                )
            )

            whenReady(service.controlResult()(implicitly, hc)) {
              result =>
                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataRetrievalError("controlResult", returnStatus, "body"))
            }
        }
      }
    }

    "when the server returns data that cannot be read as a sequence" in {

      forAll(arbitrary[HeaderCarrier]) {
        hc =>
          val invalidJson = Json.obj("foo" -> "bar")

          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(invalidJson.toString)
              )
          )

          whenReady(service.controlResult()(implicitly, hc)) {
            result =>
              val expectedError = (JsPath, Seq(JsonValidationError("error.expected.jsarray")))

              result.invalidValue.toChain.toList must contain theSameElementsAs Seq(JsonError("controlResult", Seq(expectedError)))
          }
      }
    }
  }

  "previousDocumentTypes" - {

    val endpoint = "/transit-movements-trader-reference-data/previous-document-types"

    "must return a sequence" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[PreviousDocumentTypes]]) {
        (hc, data) =>
          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(Json.toJson(data).toString)
              )
          )

          whenReady(service.previousDocumentTypes()(implicitly, hc)) {
            result =>
              result.valid.value mustEqual data
          }
      }
    }

    "must return a Reference Data Retrieval Error" - {

      "when the server returns a 4xx or 5xx status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo(endpoint))
                .willReturn(
                  status(returnStatus).withBody("body")
                )
            )

            whenReady(service.previousDocumentTypes()(implicitly, hc)) {
              result =>
                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(
                  ReferenceDataRetrievalError("previousDocumentTypes", returnStatus, "body"))
            }
        }
      }
    }

    "when the server returns data that cannot be read as a sequence" in {

      forAll(arbitrary[HeaderCarrier]) {
        hc =>
          val invalidJson = Json.obj("foo" -> "bar")

          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(invalidJson.toString)
              )
          )

          whenReady(service.previousDocumentTypes()(implicitly, hc)) {
            result =>
              val expectedError = (JsPath, Seq(JsonValidationError("error.expected.jsarray")))

              result.invalidValue.toChain.toList must contain theSameElementsAs Seq(JsonError("previousDocumentTypes", Seq(expectedError)))
          }
      }
    }
  }

  "sensitiveGoodsCode" - {

    val endpoint = "/transit-movements-trader-reference-data/sensitive-goods-code"

    "must return a sequence" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[SensitiveGoodsCode]]) {
        (hc, data) =>
          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(Json.toJson(data).toString)
              )
          )

          whenReady(service.sensitiveGoodsCode()(implicitly, hc)) {
            result =>
              result.valid.value mustEqual data
          }
      }
    }

    "must return a Reference Data Retrieval Error" - {

      "when the server returns a 4xx or 5xx status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo(endpoint))
                .willReturn(
                  status(returnStatus).withBody("body")
                )
            )

            whenReady(service.sensitiveGoodsCode()(implicitly, hc)) {
              result =>
                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataRetrievalError("sensitiveGoodsCode", returnStatus, "body"))
            }
        }
      }
    }

    "when the server returns data that cannot be read as a sequence" in {

      forAll(arbitrary[HeaderCarrier]) {
        hc =>
          val invalidJson = Json.obj("foo" -> "bar")

          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(invalidJson.toString)
              )
          )

          whenReady(service.sensitiveGoodsCode()(implicitly, hc)) {
            result =>
              val expectedError = (JsPath, Seq(JsonValidationError("error.expected.jsarray")))

              result.invalidValue.toChain.toList must contain theSameElementsAs Seq(JsonError("sensitiveGoodsCode", Seq(expectedError)))
          }
      }
    }
  }

  "specialMentions" - {

    val endpoint = "/transit-movements-trader-reference-data/special-mentions"

    "must return a sequence" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[SpecialMentions]]) {
        (hc, data) =>
          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(Json.toJson(data).toString)
              )
          )

          whenReady(service.specialMentions()(implicitly, hc)) {
            result =>
              result.valid.value mustEqual data
          }
      }
    }

    "must return a Reference Data Retrieval Error" - {

      "when the server returns a 4xx or 5xx status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses) {
          (hc, returnStatus) =>
            server.stubFor(
              get(urlEqualTo(endpoint))
                .willReturn(
                  status(returnStatus).withBody("body")
                )
            )

            whenReady(service.specialMentions()(implicitly, hc)) {
              result =>
                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataRetrievalError("specialMentions", returnStatus, "body"))
            }
        }
      }
    }

    "when the server returns data that cannot be read as a sequence" in {

      forAll(arbitrary[HeaderCarrier]) {
        hc =>
          val invalidJson = Json.obj("foo" -> "bar")

          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(invalidJson.toString)
              )
          )

          whenReady(service.specialMentions()(implicitly, hc)) {
            result =>
              val expectedError = (JsPath, Seq(JsonValidationError("error.expected.jsarray")))

              result.invalidValue.toChain.toList must contain theSameElementsAs Seq(JsonError("specialMentions", Seq(expectedError)))
          }
      }
    }
  }
}
