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

package connectors

import base.SpecBase
import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import com.github.tomakehurst.wiremock.client.WireMock._
import generators.ReferenceModelGenerators
import models.P5.unloading.IE043Data
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.DefaultAwaitTimeout
import play.api.test.FutureAwaits
import uk.gov.hmrc.http.HeaderCarrier
import utils.WireMockHelper

import scala.concurrent.ExecutionContext.Implicits.global

class UnloadingPermissionP5ConnectorSpec
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
    with DefaultAwaitTimeout
    with SpecBase {

  implicit lazy val arbitraryHC: Arbitrary[HeaderCarrier] =
    Arbitrary(Gen.const(HeaderCarrier()))

  implicit override lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(
        "microservice.services.common-transit-convention-traders.port" -> server.port
      )
      .build()

  private lazy val service: UnloadingPermissionP5Connector = app.injector.instanceOf[UnloadingPermissionP5Connector]

  "getUnloadingNotificationMessage" - {

    val unloadingNotificationUrl: String = s"/movements/arrivals/$arrivalId/messages/$messageId"

    "must return ie043 data" in {

      val ie043Data = IE043Data(unloadingPermissionMessageData)

      val jsonUserData =
        """
          |{
          |  "n1:CC043C": {
          |    "PhaseID": "NCTS5.0",
          |    "xmlns:ncts": "http://ncts.dgtaxud.ec",
          |    "messageSender": "token",
          |    "messageRecipient": "token",
          |    "preparationDateAndTime": "2007-10-26T07:36:28",
          |    "messageIdentification": "token",
          |    "messageType": "CC043C",
          |    "correlationIdentifier": "token",
          |    "TransitOperation": {
          |      "MRN": "38VYQTYFU3T0KUTUM3",
          |      "declarationType": "T1",
          |      "declarationAcceptanceDate": "2014-06-09+01:00",
          |      "security": "4",
          |      "reducedDatasetIndicator": "1"
          |    },
          |    "CustomsOfficeOfDestinationActual": {"referenceNumber": "GB000068"},
          |    "HolderOfTheTransitProcedure": {
          |      "identificationNumber": "id",
          |      "TIRHolderIdentificationNumber": "tirId",
          |      "name": "Travis",
          |      "Address": {
          |        "streetAndNumber": "Address Line 1",
          |        "postcode": "Address Line 2",
          |        "city": "Address Line 3",
          |        "country": "Address Line 4"
          |      }
          |    },
          |    "TraderAtDestination": {"identificationNumber": "tad-1"},
          |    "CTLControl": {"continueUnloading": "9"},
          |    "Consignment": {
          |      "countryOfDestination": "FR",
          |      "containerIndicator": "1",
          |      "inlandModeOfTransport": "2",
          |      "grossMass": 1000.99,
          |      "Consignor": {
          |        "identificationNumber": "idnum1",
          |        "name": "Consignor Name",
          |        "Address": {
          |          "streetAndNumber": "Address Line 1",
          |          "postcode": "Address Line 2",
          |          "city": "Address Line 3",
          |          "country": "Address Line 4"
          |        }
          |      },
          |      "Consignee": {
          |        "identificationNumber": "idnum2",
          |        "name": "Consignee Name",
          |        "Address": {
          |          "streetAndNumber": "Address Line 1",
          |          "postcode": "Address Line 2",
          |          "city": "Address Line 3",
          |          "country": "Address Line 4"
          |        }
          |      },
          |      "TransportEquipment": [
          |        {
          |          "sequenceNumber": "te2",
          |          "containerIdentificationNumber": "cin-2",
          |          "numberOfSeals": 35,
          |          "Seal": [
          |            {
          |              "sequenceNumber": "seq1",
          |              "identifier": "sealId"
          |            }
          |          ],
          |          "GoodsReference": [
          |            {
          |              "sequenceNumber": "seq1",
          |              "declarationGoodsItemNumber": 5
          |            }
          |          ]
          |        }
          |      ],
          |      "DepartureTransportMeans": [
          |        {
          |          "sequenceNumber": "seq1",
          |          "typeOfIdentification": "type1",
          |          "identificationNumber": "id1",
          |          "nationality": "NG"
          |        }
          |      ],
          |      "PreviousDocument": [
          |        {
          |          "sequenceNumber": "pr1",
          |          "type": "768",
          |          "referenceNumber": "ref1",
          |          "complementOfInformation": "55"
          |        }
          |      ],
          |      "SupportingDocument": [
          |        {
          |          "sequenceNumber": "sp1",
          |          "type": "764",
          |          "referenceNumber": "ref2",
          |          "complementOfInformation": "45"
          |        }
          |      ],
          |      "TransportDocument": [
          |        {
          |          "sequenceNumber": "tp1",
          |          "type": "767",
          |          "referenceNumber": "ref3"
          |        }
          |      ],
          |      "AdditionalReference": [
          |        {
          |          "sequenceNumber": "adRef1",
          |          "type": "4",
          |          "referenceNumber": "ref4"
          |        }
          |      ],
          |      "AdditionalInformation": [
          |        {
          |          "sequenceNumber": "adInf1",
          |          "code": "32",
          |          "text": "additional ref text"
          |        }
          |      ],
          |      "HouseConsignment": [
          |        {
          |          "ConsignmentItem": [
          |            {
          |              "goodsItemNumber": 1,
          |              "declarationGoodsItemNumber": 1,
          |              "Commodity": {
          |                "descriptionOfGoods": "commodity desc",
          |                "GoodsMeasure": {
          |                  "grossMass": 10.5
          |                }
          |              },
          |              "Packaging": [
          |                {
          |                  "numberOfPackages": 3
          |                }
          |              ]
          |            }
          |          ]
          |        }
          |      ]
          |    }
          |  }
          |}
          |""".stripMargin

      val json = Json.parse(s"""
                        {
                          "body": $jsonUserData
                        }
                        """)

      server.stubFor(
        get(urlEqualTo(unloadingNotificationUrl))
          .willReturn(
            ok(json.toString())
          )
      )

      whenReady(service.getUnloadingNotificationMessage(arrivalId, messageId)) {
        result =>
          result mustEqual ie043Data
      }

    }
  }
}
