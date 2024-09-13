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

package viewmodels.tad.transition

import base.SpecBase
import viewmodels.DummyData

class SecondPageViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val consignmentItemViewModels: Seq[ConsignmentItemViewModel] = Nil
    val result                                                   = SecondPageViewModel(cc029c, consignmentItemViewModels)

    "departureOffice" in {
      result.departureOffice mustBe "cood"
    }

    "acceptanceDate" in {
      result.acceptanceDate mustBe "03/02/1996"
    }

    "consignmentItemViewModels" in {
      result.consignmentItemViewModels mustBe consignmentItemViewModels
    }
  }
}
