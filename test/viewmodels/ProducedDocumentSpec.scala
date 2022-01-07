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

package viewmodels

import generators.ReferenceModelGenerators
import models.reference.DocumentType
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ProducedDocumentSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with ReferenceModelGenerators {

  "display" - {

    "must only include the document type's description when `reference` and `complementOfInformation` are not present" in {

      forAll(arbitrary[DocumentType]) {
        documentType =>
          val document = ProducedDocument(documentType, None, None)

          document.display mustEqual documentType.description
      }
    }

    "must include the document type's description and the reference when complementOfInformation is not present" in {

      forAll(arbitrary[DocumentType], stringWithMaxLength(50)) {
        (documentType, reference) =>
          val document = ProducedDocument(documentType, Some(reference), None)

          document.display mustEqual s"${documentType.description} - $reference"
      }
    }

    "must include the document type's description and the complementOfInformation when the reference is not present" in {

      forAll(arbitrary[DocumentType], stringWithMaxLength(50)) {
        (documentType, complementOfInformation) =>
          val document = ProducedDocument(documentType, None, Some(complementOfInformation))

          document.display mustEqual s"${documentType.description} - $complementOfInformation"
      }
    }

    "must include the document type's description, the reference and the complementOfInformation when all are present" in {
      forAll(arbitrary[DocumentType], stringWithMaxLength(50), stringWithMaxLength(50)) {
        (documentType, reference, complementOfInformation) =>
          val document = ProducedDocument(documentType, Some(reference), Some(complementOfInformation))

          document.display mustEqual s"${documentType.description} - $reference - $complementOfInformation"
      }
    }
  }
}
