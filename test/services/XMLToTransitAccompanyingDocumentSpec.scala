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

package services

import generators.ViewmodelGenerators
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class XMLToTransitAccompanyingDocumentSpec
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

      val result = XMLToTransitAccompanyingDocument.convert(declarationXmlFull)

      result.isSuccessful mustBe true
    }

    "must return a ParseSuccess with TransitAccompanyingDocument when given a minimum XML" in {

      val result = XMLToTransitAccompanyingDocument.convert(declarationXmlMinimum)

      result.isSuccessful mustBe true
    }

    "must return a ParseFailure when given an invalid XML" in {

      val result = XMLToTransitAccompanyingDocument.convert(<invalid></invalid>)

      result.isSuccessful mustBe false
    }
  }

  //TODO: Add additional nodes here
  private def declarationXmlFull =
    <CC015A>
      <HEAHEA>
        <RefNumHEA4>LRNVALUE</RefNumHEA4>
        <TypOfDecHEA24>T2</TypOfDecHEA24>
        <CouOfDisCodHEA55>GB</CouOfDisCodHEA55>
        <CouOfDesCodHEA30>IT</CouOfDesCodHEA30>
        <IdeOfMeaOfTraAtDHEA78>abcd</IdeOfMeaOfTraAtDHEA78>
        <NatOfMeaOfTraAtDHEA80>IT</NatOfMeaOfTraAtDHEA80>
        <AccDatHEA158>20200729</AccDatHEA158>
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
      <SEAINFSLI>
        <SeaNumSLI2>1</SeaNumSLI2>
        <SEAIDSID>
          <SeaIdeSID1>Seal001</SeaIdeSID1>
          <SeaIdeSID1LNG>EN</SeaIdeSID1LNG>
        </SEAIDSID>
      </SEAINFSLI>
      <GOOITEGDS>
        <IteNumGDS7>1</IteNumGDS7>
        <ComCodTarCodGDS10>1</ComCodTarCodGDS10>
        <DecTypGDS15>T2</DecTypGDS15>
        <GooDesGDS23>Flowers</GooDesGDS23>
        <GroMasGDS46>1000</GroMasGDS46>
        <NetMasGDS48>999</NetMasGDS48>
        <CouOfDisGDS58>GB</CouOfDisGDS58>
        <CouOfDesGDS59>GB</CouOfDesGDS59>
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

  private def declarationXmlMinimum =
    <CC015A>
      <HEAHEA>
        <RefNumHEA4>LRNVALUE</RefNumHEA4>
        <TypOfDecHEA24>T2</TypOfDecHEA24>
        <AccDatHEA158>20200729</AccDatHEA158>
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
      <CUSOFFDEPEPT>
        <RefNumEPT1>GB000060</RefNumEPT1>
      </CUSOFFDEPEPT>
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
