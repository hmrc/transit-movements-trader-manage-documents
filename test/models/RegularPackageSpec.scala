/*
 * Copyright 2022 HM Revenue & Customs
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
import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json

class RegularPackageSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ModelGenerators {

  "Regular Package" - {

    "must deserialise when the kind of package does not indicate Bulk or Unpacked" in {

      val gen = for {
        kindOfPackage    <- stringWithMaxLength(3)
        numberOfPackages <- Gen.choose(1, 99999)
        marksAndNumbers  <- stringWithMaxLength(42)
      } yield (kindOfPackage, numberOfPackages, marksAndNumbers)

      forAll(gen) {
        case (kindOfPackage, numberOfPackages, marksAndNumbers) =>
          whenever(!BulkPackage.validCodes.contains(kindOfPackage) && !UnpackedPackage.validCodes.contains(kindOfPackage)) {

            val json = Json.obj(
              "kindOfPackage"    -> kindOfPackage,
              "numberOfPackages" -> numberOfPackages,
              "marksAndNumbers"  -> marksAndNumbers
            )

            val expectedPackage = RegularPackage(kindOfPackage, numberOfPackages, marksAndNumbers)

            json.validate[RegularPackage] mustEqual JsSuccess(expectedPackage)
          }
      }
    }

    "must fail to deserialise when the kind of package indicates Bulk or Unpacked" in {

      val gen = for {
        kindOfPackage    <- Gen.oneOf(BulkPackage.validCodes ++ UnpackedPackage.validCodes)
        numberOfPackages <- Gen.choose(1, 99999)
        marksAndNumbers  <- stringWithMaxLength(42)
      } yield (kindOfPackage, numberOfPackages, marksAndNumbers)

      forAll(gen) {
        case (kindOfPackage, numberOfPackages, marksAndNumbers) =>
          val json = Json.obj(
            "kindOfPackage"    -> kindOfPackage,
            "numberOfPackages" -> numberOfPackages,
            "marksAndNumbers"  -> marksAndNumbers
          )

          json.validate[RegularPackage] mustEqual JsError(
            "kindOfPackage must not indicate BULK or UNPACKED"
          )
      }
    }
  }
}
