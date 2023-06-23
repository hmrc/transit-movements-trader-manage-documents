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

package viewmodels.P5

import models.P5.departure.CustomsOfficeOfDeparture
import models.P5.departure.IE029Data

case class TableViewModel(implicit ie029Data: IE029Data) {
  val table1ViewModel = Table1ViewModel.apply()
  val table2ViewModel = Table2ViewModel.apply()
  val table3ViewModel = Table3ViewModel.apply()
  val table4ViewModel = Table4ViewModel.apply()

}

case class Table1ViewModel(implicit ie029Data: IE029Data) {

  val consignor = ie029Data.data.Consignment.Consignor match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

}

case class Table2ViewModel(implicit ie029Data: IE029Data) {

  val consignor = ie029Data.data.Consignment.Consignor match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

}

case class Table3ViewModel(implicit ie029Data: IE029Data) {

  val consignor = ie029Data.data.Consignment.Consignor match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

}

case class Table4ViewModel(implicit ie029Data: IE029Data) {

  val countryOfRoutingOfConsignment = ie029Data.data.Consignment.CountryOfRoutingOfConsignment match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val customsOfficeOfTransitDeclared = ie029Data.data.CustomsOfficeOfTransitDeclared match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val customsOfficeOfExitForTransitDeclared = ie029Data.data.CustomsOfficeOfExitForTransitDeclared match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val customsOfficeOfDeparture = ie029Data.data.CustomsOfficeOfDeparture.toString

  val customsOfficeOfDestinationDeclared = ie029Data.data.CustomsOfficeOfDestinationDeclared.toString

  val countryOfDispatch = ie029Data.data.Consignment.countryOfDispatch match {
    case Some(value) => value
    case None        => "TODO get multiple consignor"
  }

  val countryOfDestination = ie029Data.data.Consignment.countryOfDestination match {
    case Some(value) => value
    case None        => "TODO get multiple consignor"
  }

}
