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

package refactor.viewmodels.p5.tad

import generated.p5._
import refactor.viewmodels.p5._

case class P5TadPdfViewModel(
  mrn: String,
  consignmentItemViewModels: Seq[ConsignmentItemViewModel],
  table1ViewModel: Table1ViewModel,
  table2ViewModel: Table2ViewModel,
  table3ViewModel: Table3ViewModel,
  table4ViewModel: Table4ViewModel
)

object P5TadPdfViewModel {

  /** @param ie029 release for transit
    * @return P5 TAD view model based on P5 (final) data
    */
  def apply(ie029: CC029CType): P5TadPdfViewModel =
    new P5TadPdfViewModel(
      mrn = ie029.TransitOperation.MRN,
      consignmentItemViewModels = ie029.rollDown.Consignment.HouseConsignment.flatMap {
        houseConsignment => houseConsignment.ConsignmentItem.map(ConsignmentItemViewModel(houseConsignment, _))
      },
      table1ViewModel = Table1ViewModel(ie029),
      table2ViewModel = Table2ViewModel(ie029),
      table3ViewModel = Table3ViewModel(ie029),
      table4ViewModel = Table4ViewModel(ie029)
    )
}
