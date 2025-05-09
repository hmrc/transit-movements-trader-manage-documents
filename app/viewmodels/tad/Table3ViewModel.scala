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

package viewmodels.tad

import generated.*
import viewmodels.*

case class Table3ViewModel(
  containerIdentification: String
)

object Table3ViewModel {

  def apply(ie029: CC029CType): Table3ViewModel =
    new Table3ViewModel(
      containerIdentification = ie029.Consignment.TransportEquipment.flatMap(_.containerIdentificationNumber).take3(_.semiColonSeparate)
    )
}
