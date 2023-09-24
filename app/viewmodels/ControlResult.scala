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

import models.reference.ControlResultData
import utils.DateFormatter

import java.time.LocalDate

case class ControlResult(controlResultData: ControlResultData, controlResult: models.ControlResult) {

  lazy val isDescriptionAvailable: Boolean = controlResultData.description.nonEmpty

  lazy val displayName: String = if (isDescriptionAvailable) {
    controlResultData.description
  } else {
    controlResult.conResCodERS16
  }

  lazy val formattedDate: String = controlResult.datLimERS69.format(DateFormatter.readableDateFormatter)

  lazy val isSimplifiedMovement: Boolean = controlResult.conResCodERS16 == "A3"
}
