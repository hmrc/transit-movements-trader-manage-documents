/*
 * Copyright 2019 HM Revenue & Customs
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
import org.scalatest.{FreeSpec, MustMatchers}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsSuccess, Json}

class PackageSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators {

  "Package" - {

    "must deserialise to a Bulk Package" in {

      forAll(Gen.oneOf(BulkPackage.validCodes)) {
        kindOfPackage =>
          val json = Json.obj("kindOfPackage" -> kindOfPackage)

          val expectedPackage = BulkPackage(kindOfPackage, None)

          json.validate[Package] mustEqual JsSuccess(expectedPackage)
      }
    }

    "must deserialise to an Unpacked Package" in {

      val gen = for {
        kindOfPackage  <- Gen.oneOf(UnpackedPackage.validCodes)
        numberOfPieces <- Gen.choose(1, 99999)
      } yield (kindOfPackage, numberOfPieces)

      forAll(gen) {
        case (kindOfPackage, numberOfPieces) =>
          val json = Json.obj(
            "kindOfPackage"  -> kindOfPackage,
            "numberOfPieces" -> numberOfPieces
          )

          val expectedPackage = UnpackedPackage(kindOfPackage, numberOfPieces, None)

          json.validate[Package] mustEqual JsSuccess(expectedPackage)
      }
    }

    "must deserialise to a Regular Package" in {

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

            json.validate[Package] mustEqual JsSuccess(expectedPackage)
          }
      }
    }

    "must serialise from a Bulk Package" in {

      forAll(Gen.oneOf(BulkPackage.validCodes), stringWithMaxLength(42)) {
        (kindOfPackage, marksAndNumbers) =>
          val json = Json.obj(
            "kindOfPackage"   -> kindOfPackage,
            "marksAndNumbers" -> marksAndNumbers
          )

          Json.toJson(BulkPackage(kindOfPackage, Some(marksAndNumbers)): Package) mustEqual json
      }
    }

    "must serialise from an Unpacked Package" in {

      forAll(Gen.oneOf(UnpackedPackage.validCodes), stringWithMaxLength(42), Gen.choose(1, 99999)) {
        (kindOfPackage, marksAndNumbers, numberOfPieces) =>
          val json = Json.obj(
            "kindOfPackage"   -> kindOfPackage,
            "marksAndNumbers" -> marksAndNumbers,
            "numberOfPieces"  -> numberOfPieces
          )

          Json.toJson(UnpackedPackage(kindOfPackage, numberOfPieces, Some(marksAndNumbers)): Package) mustEqual json
      }
    }

    "must serialise from a Regular Package" in {

      forAll(Gen.oneOf(UnpackedPackage.validCodes), stringWithMaxLength(42), Gen.choose(1, 99999)) {
        (kindOfPackage, marksAndNumbers, numberOfPackages) =>
          val json = Json.obj(
            "kindOfPackage"    -> kindOfPackage,
            "marksAndNumbers"  -> marksAndNumbers,
            "numberOfPackages" -> numberOfPackages
          )

          Json.toJson(RegularPackage(kindOfPackage, numberOfPackages, marksAndNumbers): Package) mustEqual json
      }
    }
  }
}
