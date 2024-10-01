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
import generated.{AddressType07, ConsigneeType04, ConsignorType03, ContactPersonType01}
import viewmodels.DummyData
import viewmodels.tad.transition.Table1ViewModel.*

class Table1ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val consignor = Some(
      ConsignorType03(
        identificationNumber = Some("in"),
        name = Some("namenamename namenamename namenamename namenamename namenamename name"),
        Address = Some(
          AddressType07(
            streetAndNumber = "Street and no Street and no Street and no Street and no",
            postcode = Some("12345678901234567"),
            city = "city1234567890city1234567890",
            country = "country"
          )
        ),
        ContactPerson = Some(
          ContactPersonType01(
            name = "contact person namenamename namenamename namenamename namenamename namenamename name",
            phoneNumber = "contact person phone 1234567890",
            eMailAddress = Some("ccpemail")
          )
        )
      )
    )

    val consignee = Some(
      ConsigneeType04(
        identificationNumber = Some("in"),
        name = Some("namenamename namenamename namenamename namenamename namenamename name"),
        Address = Some(
          AddressType07(
            streetAndNumber = "Street and no Street and no Street and no Street and no",
            postcode = Some("pc"),
            city = "city",
            country = "country"
          )
        )
      )
    )

    val result = Table1ViewModel(cc029c.copy(Consignment = cc029c.Consignment.copy(Consignor = consignor, Consignee = consignee)))

    "declarationType" in {
      result.declarationType mustBe "T"
    }

    "consignorViewModel" in {
      result.consignorViewModel.value mustBe ConsignorViewModel(
        eori = "in",
        name = "namenamename namenamename namenamename namename...",
        streetAndNumber = "Street and no Street and no...",
        postcode = "1234567...",
        city = "city1234567890cit...",
        country = "country",
        contactName = "contact person namenamename namenamename namena...",
        phoneNumber = "contact person phone 1234567890",
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
        name = "namenamename namenamename namenamename namename...",
        streetAndNumber = "Street and no Street and no...",
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
