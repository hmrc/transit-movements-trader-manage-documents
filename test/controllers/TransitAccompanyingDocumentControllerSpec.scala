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

package controllers

import base.SpecBase
import controllers.actions.{AuthenticateActionProvider, FakeAuthenticateActionProvider}
import generated.CC029CType
import generators.ScalaxbModelGenerators
import models.{IE015, Phase}
import org.apache.pekko.util.ByteString
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.*
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import services.messages.DepartureMessageService
import services.pdf.TADPdfGenerator

import scala.concurrent.Future

class TransitAccompanyingDocumentControllerSpec extends SpecBase with ScalaxbModelGenerators with ScalaCheckPropertyChecks with BeforeAndAfterEach {

  def onwardRoute: Call = Call("GET", "/foo")

  private val departureId = "departureId"
  private val messageId   = "messageId"

  lazy val controllerRoute: String = routes.TransitAccompanyingDocumentController.get(departureId, messageId).url

  private lazy val mockMessageService = mock[DepartureMessageService]
  private lazy val mockPdfGenerator   = mock[TADPdfGenerator]

  private def applicationBuilder(): GuiceApplicationBuilder =
    new GuiceApplicationBuilder()
      .overrides(
        bind[AuthenticateActionProvider].to[FakeAuthenticateActionProvider],
        bind[DepartureMessageService].toInstance(mockMessageService),
        bind[TADPdfGenerator].toInstance(mockPdfGenerator)
      )

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockMessageService)
    reset(mockPdfGenerator)
  }

  "get" - {

    "must return OK and PDF" - {
      "when transition" in {
        val application = applicationBuilder().build()
        running(application) {
          forAll(nonEmptyString, arbitrary[IE015], arbitrary[CC029CType], arbitrary[Array[Byte]]) {
            (ie015MessageId, ie015, ie029, byteArray) =>
              beforeEach()

              when(mockMessageService.getIE015MessageId(any(), any())(any(), any()))
                .thenReturn(Future.successful(Some(ie015MessageId)))

              when(mockMessageService.getDeclarationData(any(), any(), any())(any(), any()))
                .thenReturn(Future.successful(ie015))

              when(mockMessageService.getReleaseForTransitNotification(any(), any(), any())(any(), any()))
                .thenReturn(Future.successful(ie029))

              when(mockPdfGenerator.generateP5TADTransition(any(), any()))
                .thenReturn(byteArray)

              val request = FakeRequest(GET, controllerRoute)
                .withHeaders("APIVersion" -> "2.0")

              val result = route(application, request).value

              status(result) mustEqual OK
              contentAsBytes(result) mustEqual ByteString(byteArray)
              contentType(result).value mustEqual "application/octet-stream"
              val mrn = ie029.TransitOperation.MRN
              headers(result).get(CONTENT_DISPOSITION).value mustEqual s"""attachment; filename="TAD_$mrn.pdf""""

              verify(mockMessageService).getIE015MessageId(eqTo(departureId), eqTo(Phase.Transition))(any(), any())
              verify(mockMessageService).getDeclarationData(eqTo(departureId), eqTo(ie015MessageId), eqTo(Phase.Transition))(any(), any())
              verify(mockMessageService).getReleaseForTransitNotification(eqTo(departureId), eqTo(messageId), eqTo(Phase.Transition))(any(), any())
              verify(mockPdfGenerator).generateP5TADTransition(eqTo(ie015), eqTo(ie029))
              verify(mockPdfGenerator, never()).generateP5TADPostTransition(any(), any())
          }
        }
      }

      "when post-transition" in {
        val application = applicationBuilder().build()
        running(application) {
          forAll(nonEmptyString, arbitrary[IE015], arbitrary[CC029CType], arbitrary[Array[Byte]]) {
            (ie015MessageId, ie015, ie029, byteArray) =>
              beforeEach()

              when(mockMessageService.getIE015MessageId(any(), any())(any(), any()))
                .thenReturn(Future.successful(Some(ie015MessageId)))

              when(mockMessageService.getDeclarationData(any(), any(), any())(any(), any()))
                .thenReturn(Future.successful(ie015))

              when(mockMessageService.getReleaseForTransitNotification(any(), any(), any())(any(), any()))
                .thenReturn(Future.successful(ie029))

              when(mockPdfGenerator.generateP5TADPostTransition(any(), any()))
                .thenReturn(byteArray)

              val request = FakeRequest(GET, controllerRoute)
                .withHeaders("APIVersion" -> "2.1")

              val result = route(application, request).value

              status(result) mustEqual OK
              contentAsBytes(result) mustEqual ByteString(byteArray)
              contentType(result).value mustEqual "application/octet-stream"
              val mrn = ie029.TransitOperation.MRN
              headers(result).get(CONTENT_DISPOSITION).value mustEqual s"""attachment; filename="TAD_$mrn.pdf""""

              verify(mockMessageService).getIE015MessageId(eqTo(departureId), eqTo(Phase.PostTransition))(any(), any())
              verify(mockMessageService).getDeclarationData(eqTo(departureId), eqTo(ie015MessageId), eqTo(Phase.PostTransition))(any(), any())
              verify(mockMessageService).getReleaseForTransitNotification(eqTo(departureId), eqTo(messageId), eqTo(Phase.PostTransition))(any(), any())
              verify(mockPdfGenerator, never()).generateP5TADTransition(any(), any())
              verify(mockPdfGenerator).generateP5TADPostTransition(eqTo(ie015), eqTo(ie029))
          }
        }
      }
    }

    "must return INTERNAL SERVER ERROR" - {
      "when IE015 message ID not found" in {
        val application = applicationBuilder().build()
        running(application) {
          when(mockMessageService.getIE015MessageId(any(), any())(any(), any()))
            .thenReturn(Future.successful(None))

          val request = FakeRequest(GET, controllerRoute)
            .withHeaders("APIVersion" -> "2.0")

          val result = route(application, request).value

          status(result) mustEqual INTERNAL_SERVER_ERROR

          verify(mockMessageService).getIE015MessageId(eqTo(departureId), eqTo(Phase.Transition))(any(), any())
          verify(mockMessageService, never()).getDeclarationData(any(), any(), any())(any(), any())
          verify(mockMessageService, never()).getReleaseForTransitNotification(any(), any(), any())(any(), any())
          verifyNoInteractions(mockPdfGenerator)
        }
      }
    }
  }
}
