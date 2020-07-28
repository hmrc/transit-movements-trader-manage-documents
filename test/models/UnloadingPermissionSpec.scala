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
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json
import utils.DateFormatter._
import json.NonEmptyListOps._

import scala.xml.NodeSeq

class UnloadingPermissionSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators with OptionValues {

  "Unloading Permission" - {

    "JSON" - {

      "must deserialise to Permission to Start Unloading" in {

        forAll(arbitrary[PermissionToStartUnloading]) {
          permission =>
            val json = Json.obj(
              "movementReferenceNumber" -> permission.movementReferenceNumber,
              "declarationType"         -> Json.toJson(permission.declarationType),
              "transportIdentity"       -> permission.transportIdentity,
              "transportCountry"        -> permission.transportCountry,
              "acceptanceDate"          -> Json.toJson(permission.acceptanceDate),
              "numberOfItems"           -> permission.numberOfItems,
              "numberOfPackages"        -> permission.numberOfPackages,
              "grossMass"               -> permission.grossMass,
              "principal"               -> Json.toJson(permission.principal),
              "traderAtDestination"     -> Json.toJson(permission.traderAtDestination),
              "presentationOffice"      -> permission.presentationOffice,
              "seals"                   -> permission.seals,
              "goodsItems"              -> permission.goodsItems
            )

            json.validate[UnloadingPermission] mustEqual JsSuccess(permission)
        }
      }

      "must deserialise to Permission to Continue Unloading" in {

        forAll(arbitrary[PermissionToContinueUnloading]) {
          permission =>
            val json = Json.obj(
              "movementReferenceNumber" -> permission.movementReferenceNumber,
              "continueUnloading"       -> permission.continueUnloading,
              "presentationOffice"      -> permission.presentationOffice,
              "traderAtDestination"     -> permission.traderAtDestination
            )

            json.validate[UnloadingPermission] mustEqual JsSuccess(permission)
        }
      }

      "must serialise from Permission to Start Unloading" in {

        forAll(arbitrary[PermissionToStartUnloading]) {
          permission =>
            val transportIdentityJson =
              permission.transportIdentity
                .map {
                  id =>
                    Json.obj("transportIdentity" -> id)
                }
                .getOrElse(Json.obj())

            val transportCountryJson =
              permission.transportCountry
                .map {
                  country =>
                    Json.obj("transportCountry" -> country)
                }
                .getOrElse(Json.obj())

            val json = Json.obj(
              "movementReferenceNumber" -> permission.movementReferenceNumber,
              "declarationType"         -> Json.toJson(permission.declarationType),
              "acceptanceDate"          -> Json.toJson(permission.acceptanceDate),
              "numberOfItems"           -> permission.numberOfItems,
              "numberOfPackages"        -> permission.numberOfPackages,
              "grossMass"               -> permission.grossMass,
              "principal"               -> Json.toJson(permission.principal),
              "traderAtDestination"     -> Json.toJson(permission.traderAtDestination),
              "presentationOffice"      -> permission.presentationOffice,
              "seals"                   -> permission.seals,
              "goodsItems"              -> permission.goodsItems
            ) ++ transportIdentityJson ++ transportCountryJson

            Json.toJson(permission: UnloadingPermission) mustEqual json
        }
      }

      "must serialise from Permission to Continue Unloading" in {

        forAll(arbitrary[PermissionToContinueUnloading]) {
          permission =>
            val json = Json.obj(
              "movementReferenceNumber" -> permission.movementReferenceNumber,
              "continueUnloading"       -> permission.continueUnloading,
              "presentationOffice"      -> permission.presentationOffice,
              "traderAtDestination"     -> permission.traderAtDestination
            )

            Json.toJson(permission: UnloadingPermission) mustEqual json
        }
      }
    }

    "XML" - {

      "must deserialise to PermissionToStartUnloading" in {

        forAll(arbitrary[PermissionToStartUnloading]) {
          unloadingPermission =>
            val xml = {
              <CC043A>
            <HEAHEA>
              <DocNumHEA5>{unloadingPermission.movementReferenceNumber}</DocNumHEA5>
              <TypOfDecHEA24>{unloadingPermission.declarationType.toString}</TypOfDecHEA24>
              {
                unloadingPermission.transportIdentity.fold(NodeSeq.Empty) { transportIdentity =>
                  <IdeOfMeaOfTraAtDHEA78>{transportIdentity}</IdeOfMeaOfTraAtDHEA78>
                } ++
                unloadingPermission.transportCountry.fold(NodeSeq.Empty) { transportCountry =>
                  <NatOfMeaOfTraAtDHEA80>{transportCountry}</NatOfMeaOfTraAtDHEA80>
                }
              }
              <AccDatHEA158>{dateFormatted(unloadingPermission.acceptanceDate)}</AccDatHEA158>
              <TotNumOfIteHEA305>{unloadingPermission.numberOfItems}</TotNumOfIteHEA305>
              <TotNumOfPacHEA306>{unloadingPermission.numberOfPackages}</TotNumOfPacHEA306>
              <TotGroMasHEA307>{unloadingPermission.grossMass}</TotGroMasHEA307>
            </HEAHEA>
            <TRAPRIPC1>
              <NamPC17>{unloadingPermission.principal.name}</NamPC17>
              <StrAndNumPC122>{unloadingPermission.principal.streetAndNumber}</StrAndNumPC122>
              <PosCodPC123>{unloadingPermission.principal.postCode}</PosCodPC123>
              <CitPC124>{unloadingPermission.principal.city}</CitPC124>
              <CouPC125>{unloadingPermission.principal.countryCode}</CouPC125>
              {
                unloadingPermission.principal.eori.fold(NodeSeq.Empty) { eori =>
                  <TINPC159>{eori}</TINPC159>
                } ++
                unloadingPermission.principal.tir.fold(NodeSeq.Empty) { tir =>
                  <HITPC126>{tir}</HITPC126>
                }
              }
            </TRAPRIPC1>
            <TRADESTRD>
            {
              traderAtDestinationToXml(unloadingPermission.traderAtDestination)
            }
            </TRADESTRD>
            <CUSOFFPREOFFRES>
              <RefNumRES1>{unloadingPermission.presentationOffice}</RefNumRES1>
            </CUSOFFPREOFFRES>
            {
              sealsToXml(unloadingPermission.seals) ++
              unloadingPermission.goodsItems.toList.map(goodsItemToXml)
            }
            </CC043A>
            }

            val result = XmlReader.of[PermissionToStartUnloading].read(xml).toOption.value

            result mustBe unloadingPermission
        }
      }

      "must fail to deserialise when given invalid xml" in {

        val xml = <CC043A></CC043A>

        val result = XmlReader.of[PermissionToStartUnloading].read(xml).toOption

        result mustBe None
      }
    }
  }

  private def goodsItemToXml(goodsItem: GoodsItem) =
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

  private def traderAtDestinationToXml(traderAtDestination: TraderAtDestination): NodeSeq =
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

  private def sealsToXml(seals: Seq[String]): NodeSeq =
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
