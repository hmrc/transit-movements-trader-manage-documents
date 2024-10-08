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

class Table5ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = Table5ViewModel(cc015c, cc029c)

    "controlResult" in {
      result.controlResult mustBe None
    }

    "limitDate" in {
      result.limitDate mustBe "03/02/2022"
    }
  }
}
