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
import cats.scalatest.{ValidatedMatchers, ValidatedValues}
import com.github.tomakehurst.wiremock.client.WireMock._
import generators.ReferenceModelGenerators
import models.P5.departure.IE029Data
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import uk.gov.hmrc.http.HeaderCarrier
import utils.WireMockHelper

import scala.concurrent.ExecutionContext.Implicits.global

class DepartureMovementP5ConnectorSpec
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

  private lazy val service: DepartureMovementP5Connector = app.injector.instanceOf[DepartureMovementP5Connector]

  "getMRN" - {

    val mrnUrl: String = s"/movements/departures/$departureId"

    "must return an MRN" in {

      server.stubFor(
        get(urlEqualTo(mrnUrl))
          .willReturn(
            ok(Json.toJson(mrn).toString)
          )
      )

      whenReady(service.getMRN(departureId)) {
        result =>
          result mustEqual mrn
      }

    }
  }

  "getDepartureNotificationMessage" - {

    val depNotificationUrl: String = s"/movements/departures/$departureId/messages/$messageId"

    "must return ie029 data" in {

      val ieo29Data = IE029Data(departureMessageData)

      val json1 =
        s"""{
            "n1:CC029C": {
    
          "TransitOperation": {
            "MRN": "MRN",
            "LRN": "LRN",
            "declarationType": "T1",
            "additionalDeclarationType": "T2F",
            "security": "sec",
            "TIRCarnetNumber": "TIR",
            "specificCircumstanceIndicator": "SCI"
          },
          "HolderOfTheTransitProcedure": {
            "identificationNumber": "id1",
            "TIRHolderIdentificationNumber": "TIRID1",
            "name": "Bob",
            "Address": {
              "streetAndNumber": "Address Line 1",
              "postcode": "Address Line 2",
              "city": "Address Line 3",
              "country": "Address Line 4"
            },
            "ContactPerson": {
              "name": "Contact Person Name",
              "phoneNumber": "123456",
              "eMailAddress": "a@a.com"
            }
          },
          "Representative": {
            "identificationNumber": "ID1",
            "status": "Status-1",
            "contactPerson": {
              "name": "Contact Person Name",
              "phoneNumber": "123456",
              "eMailAddress": "a@a.com"
            }
          },
          "Consignment": {
            "grossMass": 1,
            "inlandModeOfTransport": "T1",
            "countryOfDispatch": "GER",
            "countryOfDestination": "GB",
            "modeOfTransportAtTheBorder": "Road",
            "referenceNumberUCR": "UCR001",
            "Consignor": {
              "identificationNumber": "idnum1",
              "name": "Consignor Name",
              "Address": {
                "streetAndNumber": "Address Line 1",
                "postcode": "Address Line 2",
                "city": "Address Line 3",
                "country": "Address Line 4"
              },
              "ContactPerson": {
                "name": "Contact Person Name",
                "phoneNumber": "123456",
                "eMailAddress": "a@a.com"
              }
            },
            "Consignee": {
              "identificationNumber": "idnum1",
              "name": "Consignee Name",
              "Address": {
                "streetAndNumber": "Address Line 1",
                "postcode": "Address Line 2",
                "city": "Address Line 3",
                "country": "Address Line 4"
              },
              "ContactPerson": {
                "name": "Contact Person Name",
                "phoneNumber": "123456",
                "eMailAddress": "a@a.com"
              }
            },
            "Carrier": {
              "identificationNumber": "idnum1",
              "ContactPerson": {
                "name": "Contact Person Name",
                "phoneNumber": "123456",
                "eMailAddress": "a@a.com"
              }
            },
            "LocationOfGoods": {
              "typeOfLocation": "Warehouse",
              "qualifierOfIdentification": "qualifierIdentifier-num-1",
              "authorisationNumber": "1212",
              "additionalIdentifier": "ID0001",
              "UNLocode": "LNCODE1",
              "CustomsOffice": {
                "referenceNumber": "Reference1"
              },
              "GNSS": {
                "latitude": "1232",
                "longitude": "1234"
              },
              "EconomicOperator": {
                "identificationNumber": "EconomicOperator-1"
              },
              "Address": {
                "streetAndNumber": "Address Line 1",
                "postcode": "Address Line 2",
                "city": "Address Line 3",
                "country": "Address Line 4"
              },
              "PostcodeAddress": {
                "houseNumber": "house1",
                "postcode": "BR",
                "country": "UK"
              },
              "ContactPerson": {
                "name": "Contact Person Name",
                "phoneNumber": "123456",
                "eMailAddress": "a@a.com"
              }
            },
            "AdditionalSupplyChainActor": [
              {
                "role": "Actor-Role",
                "identificationNumber": "ID001"
              }
            ],
            "CountryOfRoutingOfConsignment": [
              {
                "sequenceNumber": "Seqnum12243",
                "country": "GB"
              }
            ],
            "DepartureTransportMeans": [
              {
                "typeOfIdentification": "Actor-Role",
                "identificationNumber": "ID001",
                "nationality": "Nationality"
              }
            ],
            "TransportEquipment": [
              {
                "sequenceNumber": "12123",
                "containerIdentificationNumber": "Container-ID-1",
                "numberOfSeals": 8,
                "Seal": [
                  {
                    "sequenceNumber": "1232",
                    "identifier": "ID10012"
                  }
                ],
                "GoodsReference": [
                  {
                    "sequenceNumber": "1232",
                    "declarationGoodsItemNumber": 1234
                  }
                ]
              }
            ],
            "ActiveBorderTransportMeans": [
              {
                "customsOfficeAtBorderReferenceNumber": "GB0001",
                "typeOfIdentification": "T1",
                "identificationNumber": "ID001",
                "nationality": "GB",
                "conveyanceReferenceNumber": "conveyReferenceNumber-1"
              }
            ],
            "PlaceOfLoading": {
              "UNLocode": "LoCoCode-1",
              "country": "GB",
              "location": "L1"
            },
            "PlaceOfUnloading": {
              "UNLocode": "UnLoCoCode-1",
              "country": "GB",
              "location": "L1"
            },
            "HouseConsignment": [
              {
              "Consignor": {
                  "identificationNumber": "idnum1",
                  "name": "Consignor Name",
                  "Address": {
                    "streetAndNumber": "Address Line 1",
                    "postcode": "Address Line 2",
                    "city": "Address Line 3",
                    "country": "Address Line 4"
                  },
                  "ContactPerson": {
                    "name": "Contact Person Name",
                    "phoneNumber": "123456",
                    "eMailAddress": "a@a.com"
                  }
              },
                "ConsignmentItem": [
                {
                "AdditionalSupplyChainActor": [
                      {
                        "role": "Actor-Role",
                        "identificationNumber": "ID001"
                      }
                    ],
                    "Packaging": [
                      {
                        "numberOfPackages": 5,
                        "typeOfPackages": "Plastic",
                        "shippingMarks": "rubberStamp"
                      }
                    ],
                    "DepartureTransportMeans": [
                      {
                        "typeOfIdentification": "Actor-Role",
                        "identificationNumber": "ID001",
                        "nationality": "Nationality"
                      }
                    ],
                    "referenceNumberUCR": "refucr1",
                    "Consignor": {
                      "identificationNumber": "idnum1",
                      "name": "Consignor Name",
                      "Address": {
                        "streetAndNumber": "Address Line 1",
                        "postcode": "Address Line 2",
                        "city": "Address Line 3",
                        "country": "Address Line 4"
                      },
                      "ContactPerson": {
                        "name": "Contact Person Name",
                        "phoneNumber": "123456",
                        "eMailAddress": "a@a.com"
                      }
                    },
                    "Consignee": {
                        "identificationNumber": "idnum1",
                        "name": "Consignor Name",
                        "Address": {
                          "streetAndNumber": "Address Line 1",
                          "postcode": "Address Line 2",
                          "city": "Address Line 3",
                          "country": "Address Line 4"
                        },
                        "ContactPerson": {
                          "name": "Contact Person Name",
                          "phoneNumber": "123456",
                          "eMailAddress": "a@a.com"
                        }
                    },
                    "Commodity":
                      {
                        "descriptionOfGoods": "Tiles",
                        "GoodsMeasure":
                          {
                            "grossMass": 1.43
                          },
                          "DangerousGoods":[
                          {
                            "sequenceNumber": "SHC1",
                            "UNNumber": "NOMC1"
                          }]
                      },
                    "declarationType": "T1",
                    "countryOfDispatch": "GER",
                    "countryOfDestination": "GB",
                    "goodsItemNumber": "123545",
                    "declarationGoodsItemNumber": 9999
                  }
                ],
                 "grossMass": 1.3434
              }
            ],
            "PreviousDocument": [
              {
                "sequenceNumber": "Document-1",
                "type": "Type-1",
                "referenceNumber": "Reference-1",
                "complementOfInformation": "C1"
              }
            ],
            "TransportDocument": [
              {
                "sequenceNumber": "Document-1",
                "type": "Type-1",
                "referenceNumber": "Reference-1"
              }
            ],
            "SupportingDocument": [
              {
                "sequenceNumber": "Document-1",
                "type": "Type-1",
                "referenceNumber": "Reference-1",
                "documentLineItemNumber": 5,
                "complementOfInformation": "C1"
              }
            ],
            "AdditionalInformation": [
              {
                "sequenceNumber": "Document-1",
                "code": "Type-1",
                "text": "Reference-1"
              }
            ],
            "AdditionalReference": [
              {
                "sequenceNumber": "Document-1",
                "type": "Type-1",
                "referenceNumber": "Reference-1"
              }
            ],
            "TransportCharges": {
              "methodOfPayment": "payPal"
            }
          },
          "Guarantee": [
            {
              "sequenceNumber": "SEQNum-1",
              "guaranteeType": "SomeGuaranteeType",
              "otherGuaranteeReference": "otherGuaranteeReference",
              "GuaranteeReference": [
                {
                  "sequenceNumber": "SEQNum-1",
                  "GRN": "GRN-1",
                  "accessCode": "Access-code-1",
                  "amountToBeCovered": 123456.1212,
                  "currency": "GBP"
                }
              ]
            }
          ],
          "Authorisation": [
            {
              "sequenceNumber": "SEQNum-1",
              "type": "Auth-Type",
              "referenceNumber": "Reference-Numb-1"
            }
          ],
          "CustomsOfficeOfExitForTransitDeclared": [
          {
            "sequenceNumber": "seq001",
            "referenceNumber": "ref001"
          }
        ],
        "CustomsOfficeOfTransitDeclared": [
        {
          "sequenceNumber": "seq001",
          "referenceNumber": "ref001",
          "arrivalDateAndTimeEstimated": "-999999999-01-01T00:00"
        }
      ],
          "CustomsOfficeOfDeparture":
          {
            "referenceNumber": "Ref001"
          },
          "CustomsOfficeOfDestinationDeclared":
          {
            "referenceNumber": "Ref001"
          }
        }
  
    }"""

      val json2 = Json.parse(s"""
                  {
                    "_links": {
                      "self": {
                        "href": "/customs/transits/movements/departures/62f4ebbbf581d4aa/messages/62f4ebbb765ba8c2"
                      },
                      "departure": {
                        "href": "/customs/transits/movements/departures/62f4ebbbf581d4aa"
                      }
                    },
                    "id": "62f4ebbb765ba8c2",
                    "departureId": "62f4ebbbf581d4aa",
                    "received": "2022-08-11T11:44:59.83705",
                    "type": "IE029",
                    "status": "Success",
                    "body": $json1
                  }
                  """)
      server.stubFor(
        get(urlEqualTo(depNotificationUrl))
          .willReturn(
            ok(json2.toString())
          )
      )

      whenReady(service.getDepartureNotificationMessage(departureId, messageId)) {
        result =>
          result.data.Consignment.HouseConsignment.map(_.Consignor) mustEqual ieo29Data.data.Consignment.HouseConsignment.map(_.Consignor)
      }

    }
  }
}
