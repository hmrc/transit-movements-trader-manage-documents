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

import generated.p5.CC029CType
import refactor.viewmodels._
import refactor.viewmodels.p5.RichCC029CType

case class Table2ViewModel(
  numberOfItems: Int,
  grossMass: String,
  printBindingItinerary: Boolean,
  consignmentItemViewModel: Option[ConsignmentItemViewModel]
)

object Table2ViewModel {

  def apply(ie029: CC029CType, consignmentItemViewModel: Option[ConsignmentItemViewModel]): Table2ViewModel =
    new Table2ViewModel(
      numberOfItems = ie029.numberOfItems,
      grossMass = ie029.Consignment.grossMass.asString,
      printBindingItinerary = ie029.TransitOperation.bindingItinerary.toBoolean,
      consignmentItemViewModel = consignmentItemViewModel
    )
}
