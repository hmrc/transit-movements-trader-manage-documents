/*
 * Copyright 2023 HM Revenue & Customs
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

package viewmodels

import generators.ViewModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class PackageSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ViewModelGenerators {

  "marksAndNumbersValue" - {

    "BulkPackage must return correct value" - {

      "when value exists" in {

        forAll(arbitrary[BulkPackage], Gen.option(nonEmptyString)) {
          (packageType, string) =>
            val packageNonEmptyString = packageType.copy(marksAndNumbers = string)

            packageNonEmptyString.marksAndNumbersValue mustEqual packageNonEmptyString.marksAndNumbers
        }
      }

      "when string is empty" in {

        forAll(arbitrary[BulkPackage]) {
          packageType =>
            val copyPackageType = packageType.copy(marksAndNumbers = Some(""))

            copyPackageType.marksAndNumbersValue mustEqual None
        }
      }

    }

    "UnpackedPackage must return correct value" - {

      "when value exists" in {

        forAll(arbitrary[UnpackedPackage], Gen.option(nonEmptyString)) {
          (packageType, string) =>
            val packageNonEmptyString = packageType.copy(marksAndNumbers = string)

            packageNonEmptyString.marksAndNumbersValue mustEqual packageNonEmptyString.marksAndNumbers
        }
      }

      "when string is empty" in {

        forAll(arbitrary[UnpackedPackage]) {
          packageType =>
            val copyPackageType = packageType.copy(marksAndNumbers = Some(""))

            copyPackageType.marksAndNumbersValue mustEqual None
        }
      }

    }

    "RegularPackage must return correct value" - {

      "when value exists" in {

        forAll(arbitrary[RegularPackage], nonEmptyString) {
          (packageType, string) =>
            val packageNonEmptyString = packageType.copy(marksAndNumbers = string)

            packageNonEmptyString.marksAndNumbersValue mustEqual Some(packageNonEmptyString.marksAndNumbers)
        }
      }

      "when string is empty" in {

        forAll(arbitrary[RegularPackage]) {
          packageType =>
            val copyPackageType = packageType.copy(marksAndNumbers = "")

            copyPackageType.marksAndNumbersValue mustEqual None
        }
      }
    }
  }

  "packageAndNumber" - {

    "BulkPackage must return correct value" - {

      "when value exists" in {

        forAll(arbitrary[BulkPackage], nonEmptyString) {
          (packageType, string) =>
            val kindOfPackage = packageType.kindOfPackage.copy(description = string)

            val packageNonEmptyString = packageType.copy(kindOfPackage = kindOfPackage)

            packageNonEmptyString.packageAndNumber mustEqual Some(s"1 - ${packageNonEmptyString.kindOfPackage.description}")
        }
      }

      "when string is empty" in {

        forAll(arbitrary[BulkPackage]) {
          packageType =>
            val kindOfPackage = packageType.kindOfPackage.copy(description = "")

            val packageNonEmptyString = packageType.copy(kindOfPackage = kindOfPackage)

            packageNonEmptyString.packageAndNumber mustEqual None
        }
      }
    }

    "UnpackedPackage must return correct value" - {

      "when value exists and number of pieces is > 0" in {

        forAll(arbitrary[UnpackedPackage], nonEmptyString, Gen.choose(1, 20)) {
          (packageType, string, noOfPieces) =>
            val kindOfPackage = packageType.kindOfPackage.copy(description = string)

            val packageNonEmptyString = packageType.copy(kindOfPackage = kindOfPackage, numberOfPieces = noOfPieces)

            packageNonEmptyString.packageAndNumber mustEqual Some(s"$noOfPieces - ${packageNonEmptyString.kindOfPackage.description}")
        }
      }

      "when value exists and number of pieces is 0" in {

        forAll(arbitrary[UnpackedPackage], nonEmptyString) {
          (packageType, string) =>
            val kindOfPackage = packageType.kindOfPackage.copy(description = string)

            val packageNonEmptyString = packageType.copy(kindOfPackage = kindOfPackage, numberOfPieces = 0)

            packageNonEmptyString.packageAndNumber mustEqual None
        }
      }

      "when string is empty" in {

        forAll(arbitrary[UnpackedPackage], Gen.choose(1, 20)) {
          (packageType, noOfPieces) =>
            val kindOfPackage = packageType.kindOfPackage.copy(description = "")

            val packageNonEmptyString = packageType.copy(kindOfPackage = kindOfPackage, numberOfPieces = noOfPieces)

            packageNonEmptyString.packageAndNumber mustEqual None
        }
      }
    }

    "RegularPackage must return correct value" - {

      "when value exists and number of pieces is > 0" in {

        forAll(arbitrary[RegularPackage], nonEmptyString, Gen.choose(1, 20)) {
          (packageType, string, numberOfPackages) =>
            val kindOfPackage = packageType.kindOfPackage.copy(description = string)

            val packageNonEmptyString = packageType.copy(kindOfPackage = kindOfPackage, numberOfPackages = numberOfPackages)

            packageNonEmptyString.packageAndNumber mustEqual Some(s"$numberOfPackages - ${packageNonEmptyString.kindOfPackage.description}")
        }
      }

      "when value exists and number of pieces is 0" in {

        forAll(arbitrary[RegularPackage], nonEmptyString) {
          (packageType, string) =>
            val kindOfPackage = packageType.kindOfPackage.copy(description = string)

            val packageNonEmptyString = packageType.copy(kindOfPackage = kindOfPackage, numberOfPackages = 0)

            packageNonEmptyString.packageAndNumber mustEqual None
        }
      }

      "when string is empty" in {

        forAll(arbitrary[RegularPackage], Gen.choose(1, 20)) {
          (packageType, numberOfPackages) =>
            val kindOfPackage = packageType.kindOfPackage.copy(description = "")

            val packageNonEmptyString = packageType.copy(kindOfPackage = kindOfPackage, numberOfPackages = numberOfPackages)

            packageNonEmptyString.packageAndNumber mustEqual None
        }
      }
    }
  }

  "numberOfPiecesValue" - {

    "RegularPackage must return correct value" - {
      forAll(arbitrary[UnpackedPackage]) {
        packageType =>
          packageType.numberOfPiecesValue mustEqual packageType.numberOfPieces
      }
    }

    "BulkPackage and RegularPackage must return 0" - {
      forAll(Gen.oneOf(arbitrary[BulkPackage], arbitrary[RegularPackage])) {
        packageType =>
          packageType.numberOfPiecesValue mustEqual 0
      }
    }

  }

}
