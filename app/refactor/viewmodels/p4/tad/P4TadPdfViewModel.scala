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
import refactor.viewmodels.p5.RichCC029CType

case class P4TadPdfViewModel(
  mrn: String,
  lrn: Option[String],
  secondPageViewModel: SecondPageViewModel,
  table1ViewModel: Table1ViewModel,
  table2ViewModel: Table2ViewModel,
  table4ViewModel: Table4ViewModel,
  table5ViewModel: Table5ViewModel,
  securityViewModel: Option[SecurityViewModel]
)

object P4TadPdfViewModel {

  /** @param ie029 release for transit
    * @return P4 TAD view model based on P4 data
    */
  def apply(ie029: CC029BType): P4TadPdfViewModel = ???

  /** @param ie015 declaration data
    * @param ie029 release for transit
    * @return P4 TAD view model based on P5 (transition) data
    */
  def apply(ie015: CC015CType, ie029: CC029CType): P4TadPdfViewModel = {
    val consignmentItemViewModels = ie029.consignmentItems.map(ConsignmentItemViewModel(ie029, _))

    new P4TadPdfViewModel(
      mrn = ie029.TransitOperation.MRN,
      lrn = Some(ie029.TransitOperation.LRN),
      secondPageViewModel = SecondPageViewModel(ie029, consignmentItemViewModels),
      table1ViewModel = Table1ViewModel(ie029),
      table2ViewModel = Table2ViewModel(ie029, if (printListOfItems(consignmentItemViewModels)) None else consignmentItemViewModels.headOption),
      table4ViewModel = Table4ViewModel(ie029),
      table5ViewModel = Table5ViewModel(ie015, ie029),
      securityViewModel = ie029.TransitOperation.security match {
        case "0" => None
        case _   => Some(SecurityViewModel(ie029))
      }
    )
  }

  private def printListOfItems(consignmentItemViewModels: Seq[ConsignmentItemViewModel]): Boolean =
    consignmentItemViewModels match {
      case Nil =>
        false
      case head :: Nil =>
        head.containers.size > 1 ||
          head.sensitiveGoodsInformation.size > 1 ||
          head.packages.size > 1 ||
          head.specialMentions.size > 4 ||
          head.producedDocuments.size > 4
      case _ =>
        true
    }
}
