/*
 * Copyright 2020 HM Revenue & Customs
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

import cats.data.Validated.Invalid
import cats.data.Validated.Valid
import cats.data._
import generators.TadViewModelGenerators
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Configuration
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers.route
import play.api.test.Helpers.status
import play.api.test.Helpers._
import services.ReferenceDataRetrievalError
import services.conversion.TransitAccompanyingDocumentConversionService
import services.pdf.TransitAccompanyingDocumentPdfGenerator

import scala.concurrent.Future

class TransitAccompanyingDocumentControllerSpec
    extends FreeSpec
    with MustMatchers
    with GuiceOneAppPerSuite
    with OptionValues
    with MockitoSugar
    with ScalaCheckPropertyChecks
    with TadViewModelGenerators {

  def onwardRoute: Call = Call("GET", "/foo")

  lazy val controllerRoute: String = routes.TransitAccompanyingDocumentController.get().url

  "get" - {

    def applicationBuilder: GuiceApplicationBuilder =
      new GuiceApplicationBuilder()
        .configure(Configuration("metrics.enabled" -> "false"))

    "must return OK and PDF" in {

      val mockPDFGenerator: TransitAccompanyingDocumentPdfGenerator           = mock[TransitAccompanyingDocumentPdfGenerator]
      val mockConversionService: TransitAccompanyingDocumentConversionService = mock[TransitAccompanyingDocumentConversionService]

      val application = applicationBuilder
        .overrides {
          bind[TransitAccompanyingDocumentPdfGenerator].toInstance(mockPDFGenerator)
          bind[TransitAccompanyingDocumentConversionService].toInstance(mockConversionService)
        }
        .build()

      running(application) {

        forAll(arbitrary[viewmodels.tad.TransitAccompanyingDocument], arbitrary[Array[Byte]]) {
          (viewModel, pdf) =>
            when(mockConversionService.toViewModel(any())(any(), any()))
              .thenReturn(Future.successful(Valid(viewModel)))

            when(mockPDFGenerator.generate(any()))
              .thenReturn(pdf)

            val request = FakeRequest(POST, controllerRoute).withXmlBody(declarationXml)

            val result = route(application, request).value

            status(result) mustEqual OK
        }
      }
    }

    "must return a BadRequest when given an invalid XML" in {

      val application = applicationBuilder.build()

      running(application) {

        val request = FakeRequest(POST, controllerRoute).withXmlBody(<invalid></invalid>)

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
      }
    }

    "must return and InternalServerError if the conversion fails" in {

      val mockPDFGenerator: TransitAccompanyingDocumentPdfGenerator           = mock[TransitAccompanyingDocumentPdfGenerator]
      val mockConversionService: TransitAccompanyingDocumentConversionService = mock[TransitAccompanyingDocumentConversionService]

      val application = applicationBuilder
        .overrides {
          bind[TransitAccompanyingDocumentPdfGenerator].toInstance(mockPDFGenerator)
          bind[TransitAccompanyingDocumentConversionService].toInstance(mockConversionService)
        }
        .build()

      running(application) {

        forAll(arbitrary[Array[Byte]]) {
          pdf =>
            when(mockConversionService.toViewModel(any())(any(), any()))
              .thenReturn(Future.successful(Invalid(NonEmptyChain(ReferenceDataRetrievalError("", 500, "")))))

            when(mockPDFGenerator.generate(any()))
              .thenReturn(pdf)

            val request = FakeRequest(POST, controllerRoute).withXmlBody(declarationXml)

            val result = route(application, request).value

            status(result) mustEqual INTERNAL_SERVER_ERROR
        }
      }
    }
  }

  //TODO: Add additional nodes here
  private def declarationXml =
    <CC015A>
      <HEAHEA>
        <RefNumHEA4>99IT9876AB88901209</RefNumHEA4>
      </HEAHEA>
    </CC015A>

}
