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
import refactor.viewmodels._
import refactor.viewmodels.p5._

case class Table4ViewModel(
  countryOfRoutingOfConsignment: String,
  customsOfficeOfTransitDeclared: String,
  customsOfficeOfExitForTransitDeclared: String,
  customsOfficeOfDeparture: String,
  customsOfficeOfDestinationDeclared: String,
  countryOfDispatch: String,
  countryOfDestination: String
)

object Table4ViewModel {

  def apply(ie029: CC029CType): Table4ViewModel =
    new Table4ViewModel(
      countryOfRoutingOfConsignment = ie029.Consignment.CountryOfRoutingOfConsignment.map(_.asString).semiColonSeparate.take30,
      customsOfficeOfTransitDeclared = ie029.CustomsOfficeOfTransitDeclared
        .map(
          x => s"${x.sequenceNumber}/${x.referenceNumber}"
        )
        .semiColonSeparate,
      customsOfficeOfExitForTransitDeclared = ie029.CustomsOfficeOfExitForTransitDeclared
        .map(
          x => s"${x.sequenceNumber}/${x.referenceNumber}"
        )
        .semiColonSeparate,
      customsOfficeOfDeparture = ie029.CustomsOfficeOfDeparture.asString.take10,
      customsOfficeOfDestinationDeclared = ie029.CustomsOfficeOfDestinationDeclared.asString.take10,
      countryOfDispatch = ie029.Consignment.countryOfDispatch.orElseBlank.take10,
      countryOfDestination = ie029.Consignment.countryOfDestination.orElseBlank.take10
    )
}
