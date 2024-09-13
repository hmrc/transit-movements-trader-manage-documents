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

package viewmodels.p4.tad

import base.SpecBase
import viewmodels.p4.tad.Table4ViewModel._
import viewmodels.DummyData

class Table4ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = Table4ViewModel(cc029c)

    "guaranteeType" in {
      result.guaranteeType mustBe "g1..."
    }

    "principal" in {
      result.principal mustBe PrincipalViewModel(
        name = "hn",
        streetAndNumber = "san",
        postCode = "pc",
        city = "city",
        country = "country",
        eori = Some("hin"),
        tir = Some("thin"),
        contactName = "cp",
        phoneNumber = "cptel",
        emailAddress = "cpemail"
      )
    }

    "departureOffice" in {
      result.departureOffice mustBe "cood"
    }

    "acceptanceDate" in {
      result.acceptanceDate mustBe "03/02/1996"
    }

    "transitOffices" in {
      result.transitOffices mustBe Seq(
        TransitOfficeViewModel(
          sequenceNumber = Some("1"),
          reference = "cootd1",
          dateTime = Some("03/02/2010 08:45")
        ),
        TransitOfficeViewModel(
          sequenceNumber = Some("2"),
          reference = "cootd2",
          dateTime = Some("03/02/2015 08:45")
        )
      )
    }

    "destinationOffice" in {
      result.destinationOffice.value mustBe TransitOfficeViewModel(
        sequenceNumber = None,
        reference = "coodd",
        dateTime = None
      )
    }

    "authId" in {
      result.authId mustBe None
    }

    "bindingItinerary" in {
      result.bindingItinerary mustBe true
    }

    "isSimplifiedMovement" in {
      result.isSimplifiedMovement mustBe true
    }
  }

  "PrincipalViewModel" - {
    "must format as string" - {
      "when EORI is defined and TIR is defined" - {
        val pvm = PrincipalViewModel(
          name = "hn",
          streetAndNumber = "san",
          postCode = "pc",
          city = "city",
          country = "country",
          eori = Some("hin"),
          tir = Some("thin"),
          contactName = "contact name",
          phoneNumber = "0492938483",
          emailAddress = "emailPrincipal@hotmail.com"
        )

        pvm.toString mustBe "hin / thin"
      }

      "when EORI is defined and TIR is not defined" - {
        val pvm = PrincipalViewModel(
          name = "hn",
          streetAndNumber = "san",
          postCode = "pc",
          city = "city",
          country = "country",
          eori = Some("hin"),
          tir = None,
          contactName = "contact name",
          phoneNumber = "0492938483",
          emailAddress = "emailPrincipal@hotmail.com"
        )

        pvm.toString mustBe "hin"
      }

      "when EORI is not defined and TIR is defined" - {
        val pvm = PrincipalViewModel(
          name = "hn",
          streetAndNumber = "san",
          postCode = "pc",
          city = "city",
          country = "country",
          eori = None,
          tir = Some("thin"),
          contactName = "contact name",
          phoneNumber = "0492938483",
          emailAddress = "emailPrincipal@hotmail.com"
        )

        pvm.toString mustBe ""
      }

      "when EORI is not defined and TIR is not defined" - {
        val pvm = PrincipalViewModel(
          name = "hn",
          streetAndNumber = "san",
          postCode = "pc",
          city = "city",
          country = "country",
          eori = None,
          tir = None,
          contactName = "contact name",
          phoneNumber = "0492938483",
          emailAddress = "emailPrincipal@hotmail.com"
        )

        pvm.toString mustBe ""
      }
    }
  }
}
