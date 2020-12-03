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

package viewmodels
import generators.ReferenceModelGenerators
import models.reference.PreviousDocumentTypes
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class PreviousDocumentTypeSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with ReferenceModelGenerators {

  "PreviousDocumentType" - {

    "display" - {

      "must include description and reference when information does not exist" in {

        forAll(arbitrary[PreviousDocumentTypes], stringWithMaxLength(35)) {
          (previousDocumentTypes, reference) =>
            val document = PreviousDocumentType(previousDocumentTypes, reference, None)

            document.display mustEqual s"${previousDocumentTypes.description} - $reference"
        }
      }

      "must include description, reference and information when information exists" in {

        forAll(arbitrary[PreviousDocumentTypes], stringWithMaxLength(35), stringWithMaxLength(26)) {
          (previousDocumentTypes, reference, information) =>
            val document = PreviousDocumentType(previousDocumentTypes, reference, Some(information))

            document.display mustEqual s"${previousDocumentTypes.description} - $reference - $information"
        }
      }

    }

  }

}
