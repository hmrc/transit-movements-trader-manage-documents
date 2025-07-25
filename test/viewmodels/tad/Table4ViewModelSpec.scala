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

import base.SpecBase
import viewmodels.DummyData

class Table4ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = Table4ViewModel(cc015c, cc029c)

    "countryOfRoutingOfConsignment" in {
      result.countryOfRoutingOfConsignment mustEqual "1/corocc1;...;4/corocc4."
    }

    "customsOfficeOfTransitDeclared" in {
      result.customsOfficeOfTransitDeclared mustEqual "1/cootd1; 2/cootd2."
    }

    "customsOfficeOfExitForTransitDeclared" in {
      result.customsOfficeOfExitForTransitDeclared mustEqual "1/cooeftd1; 2/cooeftd2."
    }

    "customsOfficeOfDeparture" in {
      result.customsOfficeOfDeparture mustEqual "cood."
    }

    "customsOfficeOfDestinationDeclared" in {
      result.customsOfficeOfDestinationDeclared mustEqual "coodd."
    }

    "countryOfDispatch" in {
      result.countryOfDispatch mustEqual "c of di..."
    }

    "countryOfDestination" in {
      result.countryOfDestination mustEqual "c of de..."
    }

    "limitDate" in {
      result.limitDate mustEqual "2022-02-03"
    }

    "bindingItinerary" in {
      result.bindingItinerary mustEqual "X"
    }
  }
}
