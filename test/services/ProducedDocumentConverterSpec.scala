/*
 * Copyright 2021 HM Revenue & Customs
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
import models.reference.DocumentType
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class ProducedDocumentConverterSpec extends AnyFreeSpec with Matchers with ValidatedMatchers with ValidatedValues {

  private val documentTypes = Seq(DocumentType("a", "doc 1", true), DocumentType("b", "doc 2", true))

  "toViewModel" - {

    "must return a view model when the country code is found in the reference data" in {

      val document = models.ProducedDocument(documentTypes.head.code, Some("ref"), Some("complement"))

      val result = ProducedDocumentConverter.toViewModel(document, "path", documentTypes)

      result.valid.value mustEqual viewmodels.ProducedDocument(documentTypes.head, Some("ref"), Some("complement"))
    }

    "must return Data Not Found when the country code cannot be found in the reference data" in {

      val document = models.ProducedDocument("invalid code", Some("ref"), Some("complement"))

      val result = ProducedDocumentConverter.toViewModel(document, "path", documentTypes)

      result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.documentType", "invalid code"))
    }
  }
}
