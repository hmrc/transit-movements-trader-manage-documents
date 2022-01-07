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

package services

import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.reference.KindOfPackage
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class PackageConverterSpec extends AnyFreeSpec with Matchers with ValidatedMatchers with ValidatedValues {

  private val kindsOfPackage = Seq(KindOfPackage("a", "package 1"), KindOfPackage("b", "package 2"))

  "toViewModel" - {

    "when give a bulk package" - {

      "must return a view model when the kind of package is found in reference data" in {

        val pkg = models.BulkPackage(kindsOfPackage.head.code, Some("marks"))

        val result = PackageConverter.toViewModel(pkg, "path", kindsOfPackage)

        result.valid.value mustEqual viewmodels.BulkPackage(kindsOfPackage.head, Some("marks"))
      }

      "must return Invalid when the kind of package cannot be found in reference data" in {

        val pkg = models.BulkPackage("invalid code", Some("marks"))

        val result = PackageConverter.toViewModel(pkg, "path", kindsOfPackage)

        result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.kindOfPackage", "invalid code"))
      }
    }

    "when give an unpacked package" - {

      "must return a view model when the kind of package is found in reference data" in {

        val pkg = models.UnpackedPackage(kindsOfPackage.head.code, 1, Some("marks"))

        val result = PackageConverter.toViewModel(pkg, "path", kindsOfPackage)

        result.valid.value mustEqual viewmodels.UnpackedPackage(kindsOfPackage.head, 1, Some("marks"))
      }

      "must return Invalid when the kind of package cannot be found in reference data" in {

        val pkg = models.UnpackedPackage("invalid code", 1, Some("marks"))

        val result = PackageConverter.toViewModel(pkg, "path", kindsOfPackage)

        result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.kindOfPackage", "invalid code"))
      }
    }

    "when given a regular package" - {

      "must return a view model when the kind of package is found in reference data" in {

        val pkg = models.RegularPackage(kindsOfPackage.head.code, 1, "marks")

        val result = PackageConverter.toViewModel(pkg, "path", kindsOfPackage)

        result.valid.value mustEqual viewmodels.RegularPackage(kindsOfPackage.head, 1, "marks")
      }

      "must return Invalid when the kind of package cannot be found in reference data" in {

        val pkg = models.RegularPackage("invalid code", 1, "marks")

        val result = PackageConverter.toViewModel(pkg, "path", kindsOfPackage)

        result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.kindOfPackage", "invalid code"))
      }
    }
  }
}
