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

package viewmodels.p4.tad

import generated.p5._
import viewmodels._
import viewmodels.p5._

case class SecondPageViewModel(
  departureOffice: String,
  acceptanceDate: String,
  authorisation: Seq[String],
  consignmentItemViewModels: Seq[ConsignmentItemViewModel]
)

object SecondPageViewModel {

  def apply(ie029: CC029CType, consignmentItemViewModels: Seq[ConsignmentItemViewModel]): SecondPageViewModel =
    new SecondPageViewModel(
      departureOffice = ie029.CustomsOfficeOfDeparture.asString,
      acceptanceDate = ie029.TransitOperation.declarationAcceptanceDate.dateString,
      authorisation = ie029.Authorisation.map(_.asString),
      consignmentItemViewModels = consignmentItemViewModels
    )
}
