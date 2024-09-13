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

package services.messages

import base.SpecBase
import connectors.DepartureMovementP5Connector
import generated.p5.CC029CType
import generators.ModelGenerators
import models.DepartureMessageType.DepartureNotification
import models.DepartureMessageMetaData
import models.DepartureMessages
import models.Phase
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.xml.Node

class DepartureMessageP5ServiceSpec extends SpecBase with ScalaFutures with ScalaCheckPropertyChecks with ModelGenerators {

  private val mockConnector = mock[DepartureMovementP5Connector]

  private val service = new DepartureMessageP5Service(mockConnector)

  private val departureId = "departureId"
  private val messageId   = "messageId"

  implicit private val hc: HeaderCarrier = new HeaderCarrier()

  "getIE015MessageId" - {
    "must return None" - {
      "when IE015 message not found" in {
        forAll(arbitrary[Phase]) {
          phase =>
            val messages = DepartureMessages(List.empty)

            when(mockConnector.getMessages(any(), any())(any(), any()))
              .thenReturn(Future.successful(messages))

            val result = service.getIE015MessageId(departureId, phase).futureValue

            result mustBe None
        }
      }
    }

    "must return IE015 message ID" - {
      "when IE015 message found" in {
        forAll(arbitrary[Phase]) {
          phase =>
            val messageId = "ie015 id"
            val ie015     = DepartureMessageMetaData(messageId, LocalDateTime.now(), DepartureNotification, "")
            val messages  = DepartureMessages(List(ie015))

            when(mockConnector.getMessages(any(), any())(any(), any()))
              .thenReturn(Future.successful(messages))

            val result = service.getIE015MessageId(departureId, phase).futureValue

            result mustBe Some(messageId)
        }
      }
    }
  }

  "getReleaseForTransitNotification" - {
    "must parse CC029CType" in {
      val message: Node =
        <ncts:CC029C PhaseID="NCTS5.0" xmlns:ncts="http://ncts.dgtaxud.ec">
          <messageSender>token</messageSender>
          <messageRecipient>token</messageRecipient>
          <preparationDateAndTime>2007-10-26T07:36:28</preparationDateAndTime>
          <messageIdentification>token</messageIdentification>
          <messageType>CC029C</messageType>
          <!--Optional:-->
          <correlationIdentifier>token</correlationIdentifier>
          <TransitOperation>
            <LRN>string</LRN>
            <MRN>string</MRN>
            <declarationType>token</declarationType>
            <additionalDeclarationType>token</additionalDeclarationType>
            <!--Optional:-->
            <TIRCarnetNumber>string</TIRCarnetNumber>
            <declarationAcceptanceDate>2014-06-09+01:00</declarationAcceptanceDate>
            <releaseDate>2008-11-15</releaseDate>
            <security>token</security>
            <reducedDatasetIndicator>0</reducedDatasetIndicator>
            <!--Optional:-->
            <specificCircumstanceIndicator>token</specificCircumstanceIndicator>
            <!--Optional:-->
            <communicationLanguageAtDeparture>st</communicationLanguageAtDeparture>
            <bindingItinerary>1</bindingItinerary>
          </TransitOperation>
          <!--0 to 9 repetitions:-->
          <Authorisation>
            <sequenceNumber>token</sequenceNumber>
            <type>token</type>
            <referenceNumber>string</referenceNumber>
          </Authorisation>
          <CustomsOfficeOfDeparture>
            <referenceNumber>stringst</referenceNumber>
          </CustomsOfficeOfDeparture>
          <CustomsOfficeOfDestinationDeclared>
            <referenceNumber>stringst</referenceNumber>
          </CustomsOfficeOfDestinationDeclared>
          <!--0 to 9 repetitions:-->
          <CustomsOfficeOfTransitDeclared>
            <sequenceNumber>token</sequenceNumber>
            <referenceNumber>stringst</referenceNumber>
            <!--Optional:-->
            <arrivalDateAndTimeEstimated>2002-11-05T08:01:03+00:00</arrivalDateAndTimeEstimated>
          </CustomsOfficeOfTransitDeclared>
          <!--0 to 9 repetitions:-->
          <CustomsOfficeOfExitForTransitDeclared>
            <sequenceNumber>token</sequenceNumber>
            <referenceNumber>stringst</referenceNumber>
          </CustomsOfficeOfExitForTransitDeclared>
          <HolderOfTheTransitProcedure>
            <!--Optional:-->
            <identificationNumber>string</identificationNumber>
            <!--Optional:-->
            <TIRHolderIdentificationNumber>string</TIRHolderIdentificationNumber>
            <!--Optional:-->
            <name>string</name>
            <!--Optional:-->
            <Address>
              <streetAndNumber>string</streetAndNumber>
              <!--Optional:-->
              <postcode>string</postcode>
              <city>string</city>
              <country>st</country>
            </Address>
            <!--Optional:-->
            <ContactPerson>
              <name>string</name>
              <phoneNumber>string</phoneNumber>
              <!--Optional:-->
              <eMailAddress>string</eMailAddress>
            </ContactPerson>
          </HolderOfTheTransitProcedure>
          <!--Optional:-->
          <Representative>
            <identificationNumber>string</identificationNumber>
            <status>token</status>
            <!--Optional:-->
            <ContactPerson>
              <name>string</name>
              <phoneNumber>string</phoneNumber>
              <!--Optional:-->
              <eMailAddress>string</eMailAddress>
            </ContactPerson>
          </Representative>
          <!--Optional:-->
          <ControlResult>
            <code>token</code>
            <date>2002-06-24+01:00</date>
            <controlledBy>string</controlledBy>
            <!--Optional:-->
            <text>string</text>
          </ControlResult>
          <!--1 to 9 repetitions:-->
          <Guarantee>
            <sequenceNumber>token</sequenceNumber>
            <guaranteeType>s</guaranteeType>
            <!--Optional:-->
            <otherGuaranteeReference>string</otherGuaranteeReference>
            <!--0 to 99 repetitions:-->
            <GuaranteeReference>
              <sequenceNumber>token</sequenceNumber>
              <!--Optional:-->
              <GRN>string</GRN>
              <!--Optional:-->
              <accessCode>stri</accessCode>
              <amountToBeCovered>1000.000000000000</amountToBeCovered>
              <currency>token</currency>
            </GuaranteeReference>
          </Guarantee>
          <Consignment>
            <!--Optional:-->
            <countryOfDispatch>st</countryOfDispatch>
            <!--Optional:-->
            <countryOfDestination>token</countryOfDestination>
            <containerIndicator>0</containerIndicator>
            <!--Optional:-->
            <inlandModeOfTransport>token</inlandModeOfTransport>
            <!--Optional:-->
            <modeOfTransportAtTheBorder>token</modeOfTransportAtTheBorder>
            <grossMass>1000.000000000000</grossMass>
            <!--Optional:-->
            <referenceNumberUCR>string</referenceNumberUCR>
            <!--Optional:-->
            <Carrier>
              <identificationNumber>string</identificationNumber>
              <!--Optional:-->
              <ContactPerson>
                <name>string</name>
                <phoneNumber>string</phoneNumber>
                <!--Optional:-->
                <eMailAddress>string</eMailAddress>
              </ContactPerson>
            </Carrier>
            <!--Optional:-->
            <Consignor>
              <!--Optional:-->
              <identificationNumber>string</identificationNumber>
              <!--Optional:-->
              <name>string</name>
              <!--Optional:-->
              <Address>
                <streetAndNumber>string</streetAndNumber>
                <!--Optional:-->
                <postcode>string</postcode>
                <city>string</city>
                <country>st</country>
              </Address>
              <!--Optional:-->
              <ContactPerson>
                <name>string</name>
                <phoneNumber>string</phoneNumber>
                <!--Optional:-->
                <eMailAddress>string</eMailAddress>
              </ContactPerson>
            </Consignor>
            <!--Optional:-->
            <Consignee>
              <!--Optional:-->
              <identificationNumber>string</identificationNumber>
              <!--Optional:-->
              <name>string</name>
              <!--Optional:-->
              <Address>
                <streetAndNumber>string</streetAndNumber>
                <!--Optional:-->
                <postcode>string</postcode>
                <city>string</city>
                <country>st</country>
              </Address>
            </Consignee>
            <!--0 to 99 repetitions:-->
            <AdditionalSupplyChainActor>
              <sequenceNumber>token</sequenceNumber>
              <role>token</role>
              <identificationNumber>string</identificationNumber>
            </AdditionalSupplyChainActor>
            <!--0 to 9999 repetitions:-->
            <TransportEquipment>
              <sequenceNumber>token</sequenceNumber>
              <!--Optional:-->
              <containerIdentificationNumber>string</containerIdentificationNumber>
              <numberOfSeals>100</numberOfSeals>
              <!--0 to 99 repetitions:-->
              <Seal>
                <sequenceNumber>token</sequenceNumber>
                <identifier>string</identifier>
              </Seal>
              <!--0 to 9999 repetitions:-->
              <GoodsReference>
                <sequenceNumber>token</sequenceNumber>
                <declarationGoodsItemNumber>100</declarationGoodsItemNumber>
              </GoodsReference>
            </TransportEquipment>
            <!--Optional:-->
            <LocationOfGoods>
              <typeOfLocation>token</typeOfLocation>
              <qualifierOfIdentification>token</qualifierOfIdentification>
              <!--Optional:-->
              <authorisationNumber>string</authorisationNumber>
              <!--Optional:-->
              <additionalIdentifier>stri</additionalIdentifier>
              <!--Optional:-->
              <UNLocode>token</UNLocode>
              <!--Optional:-->
              <CustomsOffice>
                <referenceNumber>stringst</referenceNumber>
              </CustomsOffice>
              <!--Optional:-->
              <GNSS>
                <latitude>string</latitude>
                <longitude>string</longitude>
              </GNSS>
              <!--Optional:-->
              <EconomicOperator>
                <identificationNumber>string</identificationNumber>
              </EconomicOperator>
              <!--Optional:-->
              <Address>
                <streetAndNumber>string</streetAndNumber>
                <!--Optional:-->
                <postcode>string</postcode>
                <city>string</city>
                <country>st</country>
              </Address>
              <!--Optional:-->
              <PostcodeAddress>
                <!--Optional:-->
                <houseNumber>string</houseNumber>
                <postcode>string</postcode>
                <country>st</country>
              </PostcodeAddress>
              <!--Optional:-->
              <ContactPerson>
                <name>string</name>
                <phoneNumber>string</phoneNumber>
                <!--Optional:-->
                <eMailAddress>string</eMailAddress>
              </ContactPerson>
            </LocationOfGoods>
            <!--0 to 999 repetitions:-->
            <DepartureTransportMeans>
              <sequenceNumber>token</sequenceNumber>
              <typeOfIdentification>token</typeOfIdentification>
              <identificationNumber>string</identificationNumber>
              <nationality>st</nationality>
            </DepartureTransportMeans>
            <!--0 to 99 repetitions:-->
            <CountryOfRoutingOfConsignment>
              <sequenceNumber>token</sequenceNumber>
              <country>st</country>
            </CountryOfRoutingOfConsignment>
            <!--0 to 9 repetitions:-->
            <ActiveBorderTransportMeans>
              <sequenceNumber>token</sequenceNumber>
              <customsOfficeAtBorderReferenceNumber>token</customsOfficeAtBorderReferenceNumber>
              <typeOfIdentification>token</typeOfIdentification>
              <identificationNumber>string</identificationNumber>
              <nationality>st</nationality>
              <!--Optional:-->
              <conveyanceReferenceNumber>string</conveyanceReferenceNumber>
            </ActiveBorderTransportMeans>
            <PlaceOfLoading>
              <!--Optional:-->
              <UNLocode>token</UNLocode>
              <!--Optional:-->
              <country>st</country>
              <!--Optional:-->
              <location>string</location>
            </PlaceOfLoading>
            <!--Optional:-->
            <PlaceOfUnloading>
              <!--Optional:-->
              <UNLocode>token</UNLocode>
              <!--Optional:-->
              <country>st</country>
              <!--Optional:-->
              <location>string</location>
            </PlaceOfUnloading>
            <!--0 to 9999 repetitions:-->
            <PreviousDocument>
              <sequenceNumber>token</sequenceNumber>
              <type>token</type>
              <referenceNumber>string</referenceNumber>
              <!--Optional:-->
              <complementOfInformation>string</complementOfInformation>
            </PreviousDocument>
            <!--0 to 99 repetitions:-->
            <SupportingDocument>
              <sequenceNumber>token</sequenceNumber>
              <type>token</type>
              <referenceNumber>string</referenceNumber>
              <!--Optional:-->
              <documentLineItemNumber>100</documentLineItemNumber>
              <!--Optional:-->
              <complementOfInformation>string</complementOfInformation>
            </SupportingDocument>
            <!--0 to 99 repetitions:-->
            <TransportDocument>
              <sequenceNumber>token</sequenceNumber>
              <type>token</type>
              <referenceNumber>string</referenceNumber>
            </TransportDocument>
            <!--0 to 99 repetitions:-->
            <AdditionalReference>
              <sequenceNumber>token</sequenceNumber>
              <type>token</type>
              <!--Optional:-->
              <referenceNumber>string</referenceNumber>
            </AdditionalReference>
            <!--0 to 99 repetitions:-->
            <AdditionalInformation>
              <sequenceNumber>token</sequenceNumber>
              <code>token</code>
              <!--Optional:-->
              <text>string</text>
            </AdditionalInformation>
            <!--Optional:-->
            <TransportCharges>
              <methodOfPayment>s</methodOfPayment>
            </TransportCharges>
            <!--1 to 99 repetitions:-->
            <HouseConsignment>
              <sequenceNumber>token</sequenceNumber>
              <!--Optional:-->
              <countryOfDispatch>st</countryOfDispatch>
              <grossMass>1000.000000000000</grossMass>
              <!--Optional:-->
              <referenceNumberUCR>string</referenceNumberUCR>
              <!--Optional:-->
              <securityIndicatorFromExportDeclaration>token</securityIndicatorFromExportDeclaration>
              <!--Optional:-->
              <Consignor>
                <!--Optional:-->
                <identificationNumber>string</identificationNumber>
                <!--Optional:-->
                <name>string</name>
                <!--Optional:-->
                <Address>
                  <streetAndNumber>string</streetAndNumber>
                  <!--Optional:-->
                  <postcode>string</postcode>
                  <city>string</city>
                  <country>st</country>
                </Address>
                <!--Optional:-->
                <ContactPerson>
                  <name>string</name>
                  <phoneNumber>string</phoneNumber>
                  <!--Optional:-->
                  <eMailAddress>string</eMailAddress>
                </ContactPerson>
              </Consignor>
              <!--Optional:-->
              <Consignee>
                <!--Optional:-->
                <identificationNumber>string</identificationNumber>
                <!--Optional:-->
                <name>string</name>
                <!--Optional:-->
                <Address>
                  <streetAndNumber>string</streetAndNumber>
                  <!--Optional:-->
                  <postcode>string</postcode>
                  <city>string</city>
                  <country>st</country>
                </Address>
              </Consignee>
              <!--0 to 99 repetitions:-->
              <AdditionalSupplyChainActor>
                <sequenceNumber>token</sequenceNumber>
                <role>token</role>
                <identificationNumber>string</identificationNumber>
              </AdditionalSupplyChainActor>
              <!--0 to 999 repetitions:-->
              <DepartureTransportMeans>
                <sequenceNumber>token</sequenceNumber>
                <typeOfIdentification>token</typeOfIdentification>
                <identificationNumber>string</identificationNumber>
                <nationality>st</nationality>
              </DepartureTransportMeans>
              <!--0 to 99 repetitions:-->
              <PreviousDocument>
                <sequenceNumber>token</sequenceNumber>
                <type>token</type>
                <referenceNumber>string</referenceNumber>
                <!--Optional:-->
                <complementOfInformation>string</complementOfInformation>
              </PreviousDocument>
              <!--0 to 99 repetitions:-->
              <SupportingDocument>
                <sequenceNumber>token</sequenceNumber>
                <type>token</type>
                <referenceNumber>string</referenceNumber>
                <!--Optional:-->
                <documentLineItemNumber>100</documentLineItemNumber>
                <!--Optional:-->
                <complementOfInformation>string</complementOfInformation>
              </SupportingDocument>
              <!--0 to 99 repetitions:-->
              <TransportDocument>
                <sequenceNumber>token</sequenceNumber>
                <type>token</type>
                <referenceNumber>string</referenceNumber>
              </TransportDocument>
              <!--0 to 99 repetitions:-->
              <AdditionalReference>
                <sequenceNumber>token</sequenceNumber>
                <type>token</type>
                <!--Optional:-->
                <referenceNumber>string</referenceNumber>
              </AdditionalReference>
              <!--0 to 99 repetitions:-->
              <AdditionalInformation>
                <sequenceNumber>token</sequenceNumber>
                <code>token</code>
                <!--Optional:-->
                <text>string</text>
              </AdditionalInformation>
              <!--Optional:-->
              <TransportCharges>
                <methodOfPayment>s</methodOfPayment>
              </TransportCharges>
              <!--1 to 999 repetitions:-->
              <ConsignmentItem>
                <goodsItemNumber>token</goodsItemNumber>
                <declarationGoodsItemNumber>100</declarationGoodsItemNumber>
                <!--Optional:-->
                <declarationType>token</declarationType>
                <!--Optional:-->
                <countryOfDispatch>st</countryOfDispatch>
                <!--Optional:-->
                <countryOfDestination>token</countryOfDestination>
                <!--Optional:-->
                <referenceNumberUCR>string</referenceNumberUCR>
                <!--Optional:-->
                <Consignee>
                  <!--Optional:-->
                  <identificationNumber>string</identificationNumber>
                  <!--Optional:-->
                  <name>string</name>
                  <!--Optional:-->
                  <Address>
                    <streetAndNumber>string</streetAndNumber>
                    <!--Optional:-->
                    <postcode>string</postcode>
                    <city>string</city>
                    <country>st</country>
                  </Address>
                </Consignee>
                <!--0 to 99 repetitions:-->
                <AdditionalSupplyChainActor>
                  <sequenceNumber>token</sequenceNumber>
                  <role>token</role>
                  <identificationNumber>string</identificationNumber>
                </AdditionalSupplyChainActor>
                <Commodity>
                  <descriptionOfGoods>string</descriptionOfGoods>
                  <!--Optional:-->
                  <cusCode>token</cusCode>
                  <!--Optional:-->
                  <CommodityCode>
                    <harmonizedSystemSubHeadingCode>token</harmonizedSystemSubHeadingCode>
                    <!--Optional:-->
                    <combinedNomenclatureCode>st</combinedNomenclatureCode>
                  </CommodityCode>
                  <!--0 to 99 repetitions:-->
                  <DangerousGoods>
                    <sequenceNumber>token</sequenceNumber>
                    <UNNumber>token</UNNumber>
                  </DangerousGoods>
                  <GoodsMeasure>
                    <grossMass>1000.000000000000</grossMass>
                    <!--Optional:-->
                    <netMass>1000.000000000000</netMass>
                  </GoodsMeasure>
                </Commodity>
                <!--1 to 99 repetitions:-->
                <Packaging>
                  <sequenceNumber>token</sequenceNumber>
                  <typeOfPackages>token</typeOfPackages>
                  <!--Optional:-->
                  <numberOfPackages>100</numberOfPackages>
                  <!--Optional:-->
                  <shippingMarks>string</shippingMarks>
                </Packaging>
                <!--0 to 99 repetitions:-->
                <PreviousDocument>
                  <sequenceNumber>token</sequenceNumber>
                  <type>token</type>
                  <referenceNumber>string</referenceNumber>
                  <!--Optional:-->
                  <goodsItemNumber>100</goodsItemNumber>
                  <!--Optional:-->
                  <typeOfPackages>token</typeOfPackages>
                  <!--Optional:-->
                  <numberOfPackages>100</numberOfPackages>
                  <!--Optional:-->
                  <measurementUnitAndQualifier>token</measurementUnitAndQualifier>
                  <!--Optional:-->
                  <quantity>1000.000000000000</quantity>
                  <!--Optional:-->
                  <complementOfInformation>string</complementOfInformation>
                </PreviousDocument>
                <!--0 to 99 repetitions:-->
                <SupportingDocument>
                  <sequenceNumber>token</sequenceNumber>
                  <type>token</type>
                  <referenceNumber>string</referenceNumber>
                  <!--Optional:-->
                  <documentLineItemNumber>100</documentLineItemNumber>
                  <!--Optional:-->
                  <complementOfInformation>string</complementOfInformation>
                </SupportingDocument>
                <!--0 to 99 repetitions:-->
                <TransportDocument>
                  <sequenceNumber>token</sequenceNumber>
                  <type>token</type>
                  <referenceNumber>string</referenceNumber>
                </TransportDocument>
                <!--0 to 99 repetitions:-->
                <AdditionalReference>
                  <sequenceNumber>token</sequenceNumber>
                  <type>token</type>
                  <!--Optional:-->
                  <referenceNumber>string</referenceNumber>
                </AdditionalReference>
                <!--0 to 99 repetitions:-->
                <AdditionalInformation>
                  <sequenceNumber>token</sequenceNumber>
                  <code>token</code>
                  <!--Optional:-->
                  <text>string</text>
                </AdditionalInformation>
                <!--Optional:-->
                <TransportCharges>
                  <methodOfPayment>s</methodOfPayment>
                </TransportCharges>
              </ConsignmentItem>
            </HouseConsignment>
          </Consignment>
        </ncts:CC029C>

      forAll(arbitrary[Phase]) {
        phase =>
          when(mockConnector.getMessage(any(), any(), any())(any(), any()))
            .thenReturn(Future.successful(message))

          val result = Await.result(service.getReleaseForTransitNotification(departureId, messageId, phase), Duration.Inf)

          result mustBe a[CC029CType]
      }
    }
  }
}
