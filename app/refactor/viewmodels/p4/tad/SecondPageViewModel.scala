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

package refactor.viewmodels.p4.tad

import generated.p5._
import refactor.viewmodels._
import refactor.viewmodels.p5._

case class SecondPageViewModel(
  departureOffice: String,
  acceptanceDate: String,
  consignmentItemViewModels: Seq[ConsignmentItemViewModel]
)

object SecondPageViewModel {

  def apply(ie029: CC029CType): SecondPageViewModel = {
    val consignmentItems = {
      val original = ie029.consignmentItems
      ie029.Consignment.referenceNumberUCR match {
        case Some(referenceNumberUCR) => original.map(_.copy(referenceNumberUCR = Some(referenceNumberUCR)))
        case None                     => original
      }
    }

    new SecondPageViewModel(
      departureOffice = ie029.CustomsOfficeOfDeparture.asString,
      acceptanceDate = ie029.TransitOperation.declarationAcceptanceDate.dateString,
      consignmentItemViewModels = consignmentItems.map(ConsignmentItemViewModel(ie029, _))
    )
  }
}
