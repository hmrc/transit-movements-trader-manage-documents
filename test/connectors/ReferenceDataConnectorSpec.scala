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

package connectors

import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import com.github.tomakehurst.wiremock.client.WireMock._
import generators.ReferenceModelGenerators
import models.reference._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.JsPath
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.JsonValidationError
import play.api.test.DefaultAwaitTimeout
import play.api.test.FutureAwaits
import services.JsonError
import services.ReferenceDataRetrievalError
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.RequestId
import utils.WireMockHelper
import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ReferenceDataConnectorSpec
    extends AnyFreeSpec
    with Matchers
    with GuiceOneAppPerSuite
    with WireMockHelper
    with ScalaFutures
    with IntegrationPatience
    with ScalaCheckPropertyChecks
    with ReferenceModelGenerators
    with ValidatedMatchers
    with ValidatedValues
    with FutureAwaits
    with DefaultAwaitTimeout {

  implicit lazy val arbitraryHC: Arbitrary[HeaderCarrier] =
    Arbitrary(Gen.const(HeaderCarrier()))

  implicit override lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(
        "microservice.services.transit-movements-trader-reference-data.port" -> server.port
      )
      .build()

  private lazy val service: ReferenceDataConnector = app.injector.instanceOf[ReferenceDataConnector]

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
                  ReferenceDataRetrievalError("additionalInformation", returnStatus, "body")
                )
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

    val endpoint = "/transit-movements-trader-reference-data/control-results"

    "must return a sequence" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[ControlResultData]) {
        (hc, data) =>
          server.stubFor(
            get(urlEqualTo(endpoint + s"/${data.code}"))
              .willReturn(
                ok(Json.toJson(data).toString)
              )
          )

          whenReady(service.controlResultByCode(data.code)(implicitly, hc)) {
            result =>
              result mustEqual data
          }
      }
    }

    "return a malformed exception if and invalid json is received" in {

      implicit val hc: HeaderCarrier = HeaderCarrier()
      val validJson: JsValue         = Json.obj("code" -> "SomeCode")

      server.stubFor(
        get(urlEqualTo(endpoint + "/2345"))
          .willReturn(
            ok(validJson.toString)
          )
      )

      intercept[MalformedReferenceDataException](await(service.controlResultByCode("2345")))
    }

    "must return a InvalidReferenceDataStatusException" - {

      "when the server returns a 4xx or 5xx status" in {

        forAll(arbitrary[HeaderCarrier], errorStatuses, arbitrary[ControlResultData]) {
          (hc, returnStatus, controlResult) =>
            server.stubFor(
              get(urlEqualTo(endpoint + s"/${controlResult.code}"))
                .willReturn(
                  status(returnStatus).withBody("body")
                )
            )

            intercept[InvalidReferenceDataStatusException](await(service.controlResultByCode(controlResult.code)(implicitly, hc)))

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
                  ReferenceDataRetrievalError("previousDocumentTypes", returnStatus, "body")
                )
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

  "circumstanceIndicator" - {

    val endpoint = "/transit-movements-trader-reference-data/circumstance-indicators"

    "must return a sequence" in {

      forAll(arbitrary[HeaderCarrier], arbitrary[Seq[CircumstanceIndicator]]) {
        (hc, data) =>
          server.stubFor(
            get(urlEqualTo(endpoint))
              .willReturn(
                ok(Json.toJson(data).toString)
              )
          )

          whenReady(service.circumstanceIndicators()(implicitly, hc)) {
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

            whenReady(service.circumstanceIndicators()(implicitly, hc)) {
              result =>
                result.invalidValue.toChain.toList must contain theSameElementsAs Seq(
                  ReferenceDataRetrievalError("circumstanceIndicators", returnStatus, "body")
                )
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

          whenReady(service.circumstanceIndicators()(implicitly, hc)) {
            result =>
              val expectedError = (JsPath, Seq(JsonValidationError("error.expected.jsarray")))

              result.invalidValue.toChain.toList must contain theSameElementsAs Seq(JsonError("circumstanceIndicators", Seq(expectedError)))
          }
      }
    }
  }

  "Customs office" - {
    def newRequestId: RequestId = RequestId(UUID.randomUUID().toString)
    val endpoint                = "/transit-movements-trader-reference-data/customs-office/2345"

    "return a Customs office if found and name is present" in {
      val requestId = newRequestId

      implicit val hc: HeaderCarrier = HeaderCarrier(requestId = Some(requestId))
      val validJson: JsValue         = Json.obj("id" -> "SomeCode", "name" -> "Has A Name", "countryId" -> "HA")

      server.stubFor(
        get(urlEqualTo(endpoint))
          .withHeader("X-Request-Id", equalTo(requestId.value))
          .willReturn(
            ok(validJson.toString)
          )
      )

      service.customsOfficeSearch("2345").futureValue mustBe CustomsOffice("SomeCode", Some("Has A Name"), "HA")
    }
    "return a Customs office if found and name is not present" in {
      val requestId = newRequestId

      implicit val hc: HeaderCarrier = HeaderCarrier(requestId = Some(requestId))
      val validJson: JsValue         = Json.obj("id" -> "SomeCode", "countryId" -> "SC")

      server.stubFor(
        get(urlEqualTo(endpoint))
          .withHeader("X-Request-Id", equalTo(requestId.value))
          .willReturn(
            ok(validJson.toString)
          )
      )

      service.customsOfficeSearch("2345").futureValue mustBe CustomsOffice("SomeCode", None, "SC")
    }
    "return a malformed exception if and invalid json is received" in {
      val requestId = newRequestId

      implicit val hc: HeaderCarrier = HeaderCarrier(requestId = Some(requestId))
      val validJson: JsValue         = Json.obj("code" -> "SomeCode")

      server.stubFor(
        get(urlEqualTo(endpoint))
          .withHeader("X-Request-Id", equalTo(requestId.value))
          .willReturn(
            ok(validJson.toString)
          )
      )

      intercept[MalformedReferenceDataException](await(service.customsOfficeSearch("2345")))
    }
    "return an invalid status exception if and invalid json is received" in {
      forAll(errorStatuses) {
        returnStatus =>
          val requestId = newRequestId

          implicit val hc: HeaderCarrier = HeaderCarrier(requestId = Some(requestId))

          server.stubFor(
            get(urlEqualTo(endpoint))
              .withHeader("X-Request-Id", equalTo(requestId.value))
              .willReturn(
                aResponse().withStatus(returnStatus)
              )
          )

          intercept[InvalidReferenceDataStatusException](await(service.customsOfficeSearch("2345")))
      }
    }
  }
}
