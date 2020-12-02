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
import generators.ViewmodelGenerators
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
import services.pdf.UnloadingPermissionPdfGenerator

import scala.concurrent.Future

class TransitAccompanyingDocumentControllerSpec
    extends FreeSpec
    with MustMatchers
    with GuiceOneAppPerSuite
    with OptionValues
    with MockitoSugar
    with ScalaCheckPropertyChecks
    with ViewmodelGenerators {

  def onwardRoute: Call = Call("GET", "/foo")

  lazy val controllerRoute: String = routes.TransitAccompanyingDocumentController.get().url

  "get" - {

    def applicationBuilder: GuiceApplicationBuilder =
      new GuiceApplicationBuilder()
        .configure(Configuration("metrics.enabled" -> "false"))

    "must return OK and PDF" in {

      val mockPDFGenerator: UnloadingPermissionPdfGenerator                   = mock[UnloadingPermissionPdfGenerator]
      val mockConversionService: TransitAccompanyingDocumentConversionService = mock[TransitAccompanyingDocumentConversionService]

      val application = applicationBuilder
        .overrides {
          bind[UnloadingPermissionPdfGenerator].toInstance(mockPDFGenerator)
          bind[TransitAccompanyingDocumentConversionService].toInstance(mockConversionService)
        }
        .build()

      running(application) {

        forAll(arbitrary[viewmodels.TransitAccompanyingDocument], arbitrary[Array[Byte]]) {
          (viewModel, pdf) =>
            when(mockConversionService.toViewModel(any(), any())(any(), any()))
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

      val mockPDFGenerator: UnloadingPermissionPdfGenerator                   = mock[UnloadingPermissionPdfGenerator]
      val mockConversionService: TransitAccompanyingDocumentConversionService = mock[TransitAccompanyingDocumentConversionService]

      val application = applicationBuilder
        .overrides {
          bind[UnloadingPermissionPdfGenerator].toInstance(mockPDFGenerator)
          bind[TransitAccompanyingDocumentConversionService].toInstance(mockConversionService)
        }
        .build()

      running(application) {

        forAll(arbitrary[Array[Byte]]) {
          pdf =>
            when(mockConversionService.toViewModel(any(), any())(any(), any()))
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
        <RefNumHEA4>LRNVALUE</RefNumHEA4>
        <TypOfDecHEA24>T2</TypOfDecHEA24>
          <CouOfDisCodHEA55>GB</CouOfDisCodHEA55>
          <CouOfDesCodHEA30>IT</CouOfDesCodHEA30>
          <IdeOfMeaOfTraAtDHEA78>abcd</IdeOfMeaOfTraAtDHEA78>
          <NatOfMeaOfTraAtDHEA80>IT</NatOfMeaOfTraAtDHEA80>
        <TotNumOfIteHEA305>1</TotNumOfIteHEA305>
        <TotNumOfPacHEA306>1</TotNumOfPacHEA306>
        <TotGroMasHEA307>1000</TotGroMasHEA307>
      </HEAHEA>
      <TRAPRIPC1>
        <NamPC17>Mancini Carriers</NamPC17>
        <StrAndNumPC122>90 Desio Way</StrAndNumPC122>
        <PosCodPC123>MOD 5JJ</PosCodPC123>
        <CitPC124>Modena</CitPC124>
        <CouPC125>IT</CouPC125>
        <TINPC159>IT444100201000</TINPC159>
      </TRAPRIPC1>
      <TRACONCO1>
        <NamCO17>Mancini Carriers</NamCO17>
        <StrAndNumCO122>90 Desio Way</StrAndNumCO122>
        <PosCodCO123>MOD 5JJ</PosCodCO123>
        <CitCO124>Modena</CitCO124>
        <CouCO125>IT</CouCO125>
        <TINCO159>IT444100201000</TINCO159>
      </TRACONCO1>
      <TRACONCE1>
        <NamCE17>Mancini Carriers</NamCE17>
        <StrAndNumCE122>90 Desio Way</StrAndNumCE122>
        <PosCodCE123>MOD 5JJ</PosCodCE123>
        <CitCE124>Modena</CitCE124>
        <CouCE125>IT</CouCE125>
        <TINCE159>IT444100201000</TINCE159>
      </TRACONCE1>
      <CUSOFFDEPEPT>
        <RefNumEPT1>GB000060</RefNumEPT1>
      </CUSOFFDEPEPT>
      <CUSOFFTRARNS>
        <RefNumRNS1>GB000060</RefNumRNS1>
      </CUSOFFTRARNS>
      <GOOITEGDS>
        <IteNumGDS7>1</IteNumGDS7>
        <GooDesGDS23>Flowers</GooDesGDS23>
        <GroMasGDS46>1000</GroMasGDS46>
        <NetMasGDS48>999</NetMasGDS48>
        <CouOfDisGDS58>GB</CouOfDisGDS58>
        <CouOfDesGDS59>GB</CouOfDesGDS59>
        <PREADMREFAR2>
          <PreDocTypAR21>Type</PreDocTypAR21>
          <PreDocRefAR26>Ref</PreDocRefAR26>
        </PREADMREFAR2>
        <PRODOCDC2>
          <DocTypDC21>235</DocTypDC21>
          <DocRefDC23>Ref.</DocRefDC23>
        </PRODOCDC2>
        <PACGS2>
          <MarNumOfPacGS21>Ref.</MarNumOfPacGS21>
          <KinOfPacGS23>BX</KinOfPacGS23>
          <NumOfPacGS24>1</NumOfPacGS24>
        </PACGS2>
        <SGICODSD2>
          <SenGooCodSD22>1</SenGooCodSD22>
          <SenQuaSD23>1</SenQuaSD23>
        </SGICODSD2>
      </GOOITEGDS>
    </CC015A>

}
