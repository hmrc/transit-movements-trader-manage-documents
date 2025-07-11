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
import connectors.DepartureMovementConnector
import generated.*
import models.DepartureMessageType.DepartureNotification
import models.{DepartureMessageMetaData, DepartureMessages, Version}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scalaxb.XMLCalendar
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.tad.*

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.xml.Node

class DepartureMessageServiceSpec extends SpecBase with ScalaFutures with ScalaCheckPropertyChecks {

  private val mockConnector = mock[DepartureMovementConnector]

  private val service = new DepartureMessageService(mockConnector)

  private val departureId = "departureId"
  private val messageId   = "messageId"
  private val version     = Version("2.1")

  implicit private val hc: HeaderCarrier = new HeaderCarrier()

  "getIE015MessageId" - {
    "must return None" - {
      "when IE015 message not found" in {
        val messages = DepartureMessages(List.empty)

        when(mockConnector.getMessages(any())(any(), any()))
          .thenReturn(Future.successful(messages))

        val result = service.getIE015MessageId(departureId).futureValue

        result must not be defined
      }
    }

    "must return IE015 message ID" - {
      "when IE015 message found" in {
        val messageId = "ie015 id"
        val ie015     = DepartureMessageMetaData(messageId, LocalDateTime.now(), DepartureNotification, "")
        val messages  = DepartureMessages(List(ie015))

        when(mockConnector.getMessages(any())(any(), any()))
          .thenReturn(Future.successful(messages))

        val result = service.getIE015MessageId(departureId).futureValue

        result.value mustEqual messageId
      }
    }
  }

  "getReleaseForTransitNotification" - {
    "must parse CC029CType" in {
      val message: Node =
        <ncts:CC029C xmlns:ncts="http://ncts.dgtaxud.ec">
          <messageSender>NTA.GB</messageSender>
          <messageRecipient>1234567</messageRecipient>
          <preparationDateAndTime>2024-12-04T15:31:57</preparationDateAndTime>
          <messageIdentification>83ONFTMIOXMT11</messageIdentification>
          <messageType>CC029C</messageType>
          <TransitOperation>
            <LRN>GB8d53f132d00045f3c154</LRN>
            <MRN>24GB000246J8NY33L7</MRN>
            <declarationType>T1</declarationType>
            <additionalDeclarationType>A</additionalDeclarationType>
            <declarationAcceptanceDate>2024-12-04</declarationAcceptanceDate>
            <releaseDate>2024-12-04</releaseDate>
            <security>2</security>
            <reducedDatasetIndicator>0</reducedDatasetIndicator>
            <bindingItinerary>1</bindingItinerary>
          </TransitOperation>
          <CustomsOfficeOfDeparture>
            <referenceNumber>GB000246</referenceNumber>
          </CustomsOfficeOfDeparture>
          <CustomsOfficeOfDestinationDeclared>
            <referenceNumber>XI000142</referenceNumber>
          </CustomsOfficeOfDestinationDeclared>
          <HolderOfTheTransitProcedure>
            <identificationNumber>GB201909015000</identificationNumber>
          </HolderOfTheTransitProcedure>
          <Guarantee>
            <sequenceNumber>1</sequenceNumber>
            <guaranteeType>1</guaranteeType>
          </Guarantee>
          <Consignment>
            <countryOfDispatch>XI</countryOfDispatch>
            <countryOfDestination>US</countryOfDestination>
            <containerIndicator>1</containerIndicator>
            <grossMass>5500</grossMass>
            <referenceNumberUCR>AB1234</referenceNumberUCR>
            <HouseConsignment>
              <sequenceNumber>1</sequenceNumber>
              <grossMass>5500</grossMass>
              <ConsignmentItem>
                <goodsItemNumber>1</goodsItemNumber>
                <declarationGoodsItemNumber>1</declarationGoodsItemNumber>
                <Commodity>
                  <descriptionOfGoods>Toddlers Wooden Toy</descriptionOfGoods>
                </Commodity>
              </ConsignmentItem>
            </HouseConsignment>
          </Consignment>
        </ncts:CC029C>

      when(mockConnector.getMessage(any(), any(), any())(any(), any()))
        .thenReturn(Future.successful(message))

      val result = Await.result(service.getReleaseForTransitNotification(departureId, messageId, version), Duration.Inf)

      result mustEqual CC029CType(
        messageSequence1 = MESSAGESequence(
          messageSender = "NTA.GB",
          messageRecipient = "1234567",
          preparationDateAndTime = XMLCalendar("2024-12-04T15:31:57"),
          messageIdentification = "83ONFTMIOXMT11",
          messageType = CC029C
        ),
        TransitOperation = new TransitOperation(
          LRN = "GB8d53f132d00045f3c154",
          MRN = "24GB000246J8NY33L7",
          declarationType = "T1",
          additionalDeclarationType = "A",
          declarationAcceptanceDate = XMLCalendar("2024-12-04"),
          releaseDate = XMLCalendar("2024-12-04"),
          security = "2",
          reducedDatasetIndicator = Number0,
          bindingItinerary = Number1
        ),
        CustomsOfficeOfDeparture = new CustomsOfficeOfDeparture(
          referenceNumber = "GB000246"
        ),
        CustomsOfficeOfDestinationDeclared = new CustomsOfficeOfDestinationDeclared(
          referenceNumber = "XI000142"
        ),
        HolderOfTheTransitProcedure = new HolderOfTheTransitProcedure(
          identificationNumber = Some("GB201909015000")
        ),
        Guarantee = Seq(
          new Guarantee(
            sequenceNumber = 1,
            guaranteeType = "1"
          )
        ),
        Consignment = new Consignment(
          countryOfDispatch = Some("XI"),
          countryOfDestination = Some("US"),
          containerIndicator = Number1,
          grossMass = 5500,
          referenceNumberUCR = Some("AB1234"),
          HouseConsignment = Seq(
            new HouseConsignment(
              sequenceNumber = 1,
              grossMass = 5500,
              ConsignmentItem = Seq(
                new ConsignmentItem(
                  goodsItemNumber = 1,
                  declarationGoodsItemNumber = 1,
                  Commodity = new Commodity(
                    descriptionOfGoods = "Toddlers Wooden Toy"
                  )
                )
              )
            )
          )
        )
      )
    }
  }
}
