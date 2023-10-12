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
import cats.data.NonEmptyChain
import cats.data.Validated
import generators.ViewModelGenerators
import models.P5.departure.IE015
import models.P5.departure.IE029
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
import services.P5.DepartureMessageP5Service
import services.conversion.TransitAccompanyingDocumentConversionService
import services.pdf.TADPdfGenerator
import services.JsonError
import services.ValidationResult
import viewmodels.TransitAccompanyingDocumentP5TransitionPDF

import scala.concurrent.Future

class TransitAccompanyingDocumentP5ControllerSpec extends SpecBase with ViewModelGenerators with ScalaCheckPropertyChecks with BeforeAndAfterEach {

  def onwardRoute: Call = Call("GET", "/foo")

  private val departureId = "departureId"
  private val messageId   = "messageId"

  lazy val controllerRoute: String = routes.TransitAccompanyingDocumentP5Controller.get(departureId, messageId).url

  private lazy val mockMessageService    = mock[DepartureMessageP5Service]
  private lazy val mockPdfGenerator      = mock[TADPdfGenerator]
  private lazy val mockConversionService = mock[TransitAccompanyingDocumentConversionService]

  private val validationError: ValidationResult[TransitAccompanyingDocumentP5TransitionPDF] =
    Validated.Invalid(NonEmptyChain(JsonError("error", Nil)))

  private def applicationBuilder(baseApplicationBuilder: GuiceApplicationBuilder = defaultApplicationBuilder()): GuiceApplicationBuilder =
    baseApplicationBuilder
      .overrides(
        bind[DepartureMessageP5Service].toInstance(mockMessageService),
        bind[TADPdfGenerator].toInstance(mockPdfGenerator),
        bind[TransitAccompanyingDocumentConversionService].toInstance(mockConversionService)
      )

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockMessageService)
    reset(mockPdfGenerator)
    reset(mockConversionService)
  }

  "get" - {

    "must return OK and PDF" - {
      "when transition" in {
        val application = applicationBuilder(p5TransitionApplicationBuilder()).build()
        running(application) {
          forAll(nonEmptyString, arbitrary[IE015], arbitrary[IE029], arbitrary[TransitAccompanyingDocumentP5TransitionPDF], arbitrary[Array[Byte]]) {
            (ie015MessageId, ie015, ie029, p5TransitionPdf, byteArray) =>
              beforeEach()

              when(mockMessageService.getIE015MessageId(any())(any(), any()))
                .thenReturn(Future.successful(Some(ie015MessageId)))

              when(mockMessageService.getDeclarationData(any(), any())(any(), any()))
                .thenReturn(Future.successful(ie015))

              when(mockMessageService.getReleaseForTransitNotification(any(), any())(any(), any()))
                .thenReturn(Future.successful(ie029))

              when(mockConversionService.fromP5ToViewModel(any(), any())(any(), any()))
                .thenReturn(Future.successful(Validated.Valid(p5TransitionPdf)))

              when(mockPdfGenerator.generateP5TADTransition(any()))
                .thenReturn(byteArray)

              val request = FakeRequest(GET, controllerRoute)

              val result = route(application, request).value

              status(result) mustEqual OK
              contentAsBytes(result) mustEqual ByteString(byteArray)
              headers(result).get(CONTENT_TYPE).value mustEqual "application/pdf"
              val mrn = ie029.data.TransitOperation.MRN
              headers(result).get(CONTENT_DISPOSITION).value mustEqual s"""attachment; filename="TAD_$mrn.pdf""""

              verify(mockMessageService).getIE015MessageId(eqTo(departureId))(any(), any())
              verify(mockMessageService).getDeclarationData(eqTo(departureId), eqTo(ie015MessageId))(any(), any())
              verify(mockMessageService).getReleaseForTransitNotification(eqTo(departureId), eqTo(messageId))(any(), any())
              verify(mockConversionService).fromP5ToViewModel(eqTo(ie029), eqTo(ie015))(any(), any())
              verify(mockPdfGenerator).generateP5TADTransition(eqTo(p5TransitionPdf))
              verify(mockPdfGenerator, never()).generateP5TADPostTransition(any())
          }
        }
      }

      "when post-transition" in {
        val application = applicationBuilder(p5PostTransitionApplicationBuilder()).build()
        running(application) {
          forAll(nonEmptyString, arbitrary[IE015], arbitrary[IE029], arbitrary[Array[Byte]]) {
            (ie015MessageId, ie015, ie029, byteArray) =>
              beforeEach()

              when(mockMessageService.getIE015MessageId(any())(any(), any()))
                .thenReturn(Future.successful(Some(ie015MessageId)))

              when(mockMessageService.getDeclarationData(any(), any())(any(), any()))
                .thenReturn(Future.successful(ie015))

              when(mockMessageService.getReleaseForTransitNotification(any(), any())(any(), any()))
                .thenReturn(Future.successful(ie029))

              when(mockPdfGenerator.generateP5TADPostTransition(any()))
                .thenReturn(byteArray)

              val request = FakeRequest(GET, controllerRoute)

              val result = route(application, request).value

              status(result) mustEqual OK
              contentAsBytes(result) mustEqual ByteString(byteArray)
              headers(result).get(CONTENT_TYPE).value mustEqual "application/pdf"
              val mrn = ie029.data.TransitOperation.MRN
              headers(result).get(CONTENT_DISPOSITION).value mustEqual s"""attachment; filename="TAD_$mrn.pdf""""

              verify(mockMessageService).getIE015MessageId(eqTo(departureId))(any(), any())
              verify(mockMessageService).getDeclarationData(eqTo(departureId), eqTo(ie015MessageId))(any(), any())
              verify(mockMessageService).getReleaseForTransitNotification(eqTo(departureId), eqTo(messageId))(any(), any())
              verifyNoInteractions(mockConversionService)
              verify(mockPdfGenerator, never()).generateP5TADTransition(any())
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
          verifyNoInteractions(mockConversionService)
          verifyNoInteractions(mockPdfGenerator)
        }
      }

      "when conversion fails for P5 transition" in {
        val application = applicationBuilder(p5TransitionApplicationBuilder()).build()
        running(application) {
          forAll(nonEmptyString, arbitrary[IE015], arbitrary[IE029]) {
            (ie015MessageId, ie015, ie029) =>
              beforeEach()

              when(mockMessageService.getIE015MessageId(any())(any(), any()))
                .thenReturn(Future.successful(Some(ie015MessageId)))

              when(mockMessageService.getDeclarationData(any(), any())(any(), any()))
                .thenReturn(Future.successful(ie015))

              when(mockMessageService.getReleaseForTransitNotification(any(), any())(any(), any()))
                .thenReturn(Future.successful(ie029))

              when(mockConversionService.fromP5ToViewModel(any(), any())(any(), any()))
                .thenReturn(Future.successful(validationError))

              val request = FakeRequest(GET, controllerRoute)

              val result = route(application, request).value

              status(result) mustEqual INTERNAL_SERVER_ERROR

              verify(mockMessageService).getIE015MessageId(eqTo(departureId))(any(), any())
              verify(mockMessageService).getDeclarationData(eqTo(departureId), eqTo(ie015MessageId))(any(), any())
              verify(mockMessageService).getReleaseForTransitNotification(eqTo(departureId), eqTo(messageId))(any(), any())
              verify(mockConversionService).fromP5ToViewModel(eqTo(ie029), eqTo(ie015))(any(), any())
              verifyNoInteractions(mockPdfGenerator)
          }
        }
      }
    }
  }
}
