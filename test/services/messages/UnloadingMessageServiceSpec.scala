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
import connectors.UnloadingPermissionConnector
import generated.CC043CType
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.xml.Node

class UnloadingMessageServiceSpec extends SpecBase with ScalaFutures with ScalaCheckPropertyChecks {

  private val mockConnector = mock[UnloadingPermissionConnector]

  private val service = new UnloadingMessageService(mockConnector)

  private val arrivalId = "arrivalId"
  private val messageId = "messageId"

  implicit private val hc: HeaderCarrier = new HeaderCarrier()

  "getUnloadingPermissionNotification" - {
    "must parse CC043CType" in {
      val message: Node =
        <ncts:CC043C PhaseID="NCTS5.0" xmlns:ncts="http://ncts.dgtaxud.ec">
          <messageSender>token</messageSender>
          <messageRecipient>token</messageRecipient>
          <preparationDateAndTime>2007-10-26T07:36:28</preparationDateAndTime>
          <messageIdentification>token</messageIdentification>
          <messageType>CC043C</messageType>
          <correlationIdentifier>token</correlationIdentifier>
          <TransitOperation>
            <MRN>38VYQTYFU3T0KUTUM3</MRN>
            <declarationType>T</declarationType>
            <declarationAcceptanceDate>2014-06-09+01:00</declarationAcceptanceDate>
            <security>0</security>
            <reducedDatasetIndicator>1</reducedDatasetIndicator>
          </TransitOperation>
          <CustomsOfficeOfDestinationActual>
            <referenceNumber>XI000142</referenceNumber>
          </CustomsOfficeOfDestinationActual>
          <HolderOfTheTransitProcedure>
            <identificationNumber>Fzsisks</identificationNumber>
            <TIRHolderIdentificationNumber>trp-id-1</TIRHolderIdentificationNumber>
            <name>Jean Doe</name>
            <Address>
              <streetAndNumber>1 avenue marceau</streetAndNumber>
              <postcode>10006</postcode>
              <city>Paris</city>
              <country>FR</country>
            </Address>
          </HolderOfTheTransitProcedure>
          <TraderAtDestination>
            <identificationNumber>tad-1</identificationNumber>
          </TraderAtDestination>
          <CTLControl>
            <continueUnloading>1</continueUnloading>
          </CTLControl>
          <Consignment>
            <countryOfDestination>FR</countryOfDestination>
            <containerIndicator>1</containerIndicator>
            <inlandModeOfTransport>2</inlandModeOfTransport>
            <grossMass>1000.99</grossMass>
            <Consignor>
              <identificationNumber>id2</identificationNumber>
              <name>john doe</name>
              <Address>
                <streetAndNumber>1 high street</streetAndNumber>
                <postcode>N1 99Z</postcode>
                <city>Newcastle</city>
                <country>GB</country>
              </Address>
            </Consignor>
            <Consignee>
              <identificationNumber>csgnee-1</identificationNumber>
              <name>Jane Doe</name>
              <Address>
                <streetAndNumber>1 Champs Elysees</streetAndNumber>
                <postcode>75008</postcode>
                <city>Paris</city>
                <country>FR</country>
              </Address>
            </Consignee>
            <TransportEquipment>
              <sequenceNumber>1</sequenceNumber>
              <containerIdentificationNumber>cin-1</containerIdentificationNumber>
              <numberOfSeals>103</numberOfSeals>
              <Seal>
                <sequenceNumber>1</sequenceNumber>
                <identifier>1002</identifier>
              </Seal>
              <GoodsReference>
                <sequenceNumber>1</sequenceNumber>
                <declarationGoodsItemNumber>108</declarationGoodsItemNumber>
              </GoodsReference>
            </TransportEquipment>
            <DepartureTransportMeans>
              <sequenceNumber>1</sequenceNumber>
              <typeOfIdentification>10</typeOfIdentification>
              <identificationNumber>28</identificationNumber>
              <nationality>GB</nationality>
            </DepartureTransportMeans>
            <PreviousDocument>
              <sequenceNumber>1</sequenceNumber>
              <type>C512</type>
              <referenceNumber>info1</referenceNumber>
              <complementOfInformation>8</complementOfInformation>
            </PreviousDocument>
            <SupportingDocument>
              <sequenceNumber>1</sequenceNumber>
              <type>C651</type>
              <referenceNumber>1234</referenceNumber>
              <complementOfInformation>2</complementOfInformation>
            </SupportingDocument>
            <TransportDocument>
              <sequenceNumber>1</sequenceNumber>
              <type>N235</type>
              <referenceNumber>refn-1</referenceNumber>
            </TransportDocument>
            <AdditionalReference>
              <sequenceNumber>1</sequenceNumber>
              <type>Y015</type>
              <referenceNumber>addref-1</referenceNumber>
            </AdditionalReference>
            <AdditionalInformation>
              <sequenceNumber>1</sequenceNumber>
              <code>20100</code>
              <text>additional ref text</text>
            </AdditionalInformation>
            <Incident>
              <sequenceNumber>1</sequenceNumber>
              <code>5</code>
              <text>some text 1</text>
              <Endorsement>
                <date>2013-05-22+01:00</date>
                <authority>de</authority>
                <place>Cologne</place>
                <country>DE</country>
              </Endorsement>
              <Location>
                <qualifierOfIdentification>U</qualifierOfIdentification>
                <UNLocode>34</UNLocode>
                <country>DE</country>
                <GNSS>
                  <latitude>91.0</latitude>
                  <longitude>92.0</longitude>
                </GNSS>
                <Address>
                  <streetAndNumber>2 high street</streetAndNumber>
                  <postcode>ab12 34c</postcode>
                  <city>city2</city>
                </Address>
              </Location>
              <TransportEquipment>
                <sequenceNumber>1</sequenceNumber>
                <containerIdentificationNumber>tn1</containerIdentificationNumber>
                <numberOfSeals>34</numberOfSeals>
                <Seal>
                  <sequenceNumber>1</sequenceNumber>
                  <identifier>sl7</identifier>
                </Seal>
                <GoodsReference>
                  <sequenceNumber>1</sequenceNumber>
                  <declarationGoodsItemNumber>78</declarationGoodsItemNumber>
                </GoodsReference>
              </TransportEquipment>
              <Transhipment>
                <containerIndicator>1</containerIndicator>
                <TransportMeans>
                  <typeOfIdentification>21</typeOfIdentification>
                  <identificationNumber>44</identificationNumber>
                  <nationality>FR</nationality>
                </TransportMeans>
              </Transhipment>
            </Incident>
            <HouseConsignment>
              <sequenceNumber>1</sequenceNumber>
              <grossMass>1234.567</grossMass>
              <securityIndicatorFromExportDeclaration>0</securityIndicatorFromExportDeclaration>
              <Consignor>
                <identificationNumber>csgr1</identificationNumber>
                <name>michael doe</name>
                <Address>
                  <streetAndNumber>3 main street</streetAndNumber>
                  <postcode>bc2 45d</postcode>
                  <city>city4</city>
                  <country>FR</country>
                </Address>
              </Consignor>
              <Consignee>
                <identificationNumber>csgee1</identificationNumber>
                <name>John Smith</name>
                <Address>
                  <streetAndNumber>5 main street</streetAndNumber>
                  <postcode>cd4 56e</postcode>
                  <city>city5</city>
                  <country>DE</country>
                </Address>
              </Consignee>
              <DepartureTransportMeans>
                <sequenceNumber>1</sequenceNumber>
                <typeOfIdentification>20</typeOfIdentification>
                <identificationNumber>23</identificationNumber>
                <nationality>IT</nationality>
              </DepartureTransportMeans>
              <PreviousDocument>
                <sequenceNumber>1</sequenceNumber>
                <type>C605</type>
                <referenceNumber>4</referenceNumber>
                <complementOfInformation>1</complementOfInformation>
              </PreviousDocument>
              <SupportingDocument>
                <sequenceNumber>1</sequenceNumber>
                <type>N002</type>
                <referenceNumber>ref4</referenceNumber>
                <complementOfInformation>6</complementOfInformation>
              </SupportingDocument>
              <TransportDocument>
                <sequenceNumber>1</sequenceNumber>
                <type>N271</type>
                <referenceNumber>9</referenceNumber>
              </TransportDocument>
              <AdditionalReference>
                <sequenceNumber>1</sequenceNumber>
                <type>Y015</type>
                <referenceNumber>4</referenceNumber>
              </AdditionalReference>
              <AdditionalInformation>
                <sequenceNumber>1</sequenceNumber>
                <code>20200</code>
                <text>8</text>
              </AdditionalInformation>
              <ConsignmentItem>
                <goodsItemNumber>6</goodsItemNumber>
                <declarationGoodsItemNumber>100</declarationGoodsItemNumber>
                <declarationType>T1</declarationType>
                <countryOfDestination>DE</countryOfDestination>
                <Consignee>
                  <identificationNumber>5</identificationNumber>
                  <name>Smith</name>
                  <Address>
                    <streetAndNumber>5 main street</streetAndNumber>
                    <postcode>ab12 3cd</postcode>
                    <city>Newcastle</city>
                    <country>GB</country>
                  </Address>
                </Consignee>
                <Commodity>
                  <descriptionOfGoods>shirts</descriptionOfGoods>
                  <cusCode>0010001-6</cusCode>
                  <CommodityCode>
                    <harmonizedSystemSubHeadingCode>010121</harmonizedSystemSubHeadingCode>
                    <combinedNomenclatureCode>45</combinedNomenclatureCode>
                  </CommodityCode>
                  <DangerousGoods>
                    <sequenceNumber>1</sequenceNumber>
                    <UNNumber>0004</UNNumber>
                  </DangerousGoods>
                  <GoodsMeasure>
                    <grossMass>123.45</grossMass>
                    <netMass>123.45</netMass>
                  </GoodsMeasure>
                </Commodity>
                <Packaging>
                  <sequenceNumber>1</sequenceNumber>
                  <typeOfPackages>1A</typeOfPackages>
                  <numberOfPackages>99</numberOfPackages>
                  <shippingMarks>xyz</shippingMarks>
                </Packaging>
                <PreviousDocument>
                  <sequenceNumber>1</sequenceNumber>
                  <type>C612</type>
                  <referenceNumber>4</referenceNumber>
                  <goodsItemNumber>5</goodsItemNumber>
                  <complementOfInformation>1</complementOfInformation>
                </PreviousDocument>
                <SupportingDocument>
                  <sequenceNumber>1</sequenceNumber>
                  <type>L100</type>
                  <referenceNumber>8</referenceNumber>
                  <complementOfInformation>0</complementOfInformation>
                </SupportingDocument>
                <TransportDocument>
                  <sequenceNumber>1</sequenceNumber>
                  <type>N703</type>
                  <referenceNumber>4</referenceNumber>
                </TransportDocument>
                <AdditionalReference>
                  <sequenceNumber>1</sequenceNumber>
                  <type>Y015</type>
                  <referenceNumber>1</referenceNumber>
                </AdditionalReference>
                <AdditionalInformation>
                  <sequenceNumber>1</sequenceNumber>
                  <code>20300</code>
                  <text>additional info text</text>
                </AdditionalInformation>
              </ConsignmentItem>
              <ConsignmentItem>
                <goodsItemNumber>6</goodsItemNumber>
                <declarationGoodsItemNumber>101</declarationGoodsItemNumber>
                <declarationType>T1</declarationType>
                <countryOfDestination>DE</countryOfDestination>
                <Consignee>
                  <identificationNumber>5</identificationNumber>
                  <name>Smith</name>
                  <Address>
                    <streetAndNumber>5 main street</streetAndNumber>
                    <postcode>ab12 3cd</postcode>
                    <city>Newcastle</city>
                    <country>GB</country>
                  </Address>
                </Consignee>
                <Commodity>
                  <descriptionOfGoods>shorts</descriptionOfGoods>
                  <cusCode>0010001-6</cusCode>
                  <CommodityCode>
                    <harmonizedSystemSubHeadingCode>010121</harmonizedSystemSubHeadingCode>
                    <combinedNomenclatureCode>45</combinedNomenclatureCode>
                  </CommodityCode>
                  <DangerousGoods>
                    <sequenceNumber>1</sequenceNumber>
                    <UNNumber>0004</UNNumber>
                  </DangerousGoods>
                  <GoodsMeasure>
                    <grossMass>123.45</grossMass>
                    <netMass>123.45</netMass>
                  </GoodsMeasure>
                </Commodity>
                <Packaging>
                  <sequenceNumber>1</sequenceNumber>
                  <typeOfPackages>1A</typeOfPackages>
                  <numberOfPackages>99</numberOfPackages>
                  <shippingMarks>xyz</shippingMarks>
                </Packaging>
                <PreviousDocument>
                  <sequenceNumber>1</sequenceNumber>
                  <type>C612</type>
                  <referenceNumber>4</referenceNumber>
                  <goodsItemNumber>5</goodsItemNumber>
                  <complementOfInformation>1</complementOfInformation>
                </PreviousDocument>
                <SupportingDocument>
                  <sequenceNumber>1</sequenceNumber>
                  <type>L100</type>
                  <referenceNumber>8</referenceNumber>
                  <complementOfInformation>0</complementOfInformation>
                </SupportingDocument>
                <TransportDocument>
                  <sequenceNumber>1</sequenceNumber>
                  <type>N703</type>
                  <referenceNumber>4</referenceNumber>
                </TransportDocument>
                <AdditionalReference>
                  <sequenceNumber>1</sequenceNumber>
                  <type>Y015</type>
                  <referenceNumber>1</referenceNumber>
                </AdditionalReference>
                <AdditionalInformation>
                  <sequenceNumber>1</sequenceNumber>
                  <code>20300</code>
                  <text>additional info text</text>
                </AdditionalInformation>
              </ConsignmentItem>
            </HouseConsignment>
          </Consignment>
        </ncts:CC043C>

      when(mockConnector.getMessage(any(), any())(any(), any())).thenReturn(Future.successful(message))

      val result = Await.result(service.getUnloadingPermissionNotification(arrivalId, messageId), Duration.Inf)

      result mustBe a[CC043CType]
    }
  }
}
