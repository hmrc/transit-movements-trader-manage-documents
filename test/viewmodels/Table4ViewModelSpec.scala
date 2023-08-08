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

package viewmodels

import base.DepartureData
import base.SpecBase
import models.P5.departure.IE029Data
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import viewmodels.P5._

class Table4ViewModelSpec extends AnyFreeSpec with Matchers with SpecBase with DepartureData {

  val ie029Data: IE029Data       = IE029Data(departureMessageData)
  val viewModel: Table4ViewModel = Table4ViewModel()(ie029Data)

  "table4ViewModelSpec" - {

    "should return countryOfRoutingOfConsignment information" in {
      viewModel.countryOfRoutingOfConsignment mustBe "Seqnum12243, GB"
    }

    "should return customsOfficeOfTransitDeclared information" in {
      viewModel.customsOfficeOfTransitDeclared mustBe "seq001, ref001, -999999999-01-01T00:00"
    }

    "should return customsOfficeOfExitForTransitDeclared information" in {
      viewModel.customsOfficeOfExitForTransitDeclared mustBe "seq001, ref001"
    }

    "should return customsOfficeOfDeparture information" in {
      viewModel.customsOfficeOfDeparture mustBe "Ref001"
    }

    "should return customsOfficeOfDestinationDeclared information" in {
      viewModel.customsOfficeOfDestinationDeclared mustBe "Ref001"
    }

    "should return countryOfDispatch information" in {
      viewModel.countryOfDispatch mustBe "GER"
    }

    "should return countryOfDestination information" in {
      viewModel.countryOfDestination mustBe "GB"
    }

  }
}
