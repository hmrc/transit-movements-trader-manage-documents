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

import models.reference.DocumentType

object ProducedDocumentConverter extends Converter {

  def toViewModel(document: models.ProducedDocument, path: String, documentTypes: Seq[DocumentType]): ValidationResult[viewmodels.ProducedDocument] =
    findReferenceData(document.documentType, documentTypes, s"$path.documentType")
      .map {
        documentType =>
          viewmodels.ProducedDocument(documentType, document.reference, document.complementOfInformation)
      }
}
