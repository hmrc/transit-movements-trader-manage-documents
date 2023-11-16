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
import org.scalacheck.Gen
import refactor.viewmodels.DummyData
import refactor.viewmodels.p4.tad.Table4ViewModel._

class Table4ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = Table4ViewModel(cc029c)

    "guaranteeViewModels" in {
      result.guaranteeViewModels mustBe Seq(
        GuaranteeViewModel(
          typeValue = "g1",
          referenceNumbers = Seq("1grn1", "1grn2"),
          otherReferenceNumber = "ogr1"
        ),
        GuaranteeViewModel(
          typeValue = "g2",
          referenceNumbers = Seq("2grn1", "2grn2"),
          otherReferenceNumber = "ogr2"
        )
      )
    }

    "principal" in {
      result.principal mustBe PrincipalViewModel(
        name = "hn",
        streetAndNumber = "san",
        postCode = "pc",
        city = "city",
        country = "country",
        eori = Some("hin"),
        tir = Some("thin")
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
          reference = "cootd1",
          dateTime = Some("03/02/2010 08:45")
        ),
        TransitOfficeViewModel(
          reference = "cootd2",
          dateTime = Some("03/02/2015 08:45")
        )
      )
    }

    "destinationOffice" in {
      result.destinationOffice.value mustBe TransitOfficeViewModel(
        reference = "coodd",
        dateTime = None
      )
    }

    "authId" in {
      result.authId mustBe None
    }

    "isSimplifiedMovement" in {
      result.isSimplifiedMovement mustBe true
    }
  }

  "GuaranteeViewModel" - {
    "must format as string" - {
      "when type value is 4 and more than one guarantee" - {
        val gvm = GuaranteeViewModel(
          typeValue = "4",
          referenceNumbers = Seq("guaranteereference1", "guaranteereference2"),
          otherReferenceNumber = "otherreferencenumber"
        )

        gvm.ref mustBe "guaranteereference1 - rence2"
        gvm.otherRef mustBe ""
      }

      "when type value is not 4 and more than one guarantee" - {
        val gvm = GuaranteeViewModel(
          typeValue = "1",
          referenceNumbers = Seq("guaranteereference1", "guaranteereference2"),
          otherReferenceNumber = "otherreferencenumber"
        )

        gvm.ref mustBe "guaranteereference1 ***"
        gvm.otherRef mustBe "otherreferencenumber ***"
      }

      "one guarantee" - {
        val gvm = GuaranteeViewModel(
          typeValue = Gen.oneOf("1", "4").sample.value,
          referenceNumbers = Seq("guaranteereference1"),
          otherReferenceNumber = "otherreferencenumber"
        )

        gvm.ref mustBe "guaranteereference1"
        gvm.otherRef mustBe "otherreferencenumber"
      }

      "no guarantees (this should never happen)" - {
        val gvm = GuaranteeViewModel(
          typeValue = Gen.oneOf("1", "4").sample.value,
          referenceNumbers = Nil,
          otherReferenceNumber = "otherreferencenumber"
        )

        gvm.ref mustBe ""
        gvm.otherRef mustBe ""
      }
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
          tir = Some("thin")
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
          tir = None
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
          tir = Some("thin")
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
          tir = None
        )

        pvm.toString mustBe ""
      }
    }
  }
}
