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

package viewmodels.unloadingpermission

import generated.rfc37._

case class P5UnloadingPermissionPdfViewModel(
  mrn: String,
  consignmentItemViewModels: Seq[ConsignmentItemViewModel],
  table1ViewModel: Table1ViewModel
)

object P5UnloadingPermissionPdfViewModel {

  /** @param ie043 unloading permission
    * @return P5 unloading permission view model
    */
  def apply(ie043: CC043CType): P5UnloadingPermissionPdfViewModel = {
    val consignmentItems: Seq[ConsignmentItemType04] =
      ie043.Consignment.fold[Seq[ConsignmentItemType04]](Nil) {
        _.HouseConsignment.flatMap(_.ConsignmentItem)
      }

    new P5UnloadingPermissionPdfViewModel(
      mrn = ie043.TransitOperation.MRN,
      consignmentItemViewModels = consignmentItems.map(ConsignmentItemViewModel(_)),
      table1ViewModel = Table1ViewModel(ie043)
    )
  }
}
