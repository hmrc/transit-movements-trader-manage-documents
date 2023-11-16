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

import base.SpecBase
import refactor.viewmodels.DummyData

class Table2ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val consignmentItemViewModel: Option[ConsignmentItemViewModel] = None
    val result                                                     = Table2ViewModel(cc029c, consignmentItemViewModel)

    "numberOfItems" in {
      result.numberOfItems mustBe 2
    }

    "grossMass" in {
      result.grossMass mustBe "200.0"
    }

    "printBindingItinerary" in {
      result.printBindingItinerary mustBe true
    }

    "consignmentItemViewModel" in {
      result.consignmentItemViewModel mustBe consignmentItemViewModel
    }
  }
}
