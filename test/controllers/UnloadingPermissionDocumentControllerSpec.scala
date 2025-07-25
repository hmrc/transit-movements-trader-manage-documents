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
import generated.CC043CType
import generators.IE043ScalaxbModelGenerators
import models.Version
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
import services.messages.UnloadingMessageService
import services.pdf.UnloadingPermissionPdfGenerator

import scala.concurrent.Future

class UnloadingPermissionDocumentControllerSpec extends SpecBase with IE043ScalaxbModelGenerators with ScalaCheckPropertyChecks with BeforeAndAfterEach {

  def onwardRoute: Call = Call("GET", "/foo")

  private val arrivalId = "arrivalId"
  private val messageId = "messageId"
  private val version   = Version("2.1")

  lazy val controllerRoute: String = routes.UnloadingPermissionDocumentController.get(arrivalId, messageId).url

  private lazy val mockMessageService = mock[UnloadingMessageService]
  private lazy val mockPdfGenerator   = mock[UnloadingPermissionPdfGenerator]

  private def applicationBuilder(): GuiceApplicationBuilder =
    new GuiceApplicationBuilder()
      .overrides(
        bind[AuthenticateActionProvider].to[FakeAuthenticateActionProvider],
        bind[UnloadingMessageService].toInstance(mockMessageService),
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

            when(mockMessageService.getUnloadingPermissionNotification(any(), any(), any())(any(), any()))
              .thenReturn(Future.successful(ie043))

            when(mockPdfGenerator.generate(any()))
              .thenReturn(byteArray)

            val request = FakeRequest(GET, controllerRoute)
              .withHeaders("API-Version" -> "2.1")

            val result = route(application, request).value

            status(result) mustEqual OK
            contentAsBytes(result) mustEqual ByteString(byteArray)
            contentType(result).value mustEqual "application/octet-stream"
            val mrn = ie043.TransitOperation.MRN
            headers(result).get(CONTENT_DISPOSITION).value mustEqual s"""attachment; filename="UPD_$mrn.pdf""""

            verify(mockMessageService).getUnloadingPermissionNotification(eqTo(arrivalId), eqTo(messageId), eqTo(version))(any(), any())
            verify(mockPdfGenerator).generate(eqTo(ie043))
        }
      }
    }
  }
}
