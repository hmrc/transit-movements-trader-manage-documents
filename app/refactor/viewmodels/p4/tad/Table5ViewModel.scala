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

case class Table5ViewModel(
  seals: Seq[String],
  bindingItinerary: Boolean,
  controlResult: Option[String],
  limitDate: String
) {

  val numberOfSeals: String = if (seals.isEmpty) "---" else seals.size.toString

  val sealsForDisplay: String = if (seals.isEmpty) "---" else seals.firstAndLast(" - ")
}

object Table5ViewModel {

  def apply(ie015: CC015CType, ie029: CC029CType): Table5ViewModel =
    new Table5ViewModel(
      seals = ie029.Consignment.TransportEquipment.flatMap(_.Seal).map(_.asP4String),
      bindingItinerary = ie029.TransitOperation.bindingItinerary.toBoolean,
      controlResult = None,
      limitDate = ie015.TransitOperation.limitDate.map(_.dateString).orElseBlank
    )
}
