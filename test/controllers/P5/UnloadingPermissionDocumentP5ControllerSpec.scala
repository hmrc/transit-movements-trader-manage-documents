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

import base.SpecBase
import generated.p5.CC043CType
import generators.ScalaxbModelGenerators
import org.apache.pekko.util.ByteString
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
import refactor.services.pdf.UnloadingPermissionPdfGenerator
import services.P5.UnloadingMessageP5Service

import scala.concurrent.Future

class UnloadingPermissionDocumentP5ControllerSpec extends SpecBase with ScalaxbModelGenerators with ScalaCheckPropertyChecks with BeforeAndAfterEach {

  def onwardRoute: Call = Call("GET", "/foo")

  private val arrivalId = "arrivalId"
  private val messageId = "messageId"

  lazy val controllerRoute: String = routes.UnloadingPermissionDocumentP5Controller.get(arrivalId, messageId).url

  private lazy val mockMessageService = mock[UnloadingMessageP5Service]
  private lazy val mockPdfGenerator   = mock[UnloadingPermissionPdfGenerator]

  private def applicationBuilder(): GuiceApplicationBuilder =
    defaultApplicationBuilder()
      .overrides(
        bind[UnloadingMessageP5Service].toInstance(mockMessageService),
        bind[UnloadingPermissionPdfGenerator].toInstance(mockPdfGenerator)
      )

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockMessageService)
    reset(mockPdfGenerator)
  }

  "get" - {

    "must return OK and PDF" in {
      val application = applicationBuilder().build()
      running(application) {
        forAll(arbitrary[CC043CType], arbitrary[Array[Byte]]) {
          (ie043, byteArray) =>
            beforeEach()

            when(mockMessageService.getUnloadingPermissionNotification(any(), any())(any(), any()))
              .thenReturn(Future.successful(ie043))

            when(mockPdfGenerator.generateP5(any()))
              .thenReturn(byteArray)

            val request = FakeRequest(GET, controllerRoute)

            val result = route(application, request).value

            status(result) mustEqual OK
            contentAsBytes(result) mustEqual ByteString(byteArray)
            headers(result).get(CONTENT_TYPE).value mustEqual "application/pdf"
            val mrn = ie043.TransitOperation.MRN
            headers(result).get(CONTENT_DISPOSITION).value mustEqual s"""attachment; filename="UPD_$mrn.pdf""""

            verify(mockMessageService).getUnloadingPermissionNotification(eqTo(arrivalId), eqTo(messageId))(any(), any())
            verify(mockPdfGenerator).generateP5(eqTo(ie043))
        }
      }
    }
  }
}
