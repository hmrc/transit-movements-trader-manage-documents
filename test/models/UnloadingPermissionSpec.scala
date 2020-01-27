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

import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json
import json.NonEmptyListOps._

class UnloadingPermissionSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators {

  "Unloading Permission" - {

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
}
