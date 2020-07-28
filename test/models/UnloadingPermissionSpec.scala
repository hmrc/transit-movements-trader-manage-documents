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
              XMLBuilderHelper.traderAtDestinationToXml(unloadingPermission.traderAtDestination)
            }
            </TRADESTRD>
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

      "must fail to deserialise when given invalid xml" in {

        val xml = <CC043A></CC043A>

        val result = XmlReader.of[PermissionToStartUnloading].read(xml).toOption

        result mustBe None
      }
    }
  }
}
