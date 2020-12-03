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

package services

import cats.scalatest.ValidatedMatchers
import cats.scalatest.ValidatedValues
import models.reference.PreviousDocumentTypes
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

class PreviousDocumentConverterSpec extends FreeSpec with MustMatchers with ValidatedMatchers with ValidatedValues {

  private val previousDocumentTypes = Seq(PreviousDocumentTypes("235", "Container list"), PreviousDocumentTypes("270", "Loading list (delivery note)"))

  "toViewModel" - {

    "must return a view model when the country code is found in the reference data" in {

      val document = models.PreviousAdministrativeReference(previousDocumentTypes.head.code, "ref", Some("complement"))

      val result = PreviousDocumentConverter.toViewModel(document, "path", previousDocumentTypes)

      result.valid.value mustEqual viewmodels.PreviousDocumentType(previousDocumentTypes.head, "ref", Some("complement"))
    }

    "must return Data Not Found when the country code cannot be found in the reference data" in {

      val document = models.PreviousAdministrativeReference("invalid code", "ref", Some("complement"))

      val result = PreviousDocumentConverter.toViewModel(document, "path", previousDocumentTypes)

      result.invalidValue.toChain.toList must contain theSameElementsAs Seq(ReferenceDataNotFound("path.previousDocumentTypes", "invalid code"))
    }
  }
}
