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
import models.reference.Country
import refactor.viewmodels.DummyData
import refactor.viewmodels.p4.tad.Table1ViewModel._

class Table1ViewModelSpec extends SpecBase with DummyData {

  private val data = ie029

  private val countries = Seq(
    Country("GB", "United Kingdom"),
    Country("FR", "France"),
    Country("ES", "Spain")
  )

  "must map data to view model" - {

    val result = Table1ViewModel(data, countries)

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
        country = "country"
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
      result.transportIdentity mustBe "toi1, in1..."
    }

    "transportCountry" in {
      result.transportCountry mustBe "United Kingdom"
    }

    "returnCopiesCustomsOffice" in {
      result.returnCopiesCustomsOffice mustBe None
    }
  }
}
