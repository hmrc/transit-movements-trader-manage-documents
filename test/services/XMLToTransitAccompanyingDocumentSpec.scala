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
        <TotNumOfIteHEA305>1</TotNumOfIteHEA305>
        <TotNumOfPacHEA306>1</TotNumOfPacHEA306>
        <TotGroMasHEA307>1000</TotGroMasHEA307>
      </HEAHEA>
    </CC015A>

  private def declarationXmlMinimum =
    <CC015A>
      <HEAHEA>
        <RefNumHEA4>LRNVALUE</RefNumHEA4>
        <TypOfDecHEA24>T2</TypOfDecHEA24>
        <TotNumOfIteHEA305>1</TotNumOfIteHEA305>
        <TotNumOfPacHEA306>1</TotNumOfPacHEA306>
        <TotGroMasHEA307>1000</TotGroMasHEA307>
      </HEAHEA>
    </CC015A>
}
