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
import utils.XMLBuilderHelper

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
              "countryOfDispatch"       -> permission.countryOfDispatch,
              "countryOfDestination"    -> permission.countryOfDestination,
              "transportIdentity"       -> permission.transportIdentity,
              "transportCountry"        -> permission.transportCountry,
              "acceptanceDate"          -> Json.toJson(permission.acceptanceDate),
              "numberOfItems"           -> permission.numberOfItems,
              "numberOfPackages"        -> permission.numberOfPackages,
              "grossMass"               -> permission.grossMass,
              "principal"               -> Json.toJson(permission.principal),
              "consignor"               -> Json.toJson(permission.consignor),
              "consignee"               -> Json.toJson(permission.consignee),
              "traderAtDestination"     -> Json.toJson(permission.traderAtDestination),
              "departureOffice"         -> permission.departureOffice,
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

            val consignor = permission.consignor
              .map {
                x =>
                  Json.obj("consignor" -> x)
              }
              .getOrElse(Json.obj())

            val consignee = permission.consignee
              .map {
                x =>
                  Json.obj("consignee" -> x)
              }
              .getOrElse(Json.obj())

            val countryOfDispatchJson =
              permission.countryOfDispatch
                .map {
                  country =>
                    Json.obj("countryOfDispatch" -> country)
                }
                .getOrElse(Json.obj())

            val countryOfDestination =
              permission.countryOfDestination
                .map {
                  country =>
                    Json.obj("countryOfDestination" -> country)
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
              "departureOffice"         -> permission.departureOffice,
              "presentationOffice"      -> permission.presentationOffice,
              "seals"                   -> permission.seals,
              "goodsItems"              -> permission.goodsItems
            ) ++ countryOfDispatchJson ++ countryOfDestination ++ transportIdentityJson ++ transportCountryJson ++ consignor ++ consignee

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
                unloadingPermission.countryOfDispatch.fold(NodeSeq.Empty) { countryOfDispatch =>
                  <CouOfDisCodHEA55>{countryOfDispatch}</CouOfDisCodHEA55>
                } ++
                unloadingPermission.countryOfDestination.fold(NodeSeq.Empty) { countryOfDestination =>
                  <CouOfDesCodHEA30>{countryOfDestination}</CouOfDesCodHEA30>
                }
              }
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
            {
              unloadingPermission.consignor.map(XMLBuilderHelper.consignorHeaderXML) ++
              unloadingPermission.consignee.map(XMLBuilderHelper.consigneeHeaderXML)
            }
            <TRADESTRD>
            {
              XMLBuilderHelper.traderAtDestinationToXml(unloadingPermission.traderAtDestination)
            }
            </TRADESTRD>
            <CUSOFFDEPEPT>
              <RefNumEPT1>{unloadingPermission.departureOffice}</RefNumEPT1>
            </CUSOFFDEPEPT>
            <CUSOFFPREOFFRES>
              <RefNumRES1>{unloadingPermission.presentationOffice}</RefNumRES1>
            </CUSOFFPREOFFRES>
            {
              XMLBuilderHelper.sealsToXml(unloadingPermission.seals) ++
              unloadingPermission.goodsItems.toList.map(XMLBuilderHelper.goodsItemToXml)
            }
            </CC043A>
            }

            val result = XmlReader.of[PermissionToStartUnloading].read(xml).toOption.value

            result mustBe unloadingPermission
        }
      }

      //PermissionToStartUnloading(Ux5zsqr,T-,None,None,Some(H),Some(pp),1933-03-13,76204,6896761,37098012.6072487,Principal(jeonJdlpdon,bptbQ7a4xvtHoj8cqvzBnpxbv,gjon,hgfjquitranjhu8ahnxyyehxgx,i,Some(tzjymhlWrVmjc9),Some(xCJm0bbyt3Hba0)),None,Some(Consignee(jp,hrrzpw3PimbyW6tI,p,laiRrbwgm4r6Aociu,lc,Some(0d),Some(hAopstv6m))),TraderAtDestinationWithoutEori(huebAlQveq,gqbrmodilskvigqutG18Sg3ytYd6qb,7iQPog,foutodghvUkfruiilltgt0izt7swm,Ad),bers,jhhgja,List(7fpvq),NonEmptyList(GoodsItem(87443,Some(sxft),Some(T2),qemo4szcgyfoxnfwiw0Hopextb3hunfso2jl3nnly27zduIxjtywcf4MiYge7Dhxh62elrdwfmjnjd2nyqwytkuweIedhgk4jqthqnw,Some(76775334.25734319),Some(58572839.02111307),Some(s),Some(p),List(),List(),List(),Some(Consignor(sldjfkkakfirvqs1tib9jnbqho7g,sdksrjlAn5qf,s,Xnlxubzyd6prKDirsv1sjaymjpdyrp,mq,Some(t),None)),Some(Consignee(okTlfo3efkliqvwmbdrteJw,byutrpcsgc,Krmtsf,vknlJV,bx,Some(0v),None)),List(),NonEmptyList(BulkPackage(VO,Some(t7wfppnBixbijt)), UnpackedPackage(NE,51672,Some(jzupjwlvuaagzvvkkl1))),Vector()), GoodsItem(98008,Some(kmidf2hjiyh6rYUfR),Some(T-),mKxcvqqGje2mrouW9vgzfyucsilm7zxfstjhythzifn4l6cxvnra0hUh8l0w8arei6bst6qmnPiuimynlfcsicigdchlsiayqbusikxqwzhee4zi91uutbbrikllurrgwaaiwdvaw1rxwnvjtepajxcvcy1KiwibrdwmtvAmtnqrenuNHbynkyiwqbhquizyxjrcyZYewceley0bdrWVvfPkfqk2mg5lLxpxuPdqghvdfycctxmlzemijnqavem4oxx5U8b07yi,Some(68432437.99416454),Some(44246026.565262996),None,Some(Ph),List(),List(),List(SpecialMentionEc(DG1), SpecialMentionNoCountry(xpas)),Some(Consignor(f,ur6j,mo,nvskhntfku49dsgos,ko,Some(xt),Some(nmpohrisbzzkkcxcm))),Some(Consignee(ugdcpwhojz19t8psrnipf9zobomzm,ltkcJ6wrcrujPh3rlj9fvJhbyuae7z,f,Lvabbvlb3hnhfdhdsnPrhDicatntmgr,pp,Some(v),Some(dbqsGaZicnwimgr))),List(qgts5K),NonEmptyList(RegularPackage(v,72828,4j), BulkPackage(VG,Some(sFxpn6g))),Vector(SensitiveGoodsInformation(Some(t1),37504), SensitiveGoodsInformation(Some(sa),94577)))))
      //PermissionToStartUnloading(Ux5zsqr,T-,None,None,Some(H),Some(pp),1933-03-13,76204,6896761,37098012.6072487,Principal(jeonJdlpdon,bptbQ7a4xvtHoj8cqvzBnpxbv,gjon,hgfjquitranjhu8ahnxyyehxgx,i,Some(tzjymhlWrVmjc9),Some(xCJm0bbyt3Hba0)),None,Some(Consignee(jp,hrrzpw3PimbyW6tI,p,laiRrbwgm4r6Aociu,lc,Some(0d),Some(hAopstv6m))),TraderAtDestinationWithoutEori(huebAlQveq,gqbrmodilskvigqutG18Sg3ytYd6qb,7iQPog,foutodghvUkfruiilltgt0izt7swm,Ad),bers,jhhgja,List(7fpvq),NonEmptyList(GoodsItem(87443,Some(sxft),Some(T2),qemo4szcgyfoxnfwiw0Hopextb3hunfso2jl3nnly27zduIxjtywcf4MiYge7Dhxh62elrdwfmjnjd2nyqwytkuweIedhgk4jqthqnw,Some(76775334.25734319),Some(58572839.02111307),Some(s),Some(p),List(PreviousAdministrativeReference(l,jzg5xiffetjlRqmoeu4pltepulgdb33,Some(vzc8120qyfinezyj)), PreviousAdministrativeReference(Ho,srplrmeMixow,None)),List(),List(),Some(Consignor(sldjfkkakfirvqs1tib9jnbqho7g,sdksrjlAn5qf,s,Xnlxubzyd6prKDirsv1sjaymjpdyrp,mq,Some(t),None)),Some(Consignee(okTlfo3efkliqvwmbdrteJw,byutrpcsgc,Krmtsf,vknlJV,bx,Some(0v),None)),List(),NonEmptyList(BulkPackage(VO,Some(t7wfppnBixbijt)), UnpackedPackage(NE,51672,Some(jzupjwlvuaagzvvkkl1))),List()), GoodsItem(98008,Some(kmidf2hjiyh6rYUfR),Some(T-),mKxcvqqGje2mrouW9vgzfyucsilm7zxfstjhythzifn4l6cxvnra0hUh8l0w8arei6bst6qmnPiuimynlfcsicigdchlsiayqbusikxqwzhee4zi91uutbbrikllurrgwaaiwdvaw1rxwnvjtepajxcvcy1KiwibrdwmtvAmtnqrenuNHbynkyiwqbhquizyxjrcyZYewceley0bdrWVvfPkfqk2mg5lLxpxuPdqghvdfycctxmlzemijnqavem4oxx5U8b07yi,Some(68432437.99416454),Some(44246026.565262996),None,Some(Ph),List(PreviousAdministrativeReference(j,u0noeerVNjsLetz,Some(aszg1)), PreviousAdministrativeReference(q,mn9,Some(pmwkqxxQhgdrsv))),List(),List(SpecialMentionEc(DG1), SpecialMentionNoCountry(xpas)),Some(Consignor(f,ur6j,mo,nvskhntfku49dsgos,ko,Some(xt),Some(nmpohrisbzzkkcxcm))),Some(Consignee(ugdcpwhojz19t8psrnipf9zobomzm,ltkcJ6wrcrujPh3rlj9fvJhbyuae7z,f,Lvabbvlb3hnhfdhdsnPrhDicatntmgr,pp,Some(v),Some(dbqsGaZicnwimgr))),List(qgts5K),NonEmptyList(RegularPackage(v,72828,4j), BulkPackage(VG,Some(sFxpn6g))),List(SensitiveGoodsInformation(Some(t1),37504), SensitiveGoodsInformation(Some(sa),94577)))))

      "must fail to deserialise when given invalid xml" in {

        val xml = <CC043A></CC043A>

        val result = XmlReader.of[PermissionToStartUnloading].read(xml).toOption

        result mustBe None
      }
    }
  }
}
