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

import generated.p4._
import generated.p5._
import models.reference.Country
import refactor.viewmodels._

case class P4TadPdfViewModel(
  mrn: String,
  consignmentItemViewModels: Seq[ConsignmentItemViewModel],
  table1ViewModel: Table1ViewModel,
  table2ViewModel: Table2ViewModel,
  table4ViewModel: Table4ViewModel,
  table5ViewModel: Table5ViewModel
)

object P4TadPdfViewModel {

  /** @param ie029 release for transit
    * @return P4 TAD view model based on P4 data
    */
  def apply(ie029: CC029BType): P4TadPdfViewModel = ???

  /** @param ie015 declaration data
    * @param ie029 release for transit
    * @param countries CountryCodesForAddress
    * @return P4 TAD view model based on P5 (transition) data
    */
  def apply(ie015: CC015CType, ie029: CC029CType, countries: Seq[Country]): P4TadPdfViewModel = {
    val consignmentItems = {
      val original = ie029.Consignment.HouseConsignment.flatMap(_.ConsignmentItem)
      ie029.Consignment.referenceNumberUCR match {
        case Some(referenceNumberUCR) => original.map(_.copy(referenceNumberUCR = Some(referenceNumberUCR)))
        case None                     => original
      }
    }

    new P4TadPdfViewModel(
      mrn = Seq(ie029.TransitOperation.MRN, ie029.TransitOperation.LRN).commaSeparate,
      consignmentItemViewModels = consignmentItems.map(ConsignmentItemViewModel(ie029.Consignment, _)),
      table1ViewModel = Table1ViewModel(ie029, countries),
      table2ViewModel = Table2ViewModel(ie029),
      table4ViewModel = Table4ViewModel(ie029),
      table5ViewModel = Table5ViewModel(ie015, ie029)
    )
  }
}
