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

package viewmodels.tad.posttransition

import generated._
import viewmodels._

case class TadPdfViewModel(
  mrn: String,
  consignmentItemViewModels: Seq[ConsignmentItemViewModel],
  table1ViewModel: Table1ViewModel,
  table2ViewModel: Table2ViewModel,
  table3ViewModel: Table3ViewModel,
  table4ViewModel: Table4ViewModel
)

object TadPdfViewModel {

  /** @param ie015
    *   declaration data
    * @param ie029
    *   release for transit
    * @return
    *   TAD view model based on P5 (final) data
    */
  def apply(ie015: CC015CType, ie029: CC029CType): TadPdfViewModel =
    new TadPdfViewModel(
      mrn = ie029.TransitOperation.MRN,
      consignmentItemViewModels = ie029.rollDown.Consignment.HouseConsignment.flatMap {
        houseConsignment => houseConsignment.ConsignmentItem.map(ConsignmentItemViewModel(ie015, houseConsignment, _))
      },
      table1ViewModel = Table1ViewModel(ie029),
      table2ViewModel = Table2ViewModel(ie029),
      table3ViewModel = Table3ViewModel(ie029),
      table4ViewModel = Table4ViewModel(ie015, ie029)
    )
}
