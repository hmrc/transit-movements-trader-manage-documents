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
import org.scalacheck.Gen
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json

class UnpackedPackageSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators {

  "Unpacked package" - {

    "must deserialise when the kind of package indicates an Unpacked package" - {

      "and marks and numbers are present" in {

        val gen = for {
          kindOfPackage   <- Gen.oneOf(UnpackedPackage.validCodes)
          numberOfPieces  <- Gen.choose(1, 99999)
          marksAndNumbers <- stringWithMaxLength(42)
        } yield (kindOfPackage, numberOfPieces, marksAndNumbers)

        forAll(gen) {
          case (kindOfPackage, numberOfPieces, marksAndNumbers) =>
            val json = Json.obj(
              "kindOfPackage"   -> kindOfPackage,
              "numberOfPieces"  -> numberOfPieces,
              "marksAndNumbers" -> marksAndNumbers
            )

            val expectedPackage = UnpackedPackage(kindOfPackage, numberOfPieces, Some(marksAndNumbers))

            json.validate[UnpackedPackage] mustEqual JsSuccess(expectedPackage)
        }
      }

      "and marks and numbers are not present" in {

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

            json.validate[UnpackedPackage] mustEqual JsSuccess(expectedPackage)
        }
      }
    }

    "must fail to deserialise when the kind of package indicates this is not an Unpacked package" in {

      val gen = for {
        kindOfPackage  <- stringWithMaxLength(3)
        numberOfPieces <- Gen.choose(1, 99999)
      } yield (kindOfPackage, numberOfPieces)

      forAll(gen) {
        case (kindOfPackage, numberOfPieces) =>
          whenever(!UnpackedPackage.validCodes.contains(kindOfPackage)) {

            val json = Json.obj(
              "kindOfPackage"  -> kindOfPackage,
              "numberOfPieces" -> numberOfPieces
            )

            json.validate[UnpackedPackage] mustEqual JsError(
              "kindOfPackage must indicate UNPACKED"
            )
          }
      }
    }
  }
}
