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

package services

import generators.ViewmodelGenerators
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class XMLToReleaseForTransitSpec
    extends FreeSpec
    with MustMatchers
    with MockitoSugar
    with ScalaFutures
    with OptionValues
    with IntegrationPatience
    with ViewmodelGenerators
    with ScalaCheckPropertyChecks {

  "XMLToTransitAccompanyingDocument" - {

    "must return a ParseSuccess with TransitAccompanyingDocument when given a full XML" in {

      val result = XMLToReleaseForTransit.convert(declarationXmlFull)

      result.isSuccessful mustBe true
    }

    "must return a ParseSuccess with TransitAccompanyingDocument when given a minimum XML" in {

      val result = XMLToReleaseForTransit.convert(declarationXmlMinimum)

      result.isSuccessful mustBe true
    }

    "must return a ParseFailure when given an invalid XML" in {

      val result = XMLToReleaseForTransit.convert(<invalid></invalid>)

      result.isSuccessful mustBe false
    }
  }

  private def declarationXmlFull =
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
        <HITPC126>SOMETHIN'</HITPC126>
      </TRAPRIPC1>
      <TRACONCO1>
        <NamCO17>SOME TEST PLACE</NamCO17>
        <StrAndNumCO122>11TH FLOOR, SOME HOUSE, SOME AV</StrAndNumCO122>
        <PosCodCO123>XX2 2XX</PosCodCO123>
        <CitCO124>SOMEWHERE, COUNTY</CitCO124>
        <CouCO125>GB</CouCO125>
        <TINCO159>SOMETINNUMBER2</TINCO159>
        <NADLNGCO>NADLINGO</NADLNGCO>
      </TRACONCO1>
      <TRACONCE1>
        <NamCE17>ANOTHER PLACE NAME</NamCE17>
        <StrAndNumCE122>SOME COUNTRY</StrAndNumCE122>
        <PosCodCE123>ZZ1 1ZZ</PosCodCE123>
        <CitCE124>SOMEE CITY</CitCE124>
        <CouCE125>IT</CouCE125>
        <TINCE159>SOMETINNUMBER3</TINCE159>
        <NADLNGCE>NADLNGCE</NADLNGCE>
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
          <VALLIMNONECLIM>
            <NotValForOthConPLIM2>A</NotValForOthConPLIM2>
          </VALLIMNONECLIM>
          <VALLIMNONECLIM>
            <NotValForOthConPLIM2>B</NotValForOthConPLIM2>
          </VALLIMNONECLIM>
        </GUAREFREF>
      </GUAGUA>
      <GUAGUA>
        <GuaTypGUA1>2</GuaTypGUA1>
        <GUAREFREF>
          <GuaRefNumGRNREF1>SOME-12345657-REF</GuaRefNumGRNREF1>
          <AccCodREF6>AB123</AccCodREF6>
          <VALLIMECVLE>
            <NotValForECVLE1>1</NotValForECVLE1>
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
        <TRACONCO2>
          <NamCO27>SOME TEST PLACE</NamCO27>
          <StrAndNumCO222>11TH FLOOR, SOME HOUSE, SOME AV</StrAndNumCO222>
          <PosCodCO223>XX2 2XX</PosCodCO223>
          <CitCO224>SOMEWHERE, COUNTY</CitCO224>
          <CouCO225>GB</CouCO225>
          <TINCO259>SOMETINNUMBER2</TINCO259>
          <NADLNGCO>NADLINGO</NADLNGCO>
        </TRACONCO2>
        <TRACONCE2>
          <NamCE27>ANOTHER PLACE NAME</NamCE27>
          <StrAndNumCE222>SOME COUNTRY</StrAndNumCE222>
          <PosCodCE223>ZZ1 1ZZ</PosCodCE223>
          <CitCE224>SOMEE CITY</CitCE224>
          <CouCE225>IT</CouCE225>
          <TINCE259>SOMETINNUMBER3</TINCE259>
          <NADLNGCE>NADLNGCE</NADLNGCE>
        </TRACONCE2>
        <SGICODSD2>
          <SenGooCodSD22>Code1</SenGooCodSD22>
          <SenQuaSD23>12</SenQuaSD23>
        </SGICODSD2>
      </GOOITEGDS>
    </CC029B>

  private def declarationXmlMinimum =
    <CC029B>
      <HEAHEA>
        <DocNumHEA5>MRNVALUE</DocNumHEA5>
        <TypOfDecHEA24>T1</TypOfDecHEA24>
        <NCTRetCopHEA104>0</NCTRetCopHEA104>
        <AccDatHEA158>20201026</AccDatHEA158>
        <TotNumOfIteHEA305>1</TotNumOfIteHEA305>
        <TotGroMasHEA307>1000</TotGroMasHEA307>
        <BinItiHEA246>0</BinItiHEA246>
      </HEAHEA>
      <TRAPRIPC1>
        <NamPC17>SOME TEST PLACE</NamPC17>
        <StrAndNumPC122>11TH FLOOR, SOME HOUSE, SOME AV</StrAndNumPC122>
        <PosCodPC123>XX2 2XX</PosCodPC123>
        <CitPC124>SOMEWHERE, COUNTY</CitPC124>
        <CouPC125>GB</CouPC125>
      </TRAPRIPC1>
      <CUSOFFDEPEPT>
        <RefNumEPT1>GB000001</RefNumEPT1>
      </CUSOFFDEPEPT>
      <CUSOFFDESEST>
        <RefNumEST1>IT000001</RefNumEST1>
      </CUSOFFDESEST>
      <GUAGUA>
        <GuaTypGUA1>1</GuaTypGUA1>
        <GUAREFREF>
          <OthGuaRefREF4>SOME-12345656-REF</OthGuaRefREF4>
        </GUAREFREF>
      </GUAGUA>
      <GOOITEGDS>
        <IteNumGDS7>1</IteNumGDS7>
        <GooDesGDS23>Wheat</GooDesGDS23>
        <PACGS2>
          <MarNumOfPacGS21>AB234</MarNumOfPacGS21>
          <KinOfPacGS23>BX</KinOfPacGS23>
          <NumOfPacGS24>10</NumOfPacGS24>
        </PACGS2>
      </GOOITEGDS>
    </CC029B>

}
