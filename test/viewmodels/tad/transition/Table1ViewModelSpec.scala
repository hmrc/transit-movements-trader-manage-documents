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
import viewmodels.tad.transition.Table1ViewModel._

class Table1ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = Table1ViewModel(cc029c)

    "declarationType" in {
      result.declarationType mustBe "T"
    }

    "consignorViewModel" in {
      result.consignorViewModel.value mustBe ConsignorViewModel(
        eori = "in",
        name = "name",
        streetAndNumber = "san",
        postcode = "pc",
        city = "city",
        country = "country",
        contactName = "ccp",
        phoneNumber = "ccptel",
        emailAddress = "ccpemail"
      )
    }

    "numberOfItems" in {
      result.numberOfItems mustBe 2
    }

    "totalNumberOfPackages" in {
      result.totalNumberOfPackages mustBe "66"
    }

    "consigneeViewModel" in {
      result.consigneeViewModel.value mustBe ConsigneeViewModel(
        eori = "in",
        name = "name",
        streetAndNumber = "san",
        postcode = "pc",
        city = "city",
        country = "country"
      )
    }

    "countryOfDispatch" in {
      result.countryOfDispatch mustBe "c of dispatch"
    }

    "countryOfDestination" in {
      result.countryOfDestination mustBe "c of destination"
    }

    "transportIdentity" in {
      result.transportIdentity mustBe "1, toi1, in1..."
    }

    "transportCountry" in {
      result.transportCountry mustBe "nat1"
    }

    "returnCopiesCustomsOffice" in {
      result.returnCopiesCustomsOffice mustBe None
    }
  }
}
