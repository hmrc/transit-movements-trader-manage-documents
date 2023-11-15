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

package controllers.P5

import akka.util.ByteString
import base.SpecBase
import connectors.ReferenceDataP5Connector
import generated.p5.CC015CType
import generated.p5.CC029CType
import generators.ScalaxbModelGenerators
import models.reference.Country
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.{eq => eqTo}
import org.mockito.Mockito._
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import refactor.services.pdf.TADPdfGenerator
import services.P5.DepartureMessageP5Service

import scala.concurrent.Future

class TransitAccompanyingDocumentP5ControllerSpec extends SpecBase with ScalaxbModelGenerators with ScalaCheckPropertyChecks with BeforeAndAfterEach {

  def onwardRoute: Call = Call("GET", "/foo")

  private val departureId = "departureId"
  private val messageId   = "messageId"

  lazy val controllerRoute: String = routes.TransitAccompanyingDocumentP5Controller.get(departureId, messageId).url

  private lazy val mockMessageService           = mock[DepartureMessageP5Service]
  private lazy val mockPdfGenerator             = mock[TADPdfGenerator]
  private lazy val mockReferenceDataP5Connector = mock[ReferenceDataP5Connector]

  private val countries: Seq[Country] = Seq(Country("GB", "United Kingdom"))

  private def applicationBuilder(baseApplicationBuilder: GuiceApplicationBuilder = defaultApplicationBuilder()): GuiceApplicationBuilder =
    baseApplicationBuilder
      .overrides(
        bind[DepartureMessageP5Service].toInstance(mockMessageService),
        bind[TADPdfGenerator].toInstance(mockPdfGenerator),
        bind[ReferenceDataP5Connector].toInstance(mockReferenceDataP5Connector)
      )

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockMessageService)
    reset(mockPdfGenerator)
    reset(mockReferenceDataP5Connector)
  }

  "get" - {

    "must return OK and PDF" - {
      "when transition" in {
        val application = applicationBuilder(p5TransitionApplicationBuilder()).build()
        running(application) {
          forAll(nonEmptyString, arbitrary[CC015CType], arbitrary[CC029CType], arbitrary[Array[Byte]]) {
            (ie015MessageId, ie015, ie029, byteArray) =>
              beforeEach()

              when(mockMessageService.getIE015MessageId(any())(any(), any()))
                .thenReturn(Future.successful(Some(ie015MessageId)))

              when(mockMessageService.getDeclarationData(any(), any())(any(), any()))
                .thenReturn(Future.successful(ie015))

              when(mockMessageService.getReleaseForTransitNotification(any(), any())(any(), any()))
                .thenReturn(Future.successful(ie029))

              when(mockReferenceDataP5Connector.getListWithDefault[Country](any())(any(), any(), any()))
                .thenReturn(Future.successful(countries))

              when(mockPdfGenerator.generateP5TADTransition(any(), any(), any()))
                .thenReturn(byteArray)

              val request = FakeRequest(GET, controllerRoute)

              val result = route(application, request).value

              status(result) mustEqual OK
              contentAsBytes(result) mustEqual ByteString(byteArray)
              headers(result).get(CONTENT_TYPE).value mustEqual "application/pdf"
              val mrn = ie029.TransitOperation.MRN
              headers(result).get(CONTENT_DISPOSITION).value mustEqual s"""attachment; filename="TAD_$mrn.pdf""""

              verify(mockMessageService).getIE015MessageId(eqTo(departureId))(any(), any())
              verify(mockMessageService).getDeclarationData(eqTo(departureId), eqTo(ie015MessageId))(any(), any())
              verify(mockMessageService).getReleaseForTransitNotification(eqTo(departureId), eqTo(messageId))(any(), any())
              verify(mockReferenceDataP5Connector).getListWithDefault[Country](eqTo("CountryCodesForAddress"))(any(), any(), any())
              verify(mockPdfGenerator).generateP5TADTransition(eqTo(ie015), eqTo(ie029), eqTo(countries))
              verify(mockPdfGenerator, never()).generateP5TADPostTransition(any())
          }
        }
      }

      "when post-transition" in {
        val application = applicationBuilder(p5PostTransitionApplicationBuilder()).build()
        running(application) {
          forAll(nonEmptyString, arbitrary[CC015CType], arbitrary[CC029CType], arbitrary[Array[Byte]]) {
            (ie015MessageId, ie015, ie029, byteArray) =>
              beforeEach()

              when(mockMessageService.getIE015MessageId(any())(any(), any()))
                .thenReturn(Future.successful(Some(ie015MessageId)))

              when(mockMessageService.getReleaseForTransitNotification(any(), any())(any(), any()))
                .thenReturn(Future.successful(ie029))

              when(mockPdfGenerator.generateP5TADPostTransition(any()))
                .thenReturn(byteArray)

              val request = FakeRequest(GET, controllerRoute)

              val result = route(application, request).value

              status(result) mustEqual OK
              contentAsBytes(result) mustEqual ByteString(byteArray)
              headers(result).get(CONTENT_TYPE).value mustEqual "application/pdf"
              val mrn = ie029.TransitOperation.MRN
              headers(result).get(CONTENT_DISPOSITION).value mustEqual s"""attachment; filename="TAD_$mrn.pdf""""

              verify(mockMessageService).getIE015MessageId(eqTo(departureId))(any(), any())
              verify(mockMessageService, never()).getDeclarationData(any(), any())(any(), any())
              verify(mockMessageService).getReleaseForTransitNotification(eqTo(departureId), eqTo(messageId))(any(), any())
              verifyNoInteractions(mockReferenceDataP5Connector)
              verify(mockPdfGenerator, never()).generateP5TADTransition(any(), any(), any())
              verify(mockPdfGenerator).generateP5TADPostTransition(eqTo(ie029))
          }
        }
      }
    }

    "must return INTERNAL SERVER ERROR" - {
      "when IE015 message ID not found" in {
        val application = applicationBuilder().build()
        running(application) {
          when(mockMessageService.getIE015MessageId(any())(any(), any()))
            .thenReturn(Future.successful(None))

          val request = FakeRequest(GET, controllerRoute)

          val result = route(application, request).value

          status(result) mustEqual INTERNAL_SERVER_ERROR

          verify(mockMessageService).getIE015MessageId(eqTo(departureId))(any(), any())
          verify(mockMessageService, never()).getDeclarationData(any(), any())(any(), any())
          verify(mockMessageService, never()).getReleaseForTransitNotification(any(), any())(any(), any())
          verifyNoInteractions(mockReferenceDataP5Connector)
          verifyNoInteractions(mockPdfGenerator)
        }
      }
    }
  }
}
