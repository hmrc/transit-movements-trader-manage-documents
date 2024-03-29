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

import models.PreviousAdministrativeReference
import models.reference.PreviousDocumentTypes

final case class PreviousDocumentType(
  documentType: PreviousDocumentTypes,
  previousAdminReference: PreviousAdministrativeReference
) {

  val display: String = if (documentType.description.exists(_.nonEmpty)) {
    Seq(
      documentType.description,
      Some(previousAdminReference.documentReference),
      previousAdminReference.complimentOfInfo
    ).flatten.mkString(" - ")
  } else {
    Seq(
      Some(previousAdminReference.documentType),
      Some(previousAdminReference.documentReference),
      previousAdminReference.complimentOfInfo
    ).flatten.mkString(" - ")
  }
}
