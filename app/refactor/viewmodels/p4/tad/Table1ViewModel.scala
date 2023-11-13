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
import models.reference.Country
import refactor.viewmodels.p4.tad.Table1ViewModel.ConsignorViewModel

case class Table1ViewModel(
  consignorViewModel: Option[ConsignorViewModel]
)

object Table1ViewModel {

  case class ConsignorViewModel(
    eori: String,
    name: String,
    streetAndNumber: String,
    postcode: String
  )

  object ConsignorViewModel {

    def apply(ie029: CC029CType): Option[ConsignorViewModel] =
      ???
  }

  def apply(ie029: CC029CType, countries: Seq[Country]): Table1ViewModel =
    new Table1ViewModel(
      consignorViewModel = ConsignorViewModel(ie029)
    )
}
