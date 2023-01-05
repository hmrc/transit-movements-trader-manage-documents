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

import base.SpecBase
import generators.GeneratorHelpers
import models.PreviousAdministrativeReference
import models.reference.PreviousDocumentTypes
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class PreviousDocumentTypeSpec extends SpecBase with ScalaCheckPropertyChecks with GeneratorHelpers {

  ".display" - {

    "must return formatted previous administrative reference" - {

      "when previous document description isn't defined" in {

        forAll(arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[Option[String]]) {
          (pdCode, parDocumentType, parDocumentReference, parComplimentOfInfo) =>
            val previousDocumentType = PreviousDocumentType(
              documentType = PreviousDocumentTypes(
                code = pdCode,
                description = None
              ),
              previousAdminReference = PreviousAdministrativeReference(
                documentType = parDocumentType,
                documentReference = parDocumentReference,
                complimentOfInfo = parComplimentOfInfo
              )
            )

            val result = previousDocumentType.display

            if (parComplimentOfInfo.isDefined) {
              result mustBe s"$parDocumentType - $parDocumentReference - ${parComplimentOfInfo.get}"
            } else {
              result mustBe s"$parDocumentType - $parDocumentReference"
            }
        }
      }

      "when previous document description is defined but empty" in {

        forAll(arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[Option[String]]) {
          (pdCode, parDocumentType, parDocumentReference, parComplimentOfInfo) =>
            val previousDocumentType = PreviousDocumentType(
              documentType = PreviousDocumentTypes(
                code = pdCode,
                description = Some("")
              ),
              previousAdminReference = PreviousAdministrativeReference(
                documentType = parDocumentType,
                documentReference = parDocumentReference,
                complimentOfInfo = parComplimentOfInfo
              )
            )

            val result = previousDocumentType.display

            if (parComplimentOfInfo.isDefined) {
              result mustBe s"$parDocumentType - $parDocumentReference - ${parComplimentOfInfo.get}"
            } else {
              result mustBe s"$parDocumentType - $parDocumentReference"
            }
        }
      }
    }

    "must return formatted previous document" - {
      "when previous document description is defined and non-empty" in {

        forAll(nonEmptyString, arbitrary[String], arbitrary[String], arbitrary[String], arbitrary[Option[String]]) {
          (pdDescription, pdCode, parDocumentType, parDocumentReference, parComplimentOfInfo) =>
            val previousDocumentType = PreviousDocumentType(
              documentType = PreviousDocumentTypes(
                code = pdCode,
                description = Some(pdDescription)
              ),
              previousAdminReference = PreviousAdministrativeReference(
                documentType = parDocumentType,
                documentReference = parDocumentReference,
                complimentOfInfo = parComplimentOfInfo
              )
            )

            val result = previousDocumentType.display

            if (parComplimentOfInfo.isDefined) {
              result mustBe s"$pdDescription - $parDocumentReference - ${parComplimentOfInfo.get}"
            } else {
              result mustBe s"$pdDescription - $parDocumentReference"
            }
        }
      }
    }
  }
}
