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

package models.P5.departure

import play.api.libs.json.Json
import play.api.libs.json.OFormat

import java.time.LocalDate

case class IE015TransitOperation(
  limitDate: Option[String]
)

object IE015TransitOperation {
  implicit val formats: OFormat[IE015TransitOperation] = Json.format[IE015TransitOperation]
}

case class IE029TransitOperation(
  LRN: String,
  MRN: String,
  declarationType: String,
  additionalDeclarationType: String,
  TIRCarnetNumber: Option[String],
  declarationAcceptanceDate: Option[LocalDate],
  releaseDate: LocalDate,
  security: String,
  reducedDatasetIndicator: String,
  specificCircumstanceIndicator: Option[String],
  communicationLanguageAtDeparture: Option[String],
  bindingItinerary: String
)

object IE029TransitOperation {
  implicit val formats: OFormat[IE029TransitOperation] = Json.format[IE029TransitOperation]
}
