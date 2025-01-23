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

package viewmodels.tad.posttransition

import base.SpecBase
import viewmodels.DummyData

class Table4ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = Table4ViewModel(ie015, cc029c)

    "countryOfRoutingOfConsignment" in {
      result.countryOfRoutingOfConsignment mustBe "1/corocc1;...;4/corocc4."
    }

    "customsOfficeOfTransitDeclared" in {
      result.customsOfficeOfTransitDeclared mustBe "1/cootd1; 2/cootd2."
    }

    "customsOfficeOfExitForTransitDeclared" in {
      result.customsOfficeOfExitForTransitDeclared mustBe "1/cooeftd1; 2/cooeftd2."
    }

    "customsOfficeOfDeparture" in {
      result.customsOfficeOfDeparture mustBe "cood."
    }

    "customsOfficeOfDestinationDeclared" in {
      result.customsOfficeOfDestinationDeclared mustBe "coodd."
    }

    "countryOfDispatch" in {
      result.countryOfDispatch mustBe "c of di..."
    }

    "countryOfDestination" in {
      result.countryOfDestination mustBe "c of de..."
    }

    "limitDate" in {
      result.limitDate mustBe "2022-02-03"
    }

    "bindingItinerary" in {
      result.bindingItinerary mustBe "X"
    }
  }
}
