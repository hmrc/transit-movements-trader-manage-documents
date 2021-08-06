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

package utils

import models.BulkPackage
import models.Consignee
import models.Consignor
import models.GoodsItem
import models.Package
import models.ProducedDocument
import models.RegularPackage
import models.SecurityConsignee
import models.SecurityConsigneeWithEori
import models.SecurityConsigneeWithoutEori
import models.SecurityConsignor
import models.SecurityConsignorWithEori
import models.SecurityConsignorWithoutEori
import models.SpecialMention
import models.TraderAtDestination
import models.TraderAtDestinationWithEori
import models.TraderAtDestinationWithoutEori
import models.UnpackedPackage

import scala.xml.NodeSeq

object XMLBuilderHelper {

  def goodsItemToXml(goodsItem: GoodsItem) =
    <GOOITEGDS>
      <IteNumGDS7>{goodsItem.itemNumber}</IteNumGDS7>
      {
      {
        goodsItem.commodityCode.fold(NodeSeq.Empty) {
          commodityCode =>
            <ComCodTarCodGDS10>{commodityCode}</ComCodTarCodGDS10>
        } ++
          goodsItem.declarationType.fold(NodeSeq.Empty) {
            declarationType =>
              <DecTypGDS15>{declarationType.toString}</DecTypGDS15>
          }
      }
    }
      <GooDesGDS23>{goodsItem.description}</GooDesGDS23>
      {
      goodsItem.grossMass.fold(NodeSeq.Empty) {
        grossMass =>
          <GroMasGDS46>{grossMass}</GroMasGDS46>
      } ++
        goodsItem.netMass.fold(NodeSeq.Empty) {
          netMass =>
            <NetMasGDS48>{netMass}</NetMasGDS48>
        }
    }
      {
      goodsItem.countryOfDispatch.fold(NodeSeq.Empty) {
        countryOfDispatch =>
          <CouOfDisGDS58>{countryOfDispatch}</CouOfDisGDS58>
      }
    }
      {
      goodsItem.countryOfDestination.fold(NodeSeq.Empty) {
        countryOfDestination =>
          <CouOfDesGDS59>{countryOfDestination}</CouOfDesGDS59>
      }
    }
      {
      goodsItem.methodOfPayment.fold(NodeSeq.Empty) {
        methodOfPayment =>
          <MetOfPayGDI12>{methodOfPayment}</MetOfPayGDI12>
      }
    }
      {
      goodsItem.commercialReferenceNumber.fold(NodeSeq.Empty) {
        commercialReferenceNumber =>
          <ComRefNumGIM1>{commercialReferenceNumber}</ComRefNumGIM1>
      }
    }
      {
      goodsItem.unDangerGoodsCode.fold(NodeSeq.Empty) {
        unDangerGoodsCode =>
          <UNDanGooCodGDI1>{unDangerGoodsCode}</UNDanGooCodGDI1>
      }
    }
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
              sensitiveInformation.goodsCode.fold(NodeSeq.Empty) {
                goodsCode =>
                  <SenGooCodSD22>{goodsCode}</SenGooCodSD22>
              }
            }
              <SenQuaSD23>{sensitiveInformation.quantity}</SenQuaSD23>
            </SGICODSD2>
        } ++
        goodsItem.previousAdminRef.map(
          prevAdminRef =>
            <PREADMREFAR2>
              <PreDocTypAR21>{prevAdminRef.documentType}</PreDocTypAR21>
              <PreDocRefAR26>{prevAdminRef.documentReference}</PreDocRefAR26>
              {
              prevAdminRef.complimentOfInfo.fold(NodeSeq.Empty) {
                compliment =>
                  <ComOfInfAR29>{compliment}</ComOfInfAR29>
              }
            }
            </PREADMREFAR2>
        ) ++
        goodsItem.securityConsignor.map(securityConsignorXML) ++
        goodsItem.securityConsignee.map(securityConsigneeXML)
    }
    </GOOITEGDS>

  def traderAtDestinationToXml(traderAtDestination: TraderAtDestination): NodeSeq =
    traderAtDestination match {
      case value: TraderAtDestinationWithEori =>
        <TINTRD59>{value.eori}</TINTRD59> ++ {
          value.name.fold(NodeSeq.Empty) {
            name =>
              <NamTRD7>{name}</NamTRD7>
          } ++
            value.streetAndNumber.fold(NodeSeq.Empty) {
              streetAndNumber =>
                <StrAndNumTRD22>{streetAndNumber}</StrAndNumTRD22>
            } ++
            value.postCode.fold(NodeSeq.Empty) {
              postcode =>
                <PosCodTRD23>{postcode}</PosCodTRD23>
            } ++
            value.city.fold(NodeSeq.Empty) {
              city =>
                <CitTRD24>{city}</CitTRD24>
            } ++
            value.countryCode.fold(NodeSeq.Empty) {
              countryCode =>
                <CouTRD25>{countryCode}</CouTRD25>
            }
        }
      case value: TraderAtDestinationWithoutEori =>
        <NamTRD7>{value.name}</NamTRD7>
          <StrAndNumTRD22>{value.streetAndNumber}</StrAndNumTRD22>
          <PosCodTRD23>{value.postCode}</PosCodTRD23>
          <CitTRD24>{value.city}</CitTRD24>
          <CouTRD25>{value.countryCode}</CouTRD25>
    }

  def sealsToXml(seals: Seq[String]): NodeSeq =
    seals match {
      case seals: Seq[String] if seals.nonEmpty =>
        <SEAINFSLI>
          {
          seals.map {
            sealId =>
              <SEAIDSID>
                <SeaIdeSID1>{sealId}</SeaIdeSID1>
              </SEAIDSID>
          }
        }
        </SEAINFSLI>
      case _ => NodeSeq.Empty
    }

  def producedDocumentXML(producedDocument: ProducedDocument): NodeSeq =
    <PRODOCDC2>
      <DocTypDC21>{producedDocument.documentType}</DocTypDC21>
      {
      producedDocument.reference.fold(NodeSeq.Empty) {
        reference =>
          <DocRefDC23>{reference}</DocRefDC23>
      } ++
        producedDocument.complementOfInformation.fold(NodeSeq.Empty) {
          information =>
            <ComOfInfDC25>{information}</ComOfInfDC25>
        }
    }
    </PRODOCDC2>

  def specialMentionXML(specialMention: SpecialMention): NodeSeq =
    <SPEMENMT2>
      {
      specialMention.additionalInformation.fold(NodeSeq.Empty) {
        ai =>
          <AddInfMT21>{ai}</AddInfMT21>
      } ++
        NodeSeq.fromSeq(Seq(<AddInfCodMT23>{specialMention.additionalInformationCoded}</AddInfCodMT23>)) ++
        specialMention.exportFromEC.fold(NodeSeq.Empty) {
          efec =>
            <ExpFroECMT24>{if (efec) 1 else 0}</ExpFroECMT24>
        } ++
        specialMention.exportFromCountry.fold(NodeSeq.Empty) {
          efc =>
            <ExpFroCouMT25>{efc}</ExpFroCouMT25>
        }
    }
    </SPEMENMT2>

  def consignorXML(consignor: Consignor): NodeSeq =
    <TRACONCO2>
    <NamCO27>{consignor.name}</NamCO27>
    <StrAndNumCO222>{consignor.streetAndNumber}</StrAndNumCO222>
    <PosCodCO223>{consignor.postCode}</PosCodCO223>
    <CitCO224>{consignor.city}</CitCO224>
    <CouCO225>{consignor.countryCode}</CouCO225>
    {
      consignor.nadLanguageCode.fold(NodeSeq.Empty) {
        nadLangCode =>
          <NADLNGGTCO>{nadLangCode}</NADLNGGTCO>
      } ++
        consignor.eori.fold(NodeSeq.Empty) {
          eori =>
            <TINCO259>{eori}</TINCO259>
        }
    }
  </TRACONCO2>

  def consigneeXML(consignee: Consignee): NodeSeq =
    <TRACONCE2>
      <NamCE27>{consignee.name}</NamCE27>
      <StrAndNumCE222>{consignee.streetAndNumber}</StrAndNumCE222>
      <PosCodCE223>{consignee.postCode}</PosCodCE223>
      <CitCE224>{consignee.city}</CitCE224>
      <CouCE225>{consignee.countryCode}</CouCE225>
      {
      consignee.nadLanguageCode.fold(NodeSeq.Empty) {
        nadLangCode =>
          <NADLNGGICE>{nadLangCode}</NADLNGGICE>
      } ++
        consignee.eori.fold(NodeSeq.Empty) {
          eori =>
            <TINCE259>{eori}</TINCE259>
        }
    }
    </TRACONCE2>

  def consignorHeaderXML(consignor: Consignor): NodeSeq =
    <TRACONCO1>
      <NamCO17>{consignor.name}</NamCO17>
      <StrAndNumCO122>{consignor.streetAndNumber}</StrAndNumCO122>
      <PosCodCO123>{consignor.postCode}</PosCodCO123>
      <CitCO124>{consignor.city}</CitCO124>
      <CouCO125>{consignor.countryCode}</CouCO125>
      {
      consignor.nadLanguageCode.fold(NodeSeq.Empty) {
        nadLangCode =>
          <NADLNGCO>{nadLangCode}</NADLNGCO>
      } ++
        consignor.eori.fold(NodeSeq.Empty) {
          eori =>
            <TINCO159>{eori}</TINCO159>
        }
    }
    </TRACONCO1>

  def consigneeHeaderXML(consignee: Consignee): NodeSeq =
    <TRACONCE1>
      <NamCE17>{consignee.name}</NamCE17>
      <StrAndNumCE122>{consignee.streetAndNumber}</StrAndNumCE122>
      <PosCodCE123>{consignee.postCode}</PosCodCE123>
      <CitCE124>{consignee.city}</CitCE124>
      <CouCE125>{consignee.countryCode}</CouCE125>
      {
      consignee.nadLanguageCode.fold(NodeSeq.Empty) {
        nadLangCode =>
          <NADLNGCE>{nadLangCode}</NADLNGCE>
      } ++
        consignee.eori.fold(NodeSeq.Empty) {
          eori =>
            <TINCE159>{eori}</TINCE159>
        }
    }
    </TRACONCE1>

  def securityConsignorXML(consignor: SecurityConsignor): NodeSeq =
    consignor match {
      case SecurityConsignorWithEori(eori) => <TRACORSECGOO021><TINTRACORSECGOO028>{eori}</TINTRACORSECGOO028></TRACORSECGOO021>
      case SecurityConsignorWithoutEori(name, streetAndNumber, postCode, city, countryCode) =>
        <TRACORSECGOO021>
          <NamTRACORSECGOO025>{name}</NamTRACORSECGOO025>
          <StrNumTRACORSECGOO027>{streetAndNumber}</StrNumTRACORSECGOO027>
          <PosCodTRACORSECGOO026>{postCode}</PosCodTRACORSECGOO026>
          <CitTRACORSECGOO022>{city}</CitTRACORSECGOO022>
          <CouCodTRACORSECGOO023>{countryCode}</CouCodTRACORSECGOO023>
        </TRACORSECGOO021>
    }

  def securityConsigneeXML(consignor: SecurityConsignee): NodeSeq =
    consignor match {
      case SecurityConsigneeWithEori(eori) => <TRACONSECGOO013><TINTRACONSECGOO020>{eori}</TINTRACONSECGOO020></TRACONSECGOO013>
      case SecurityConsigneeWithoutEori(name, streetAndNumber, postCode, city, countryCode) =>
        <TRACONSECGOO013>
          <NamTRACONSECGOO017>{name}</NamTRACONSECGOO017>
          <StrNumTRACONSECGOO019>{streetAndNumber}</StrNumTRACONSECGOO019>
          <PosCodTRACONSECGOO018>{postCode}</PosCodTRACONSECGOO018>
          <CityTRACONSECGOO014>{city}</CityTRACONSECGOO014>
          <CouCodTRACONSECGOO015>{countryCode}</CouCodTRACONSECGOO015>
        </TRACONSECGOO013>
    }

  def packageToXML(packageModel: Package): NodeSeq =
    packageModel match {
      case value: UnpackedPackage =>
        <PACGS2>
          <KinOfPacGS23>{value.kindOfPackage}</KinOfPacGS23>
          <NumOfPieGS25>{value.numberOfPieces}</NumOfPieGS25>
            {
          value.marksAndNumbers.fold(NodeSeq.Empty) {
            marksAndNumbers =>
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
          value.marksAndNumbers.fold(NodeSeq.Empty) {
            marksAndNumbers =>
              <MarNumOfPacGS21>{marksAndNumbers}</MarNumOfPacGS21>
          }
        }
        </PACGS2>
    }

}
