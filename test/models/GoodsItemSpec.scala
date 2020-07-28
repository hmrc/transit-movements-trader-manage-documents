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
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatest.OptionValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalacheck.Arbitrary.arbitrary

import scala.xml.NodeSeq

class GoodsItemSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "GoodsItem" - {

    "XML" - {

      "must deserialise" in {

        forAll(arbitrary[GoodsItem]) {
          goodsItem =>
            val xml = {
              <GOOITEGDS>
              <IteNumGDS7>{goodsItem.itemNumber}</IteNumGDS7>
              {
                {
                  goodsItem.commodityCode.fold(NodeSeq.Empty) { commodityCode =>
                    <ComCodTarCodGDS10>{commodityCode}</ComCodTarCodGDS10>
                  } ++
                  goodsItem.declarationType.fold(NodeSeq.Empty) { declarationType =>
                    <DecTypGDS15>{declarationType.toString}</DecTypGDS15>
                  }
                }
              }
              <GooDesGDS23>{goodsItem.description}</GooDesGDS23>
              {
                goodsItem.grossMass.fold(NodeSeq.Empty) { grossMass =>
                  <GroMasGDS46>{grossMass}</GroMasGDS46>
                } ++
                goodsItem.netMass.fold(NodeSeq.Empty) { netMass =>
                  <NetMasGDS48>{netMass}</NetMasGDS48>
                }
              }
              <CouOfDisGDS58>{goodsItem.countryOfDispatch}</CouOfDisGDS58>
              <CouOfDesGDS59>{goodsItem.countryOfDestination}</CouOfDesGDS59>
              {
                goodsItem.producedDocuments.map(producedDocumentXML) ++
                goodsItem.specialMentions.map(specialMentionXML) ++
                goodsItem.consignor.map(consignorXML) ++
                goodsItem.consignee.map(consigneeXML) ++
                goodsItem.containers.map {
                  containersNumber =>
                    <CONNR2>
                      <ConNumNR21>{containersNumber}</ConNumNR21>
                    </CONNR2>
                } ++
                goodsItem.packages.toList.map(packageToXML) ++
                goodsItem.sensitiveGoodsInformation.map {
                  sensitiveInformation =>
                  <SGICODSD2>
                    {
                      sensitiveInformation.goodsCode.fold(NodeSeq.Empty) { goodsCode =>
                        <SenGooCodSD22>{goodsCode}</SenGooCodSD22>
                      }
                    }
                    <SenQuaSD23>{sensitiveInformation.quantity}</SenQuaSD23>
                  </SGICODSD2>
                }
              }
            </GOOITEGDS>
            }

            val result = XmlReader.of[GoodsItem].read(xml).toOption.value

            result mustBe goodsItem
        }
      }

      "must fail to deserialise when given invalid xml" in {

        val xml = <GOOITEGDS></GOOITEGDS>

        val result = XmlReader.of[GoodsItem].read(xml).toOption

        result mustBe None
      }
    }
  }

  private def producedDocumentXML(producedDocument: ProducedDocument): NodeSeq =
    <PRODOCDC2>
      <DocTypDC21>{producedDocument.documentType}</DocTypDC21>
      {
      producedDocument.reference.fold(NodeSeq.Empty) { reference =>
        <DocRefDC23>{reference}</DocRefDC23>
      } ++
        producedDocument.complementOfInformation.fold(NodeSeq.Empty) { information =>
          <ComOfInfDC25>{information}</ComOfInfDC25>
        }
      }
    </PRODOCDC2>

  private def specialMentionXML(specialMention: SpecialMention): NodeSeq =
    <SPEMENMT2>
    {
      specialMention match {
        case value: SpecialMentionEc =>
          <ExpFroECMT24>1</ExpFroECMT24>
          <AddInfCodMT23>{value.additionalInformationCoded}</AddInfCodMT23>
        case value: SpecialMentionNonEc =>
          <ExpFroECMT24>0</ExpFroECMT24>
          <AddInfCodMT23>{value.additionalInformationCoded}</AddInfCodMT23>
          <ExpFroCouMT25>{value.exportFromCountry}</ExpFroCouMT25>
        case value: SpecialMentionNoCountry =>
          <AddInfCodMT23>{value.additionalInformationCoded}</AddInfCodMT23>
      }
    }
    </SPEMENMT2>

  private def consignorXML(consignor: Consignor): NodeSeq =
    <TRACONCO2>
      <NamCO17>{consignor.name}</NamCO17>
      <StrAndNumCO122>{consignor.streetAndNumber}</StrAndNumCO122>
      <PosCodCO123>{consignor.postCode}</PosCodCO123>
      <CitCO124>{consignor.city}</CitCO124>
      <CouCO125>{consignor.countryCode}</CouCO125>
      {
      consignor.nadLanguageCode.fold(NodeSeq.Empty) { nadLangCode =>
        <NADLNGCO>{nadLangCode}</NADLNGCO>
      } ++
        consignor.eori.fold(NodeSeq.Empty) { eori =>
          <TINCO159>{eori}</TINCO159>
        }
      }
    </TRACONCO2>

  private def consigneeXML(consignee: Consignee): NodeSeq =
    <TRACONCE2>
      <NamCE17>{consignee.name}</NamCE17>
      <StrAndNumCE122>{consignee.streetAndNumber}</StrAndNumCE122>
      <PosCodCE123>{consignee.postCode}</PosCodCE123>
      <CitCE124>{consignee.city}</CitCE124>
      <CouCE125>{consignee.countryCode}</CouCE125>
      {
      consignee.nadLanguageCode.fold(NodeSeq.Empty) { nadLangCode =>
        <NADLNGCE>{nadLangCode}</NADLNGCE>
      } ++
        consignee.eori.fold(NodeSeq.Empty) { eori =>
          <TINCE159>{eori}</TINCE159>
        }
      }
    </TRACONCE2>

  private def packageToXML(packageModel: Package): NodeSeq =
    packageModel match {
      case value: UnpackedPackage =>
        <PACGS2>
          <KinOfPacGS23>{value.kindOfPackage}</KinOfPacGS23>
          <NumOfPieGS25>{value.numberOfPieces}</NumOfPieGS25>
          {
            value.marksAndNumbers.fold(NodeSeq.Empty) { marksAndNumbers =>
              <MarNumOfPacGS21>{marksAndNumbers}</MarNumOfPacGS21>
            }
          }
        </PACGS2>
      case value: RegularPackage =>
        <PACGS2>
          <KinOfPacGS23>{value.kindOfPackage}</KinOfPacGS23>
          <NumOfPacGS24>{value.numberOfPackages}</NumOfPacGS24>
          <MarNumOfPacGS21>{value.marksAndNumbers}</MarNumOfPacGS21>
        </PACGS2>
      case value: BulkPackage =>
        <PACGS2>
          <KinOfPacGS23>{value.kindOfPackage}</KinOfPacGS23>
          {
            value.marksAndNumbers.fold(NodeSeq.Empty) { marksAndNumbers =>
              <MarNumOfPacGS21>{marksAndNumbers}</MarNumOfPacGS21>
            }
          }
        </PACGS2>
    }

}
