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

import cats.data.Validated.Invalid
import cats.data.Validated.Valid
import cats.data._
import generators.ViewModelGenerators
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Configuration
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.ReferenceDataRetrievalError
import services.conversion.UnloadingPermissionConversionService
import services.pdf.UnloadingPermissionPdfGenerator

import scala.concurrent.Future

class UnloadingPermissionControllerSpec
    extends AnyFreeSpec
    with Matchers
    with GuiceOneAppPerSuite
    with OptionValues
    with MockitoSugar
    with ScalaCheckPropertyChecks
    with ViewModelGenerators {

  def onwardRoute: Call = Call("GET", "/foo")

  lazy val unloadingPermissionControllerRoute: String = routes.UnloadingPermissionController.get.url

  "get" - {

    def applicationBuilder: GuiceApplicationBuilder =
      new GuiceApplicationBuilder()
        .configure(Configuration("metrics.enabled" -> "false"))

    "must return OK and PDF" in {

      val mockPDFGenerator: UnloadingPermissionPdfGenerator           = mock[UnloadingPermissionPdfGenerator]
      val mockConversionService: UnloadingPermissionConversionService = mock[UnloadingPermissionConversionService]

      val application = applicationBuilder
        .overrides(
          bind[UnloadingPermissionPdfGenerator].toInstance(mockPDFGenerator),
          bind[UnloadingPermissionConversionService].toInstance(mockConversionService)
        )
        .build()

      running(application) {

        forAll(arbitrary[viewmodels.PermissionToStartUnloading], arbitrary[Array[Byte]]) {
          (permissionToStartUnloadingViewModel, pdf) =>
            when(mockConversionService.toViewModel(any())(any(), any()))
              .thenReturn(Future.successful(Valid(permissionToStartUnloadingViewModel)))

            when(mockPDFGenerator.generate(any()))
              .thenReturn(pdf)

            val request = FakeRequest(POST, unloadingPermissionControllerRoute).withXmlBody(validUnloadingPermissionXml)

            val result = route(application, request).value

            status(result) mustEqual OK
            headers(result).get(CONTENT_TYPE).value mustEqual "application/pdf"
            headers(result).get(CONTENT_DISPOSITION).value mustEqual """attachment; filename="UnloadingPermission_99IT9876AB88901209.pdf""""
        }
      }
    }

    "must return a BadRequest when given an invalid XML" in {

      val application = applicationBuilder.build()

      running(application) {

        val request = FakeRequest(POST, unloadingPermissionControllerRoute).withXmlBody(<invalid></invalid>)

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
      }
    }

    "must return and InternalServerError if the conversion fails" in {

      val mockPDFGenerator: UnloadingPermissionPdfGenerator           = mock[UnloadingPermissionPdfGenerator]
      val mockConversionService: UnloadingPermissionConversionService = mock[UnloadingPermissionConversionService]

      val application = applicationBuilder
        .overrides(
          bind[UnloadingPermissionPdfGenerator].toInstance(mockPDFGenerator),
          bind[UnloadingPermissionConversionService].toInstance(mockConversionService)
        )
        .build()

      running(application) {

        forAll(arbitrary[Array[Byte]]) {
          pdf =>
            when(mockConversionService.toViewModel(any())(any(), any()))
              .thenReturn(Future.successful(Invalid(NonEmptyChain(ReferenceDataRetrievalError("", 500, "")))))

            when(mockPDFGenerator.generate(any()))
              .thenReturn(pdf)

            val request = FakeRequest(POST, unloadingPermissionControllerRoute).withXmlBody(validUnloadingPermissionXml)

            val result = route(application, request).value

            status(result) mustEqual INTERNAL_SERVER_ERROR
        }
      }
    }
  }

  private def validUnloadingPermissionXml =
    <CC043A>
      <SynIdeMES1>UNOC</SynIdeMES1>
      <SynVerNumMES2>3</SynVerNumMES2>
      <MesSenMES3>NTA.GB</MesSenMES3>
      <MesRecMES6>SYST17B-NCTS_EU_EXIT</MesRecMES6>
      <DatOfPreMES9>20200729</DatOfPreMES9>
      <TimOfPreMES10>1058</TimOfPreMES10>
      <IntConRefMES11>66390912144854</IntConRefMES11>
      <AppRefMES14>NCTS</AppRefMES14>
      <TesIndMES18>0</TesIndMES18>
      <MesIdeMES19>66390912144854</MesIdeMES19>
      <MesTypMES20>GB043A</MesTypMES20>
      <HEAHEA>
        <DocNumHEA5>99IT9876AB88901209</DocNumHEA5>
        <TypOfDecHEA24>T1</TypOfDecHEA24>
        <CouOfDesCodHEA30>GB</CouOfDesCodHEA30>
        <CouOfDisCodHEA55>IT</CouOfDisCodHEA55>
        <IdeOfMeaOfTraAtDHEA78>abcd</IdeOfMeaOfTraAtDHEA78>
        <NatOfMeaOfTraAtDHEA80>IT</NatOfMeaOfTraAtDHEA80>
        <ConIndHEA96>0</ConIndHEA96>
        <AccDatHEA158>20190912</AccDatHEA158>
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
      <TRADESTRD>
        <NamTRD7>The Luggage Carriers</NamTRD7>
        <StrAndNumTRD22>225 Suedopolish Yard,</StrAndNumTRD22>
        <PosCodTRD23>SS8 2BB</PosCodTRD23>
        <CitTRD24>,</CitTRD24>
        <CouTRD25>GB</CouTRD25>
        <TINTRD59>GB163910077000</TINTRD59>
      </TRADESTRD>
      <CUSOFFDEPEPT>
        <RefNumEPT1>IT021100</RefNumEPT1>
      </CUSOFFDEPEPT>
      <CUSOFFPREOFFRES>
        <RefNumRES1>GB000060</RefNumRES1>
      </CUSOFFPREOFFRES>
      <SEAINFSLI>
        <SeaNumSLI2>3</SeaNumSLI2>
        <SEAIDSID>
          <SeaIdeSID1>Seals01</SeaIdeSID1>
        </SEAIDSID>
        <SEAIDSID>
          <SeaIdeSID1>Seals02</SeaIdeSID1>
        </SEAIDSID>
        <SEAIDSID>
          <SeaIdeSID1>Seals03</SeaIdeSID1>
        </SEAIDSID>
      </SEAINFSLI>
      <GOOITEGDS>
        <IteNumGDS7>1</IteNumGDS7>
        <GooDesGDS23>Flowers</GooDesGDS23>
        <GroMasGDS46>1000</GroMasGDS46>
        <NetMasGDS48>999</NetMasGDS48>
        <CouOfDisGDS58>GB</CouOfDisGDS58>
        <CouOfDesGDS59>GB</CouOfDesGDS59>
        <PRODOCDC2>
          <DocTypDC21>235</DocTypDC21>
          <DocRefDC23>Ref.</DocRefDC23>
        </PRODOCDC2>
        <CONNR2>
          <ConNumNR21>container 1</ConNumNR21>
        </CONNR2>
        <CONNR2>
          <ConNumNR21>container 2</ConNumNR21>
        </CONNR2>
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
      <GOOITEGDS>
        <IteNumGDS7>1</IteNumGDS7>
        <GooDesGDS23>Flowers</GooDesGDS23>
        <GroMasGDS46>1000</GroMasGDS46>
        <NetMasGDS48>999</NetMasGDS48>
        <CouOfDisGDS58>GB</CouOfDisGDS58>
        <CouOfDesGDS59>GB</CouOfDesGDS59>
        <PRODOCDC2>
          <DocTypDC21>235</DocTypDC21>
          <DocRefDC23>Ref.</DocRefDC23>
        </PRODOCDC2>
        <CONNR2>
          <ConNumNR21>container 1</ConNumNR21>
        </CONNR2>
        <CONNR2>
          <ConNumNR21>container 2</ConNumNR21>
        </CONNR2>
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
    </CC043A>

}
