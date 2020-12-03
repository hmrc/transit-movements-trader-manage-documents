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

package models
import com.lucidchart.open.xtract.XmlReader
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import utils.DateFormatter.dateFormatted
import utils.XMLBuilderHelper

import scala.xml.NodeSeq

class TransitAccompanyingDocumentSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "XML" - {

    "must deserialise to TransitAccompanyingDocument" in {
      forAll(arbitrary[TransitAccompanyingDocument]) {
        transitAccompanyingDocument =>
          val xml = {
            <CC029B>
              <HEAHEA>
                <RefNumHEA4>{transitAccompanyingDocument.localReferenceNumber}</RefNumHEA4>
                <TypOfDecHEA24>{transitAccompanyingDocument.declarationType.toString}</TypOfDecHEA24>
                {
                transitAccompanyingDocument.countryOfDispatch.fold(NodeSeq.Empty) { countryOfDispatch =>
                  <CouOfDisCodHEA55>{countryOfDispatch}</CouOfDisCodHEA55>
                } ++
                  transitAccompanyingDocument.countryOfDestination.fold(NodeSeq.Empty) { countryOfDestination =>
                    <CouOfDesCodHEA30>{countryOfDestination}</CouOfDesCodHEA30>
                  }
                }
                {
                transitAccompanyingDocument.transportIdentity.fold(NodeSeq.Empty) { transportIdentity =>
                  <IdeOfMeaOfTraAtDHEA78>{transportIdentity}</IdeOfMeaOfTraAtDHEA78>
                } ++
                  transitAccompanyingDocument.transportCountry.fold(NodeSeq.Empty) { transportCountry =>
                    <NatOfMeaOfTraAtDHEA80>{transportCountry}</NatOfMeaOfTraAtDHEA80>
                  }
                }
                <AccDatHEA158>{dateFormatted(transitAccompanyingDocument.acceptanceDate)}</AccDatHEA158>
                <TotNumOfIteHEA305>{transitAccompanyingDocument.numberOfItems}</TotNumOfIteHEA305>
                <TotNumOfPacHEA306>{transitAccompanyingDocument.numberOfPackages}</TotNumOfPacHEA306>
                <TotGroMasHEA307>{transitAccompanyingDocument.grossMass}</TotGroMasHEA307>
                {
                  transitAccompanyingDocument.authorisationId.fold(NodeSeq.Empty) { authorisationId =>
                    <AutIdHEA380>{authorisationId}</AutIdHEA380>
                  }
                }
              </HEAHEA>
              <TRAPRIPC1>
                <NamPC17>{transitAccompanyingDocument.principal.name}</NamPC17>
                <StrAndNumPC122>{transitAccompanyingDocument.principal.streetAndNumber}</StrAndNumPC122>
                <PosCodPC123>{transitAccompanyingDocument.principal.postCode}</PosCodPC123>
                <CitPC124>{transitAccompanyingDocument.principal.city}</CitPC124>
                <CouPC125>{transitAccompanyingDocument.principal.countryCode}</CouPC125>
                {
                transitAccompanyingDocument.principal.eori.fold(NodeSeq.Empty) { eori =>
                  <TINPC159>{eori}</TINPC159>
                } ++
                  transitAccompanyingDocument.principal.tir.fold(NodeSeq.Empty) { tir =>
                    <HITPC126>{tir}</HITPC126>
                  }
                }
              </TRAPRIPC1>
              {
                transitAccompanyingDocument.consignor.map(XMLBuilderHelper.consignorHeaderXML) ++
                transitAccompanyingDocument.consignee.map(XMLBuilderHelper.consigneeHeaderXML)
              }
              <CUSOFFDEPEPT>
                <RefNumEPT1>{transitAccompanyingDocument.departureOffice}</RefNumEPT1>
              </CUSOFFDEPEPT>
              {
                XMLBuilderHelper.customsOfficeTransitToXml(transitAccompanyingDocument.customsOfficeTransit) ++
                XMLBuilderHelper.controlResult(transitAccompanyingDocument.controlResult) ++
                XMLBuilderHelper.sealsToXml(transitAccompanyingDocument.seals) ++
                transitAccompanyingDocument.goodsItems.toList.map(XMLBuilderHelper.goodsItemToXml)
              }
            </CC029B>
          }

          val result = XmlReader.of[TransitAccompanyingDocument].read(xml).toOption.value

          result mustBe transitAccompanyingDocument
      }
    }

    "must fail to deserialise when given invalid xml" in {

      val xml = <CC029B></CC029B>

      val result = XmlReader.of[TransitAccompanyingDocument].read(xml).toOption

      result mustBe None
    }

  }

}
