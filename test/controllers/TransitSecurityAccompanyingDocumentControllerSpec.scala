/*
 * Copyright 2021 HM Revenue & Customs
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
import services.conversion.TransitSecurityAccompanyingDocumentConversionService
import services.pdf.TADPdfGenerator

import scala.concurrent.Future

class TransitSecurityAccompanyingDocumentControllerSpec
    extends FreeSpec
    with MustMatchers
    with GuiceOneAppPerSuite
    with OptionValues
    with MockitoSugar
    with ScalaCheckPropertyChecks
    with ViewmodelGenerators {

  def onwardRoute: Call = Call("GET", "/foo")

  lazy val controllerRoute: String = routes.TransitSecurityAccompanyingDocumentController.get().url

  "get" - {

    def applicationBuilder: GuiceApplicationBuilder =
      new GuiceApplicationBuilder()
        .configure(Configuration("metrics.enabled" -> "false"))

    "must return OK and PDF" in {

      val mockPDFGenerator: TADPdfGenerator                                           = mock[TADPdfGenerator]
      val mockConversionService: TransitSecurityAccompanyingDocumentConversionService = mock[TransitSecurityAccompanyingDocumentConversionService]

      val application = applicationBuilder
        .overrides {
          bind[TADPdfGenerator].toInstance(mockPDFGenerator)
          bind[TransitSecurityAccompanyingDocumentConversionService].toInstance(mockConversionService)
        }
        .build()

      running(application) {

        forAll(arbitrary[viewmodels.TransitSecurityAccompanyingDocumentPDF], arbitrary[Array[Byte]]) {
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

      val mockPDFGenerator: TADPdfGenerator                                           = mock[TADPdfGenerator]
      val mockConversionService: TransitSecurityAccompanyingDocumentConversionService = mock[TransitSecurityAccompanyingDocumentConversionService]

      val application = applicationBuilder
        .overrides {
          bind[TADPdfGenerator].toInstance(mockPDFGenerator)
          bind[TransitSecurityAccompanyingDocumentConversionService].toInstance(mockConversionService)
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

  private def declarationXml =
    <CC029B>
      <SynIdeMES1>SYNI</SynIdeMES1>
      <SynVerNumMES2>3</SynVerNumMES2>
      <MesSenMES3>ABC.GB</MesSenMES3>
      <MesRecMES6>SOME-ID</MesRecMES6>
      <DatOfPreMES9>20191212</DatOfPreMES9>
      <TimOfPreMES10>201912122344</TimOfPreMES10>
      <IntConRefMES11>Hello</IntConRefMES11>
      <AppRefMES14>AppName</AppRefMES14>
      <TesIndMES18>0</TesIndMES18>
      <MesIdeMES19>1234123412341234</MesIdeMES19>
      <MesTypMES20>GB029B</MesTypMES20>
      <HEAHEA>
        <RefNumHEA4>LRNVALUE</RefNumHEA4>
        <DocNumHEA5>MRNVALUE</DocNumHEA5>
        <TypOfDecHEA24>T1</TypOfDecHEA24>
        <CouOfDesCodHEA30>IT</CouOfDesCodHEA30>
        <CouOfDisCodHEA55>GB</CouOfDisCodHEA55>
        <IdeOfMeaOfTraAtDHEA78>XX11 1XX</IdeOfMeaOfTraAtDHEA78>
        <NatOfMeaOfTraAtDHEA80>GB</NatOfMeaOfTraAtDHEA80>
        <IdeOfMeaOfTraCroHEA85>XX11 1XX</IdeOfMeaOfTraCroHEA85>
        <NatOfMeaOfTraCroHEA87>GB</NatOfMeaOfTraCroHEA87>
        <ConIndHEA96>0</ConIndHEA96>
        <NCTRetCopHEA104>0</NCTRetCopHEA104>
        <AccDatHEA158>20201026</AccDatHEA158>
        <IssDatHEA186>20201026</IssDatHEA186>
        <DiaLanIndAtDepHEA254>EN</DiaLanIndAtDepHEA254>
        <NCTSAccDocHEA601LNG>EN</NCTSAccDocHEA601LNG>
        <TotNumOfIteHEA305>1</TotNumOfIteHEA305>
        <TotNumOfPacHEA306>10</TotNumOfPacHEA306>
        <TotGroMasHEA307>1000</TotGroMasHEA307>
        <BinItiHEA246>0</BinItiHEA246>
        <AutIdHEA380>SOME-AUTH-ID</AutIdHEA380>
        <DecDatHEA383>20201026</DecDatHEA383>
        <DecPlaHEA394>SOMEPLACE</DecPlaHEA394>
      </HEAHEA>
      <TRAPRIPC1>
        <NamPC17>SOME TEST PLACE</NamPC17>
        <StrAndNumPC122>11TH FLOOR, SOME HOUSE, SOME AV</StrAndNumPC122>
        <PosCodPC123>XX2 2XX</PosCodPC123>
        <CitPC124>SOMEWHERE, COUNTY</CitPC124>
        <CouPC125>GB</CouPC125>
        <TINPC159>SOMETINNUMBER</TINPC159>
      </TRAPRIPC1>
      <TRACONCO1>
        <NamCO17>SOME TEST PLACE</NamCO17>
        <StrAndNumCO122>11TH FLOOR, SOME HOUSE, SOME AV</StrAndNumCO122>
        <PosCodCO123>XX2 2XX</PosCodCO123>
        <CitCO124>SOMEWHERE, COUNTY</CitCO124>
        <CouCO125>GB</CouCO125>
        <TINCO159>SOMETINNUMBER2</TINCO159>
      </TRACONCO1>
      <TRACONCE1>
        <NamCE17>ANOTHER PLACE NAME</NamCE17>
        <StrAndNumCE122>SOME COUNTRY</StrAndNumCE122>
        <PosCodCE123>ZZ1 1ZZ</PosCodCE123>
        <CitCE124>SOMEE CITY</CitCE124>
        <CouCE125>IT</CouCE125>
        <TINCE159>SOMETINNUMBER3</TINCE159>
      </TRACONCE1>
      <CUSOFFDEPEPT>
        <RefNumEPT1>GB000001</RefNumEPT1>
      </CUSOFFDEPEPT>
      <CUSOFFTRARNS>
        <RefNumRNS1>FR000001</RefNumRNS1>
        <ArrTimTRACUS085>202010281445</ArrTimTRACUS085>
      </CUSOFFTRARNS>
      <CUSOFFDESEST>
        <RefNumEST1>IT000001</RefNumEST1>
      </CUSOFFDESEST>
      <CUSOFFRETCOPOCP>
        <RefNumOCP1>GB000001</RefNumOCP1>
        <CusOffNamOCP2>SOME OFFICE NAME</CusOffNamOCP2>
        <StrAndNumOCP3>SOME OFFICE ADDRESS</StrAndNumOCP3>
        <CouOCP4>GB</CouOCP4>
        <PosCodOCP6>ZZ2 2ZZ</PosCodOCP6>
        <CitOCP7>SOME CITY</CitOCP7>
      </CUSOFFRETCOPOCP>
      <CONRESERS>
        <ConDatERS14>20201026</ConDatERS14>
        <ConResCodERS16>A3</ConResCodERS16>
        <ConByERS18>Not Controlled</ConByERS18>
        <DatLimERS69>20201103</DatLimERS69>
      </CONRESERS>
      <SEAINFSLI>
        <SeaNumSLI2>1</SeaNumSLI2>
        <SEAIDSID>
          <SeaIdeSID1>ABC0001</SeaIdeSID1>
        </SEAIDSID>
      </SEAINFSLI>
      <GUAGUA>
        <GuaTypGUA1>1</GuaTypGUA1>
        <GUAREFREF>
          <GuaRefNumGRNREF1>SOME-12345656-REF</GuaRefNumGRNREF1>
          <AccCodREF6>AB123</AccCodREF6>
          <VALLIMECVLE>
            <NotValForECVLE1>0</NotValForECVLE1>
          </VALLIMECVLE>
        </GUAREFREF>
      </GUAGUA>
      <GOOITEGDS>
        <IteNumGDS7>1</IteNumGDS7>
        <ComCodTarCodGDS10>ComodityCode</ComCodTarCodGDS10>
        <DecTypGDS15>T1</DecTypGDS15>
        <GooDesGDS23>Wheat</GooDesGDS23>
        <GroMasGDS46>1000</GroMasGDS46>
        <NetMasGDS48>950</NetMasGDS48>
        <CouOfDisGDS58>GB</CouOfDisGDS58>
        <CouOfDesGDS59>GB</CouOfDesGDS59>
        <PREADMREFAR2>
          <PreDocTypAR21>01122</PreDocTypAR21>
          <PreDocRefAR26>ABABA</PreDocRefAR26>
          <ComOfInfAR29>Compliment</ComOfInfAR29>
        </PREADMREFAR2>
        <PRODOCDC2>
          <DocTypDC21>235</DocTypDC21>
          <DocRefDC23>Ref.</DocRefDC23>
        </PRODOCDC2>
        <SPEMENMT2>
          <AddInfMT21>8731.8GBPSOMETHING</AddInfMT21>
          <AddInfCodMT23>CAL</AddInfCodMT23>
        </SPEMENMT2>
        <CONNR2>
          <ConNumNR21>Container-1</ConNumNR21>
        </CONNR2>
        <CONNR2>
          <ConNumNR21>Container-2</ConNumNR21>
        </CONNR2>
        <PACGS2>
          <MarNumOfPacGS21>AB234</MarNumOfPacGS21>
          <KinOfPacGS23>BX</KinOfPacGS23>
          <NumOfPacGS24>10</NumOfPacGS24>
        </PACGS2>
        <SGICODSD2>
          <SenGooCodSD22>Code1</SenGooCodSD22>
          <SenQuaSD23>12</SenQuaSD23>
        </SGICODSD2>
      </GOOITEGDS>
    </CC029B>

}
