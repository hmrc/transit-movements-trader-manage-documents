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
import play.api.libs.json.{JsError, JsSuccess, Json}

class BulkPackageSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ModelGenerators {

  "Bulk Package" - {

    "must deserialise when the kind of package indicates a Bulk package" -  {

      "and marks and numbers are present" in {

        val gen = for {
          kindOfPackage   <- Gen.oneOf(BulkPackage.validCodes)
          marksAndNumbers <- stringWithMaxLength(42)
        } yield (kindOfPackage, marksAndNumbers)

        forAll(gen) {
          case (kindOfPackage, marksAndNumbers) =>

            val json = Json.obj(
              "kindOfPackage"   -> kindOfPackage,
              "marksAndNumbers" -> marksAndNumbers
            )

            val expectedPackage = BulkPackage(kindOfPackage, Some(marksAndNumbers))

            json.validate[BulkPackage] mustEqual JsSuccess(expectedPackage)
        }
      }

      "and marks and numbers are not present" in {

        forAll(Gen.oneOf(BulkPackage.validCodes)) {
          kindOfPackage =>

            val json = Json.obj("kindOfPackage" -> kindOfPackage)

            val expectedPackage = BulkPackage(kindOfPackage, None)

            json.validate[BulkPackage] mustEqual JsSuccess(expectedPackage)
        }
      }
    }

    "must fail to deserialise when the kind of package indicates this is not a Bulk package" in {

      forAll(stringWithMaxLength(3)) {
        kindOfPackage =>

          whenever(!BulkPackage.validCodes.contains(kindOfPackage)) {

            val json = Json.obj("kindOfPackage" -> kindOfPackage)

            json.validate[BulkPackage] mustEqual JsError(
              "kindOfPackage must indicate BULK"
            )
          }
      }
    }
  }
}
