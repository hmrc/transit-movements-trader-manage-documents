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
import viewmodels.DummyData

class SecurityViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = SecurityViewModel(cc029c)

    "security" in {
      result.security mustBe "security"
    }

    "modeOfTransportAtTheBorder" in {
      result.modeOfTransportAtTheBorder mustBe "motatb"
    }

    "referenceNumberUcr" in {
      result.referenceNumberUcr mustBe "ucr"
    }

    "carrierIdentificationNumber" in {
      result.carrierIdentificationNumber mustBe "cin"
    }

    "carrierContactPerson" in {
      result.carrierContactPerson.name mustBe "ccp"
      result.carrierContactPerson.phoneNumber mustBe "ccptel"
      result.carrierContactPerson.emailAddress mustBe "ccpemail"
    }

    "seals" in {
      result.seals.sequenceNumbers mustBe "1, 2"
      result.seals.numberOfSeals mustBe "2"
      result.seals.identifiers mustBe "[sid1], [sid2]"
    }

    "locationOfGoods" in {
      result.locationOfGoods.typeOfLocation mustBe "tol"
      result.locationOfGoods.qualifierOfIdentification mustBe "qoi"
      result.locationOfGoods.authorisationNumber mustBe "an"
      result.locationOfGoods.additionalIdentifier mustBe "ai"
      result.locationOfGoods.unLocode mustBe "unl"
      result.locationOfGoods.gnssLatitude mustBe "lat"
      result.locationOfGoods.gnssLongitude mustBe "lon"
      result.locationOfGoods.economicOperator mustBe "eoin"
      result.locationOfGoods.addressStreetAndNumber mustBe "san"
      result.locationOfGoods.addressPostcode mustBe "pc"
      result.locationOfGoods.addressCity mustBe "city"
      result.locationOfGoods.addressCountry mustBe "country"
      result.locationOfGoods.postcodeAddressHouseNumber mustBe "hn"
      result.locationOfGoods.postcodeAddressPostcode mustBe "pc"
      result.locationOfGoods.postcodeAddressCountry mustBe "country"
      result.locationOfGoods.contactPerson.name mustBe "logcp"
      result.locationOfGoods.contactPerson.phoneNumber mustBe "logcptel"
      result.locationOfGoods.contactPerson.emailAddress mustBe "logcpemail"
    }

    "countriesOfRoutingOfConsignment" in {
      result.countriesOfRoutingOfConsignment.sequenceNumbers mustBe "1..."
      result.countriesOfRoutingOfConsignment.countries mustBe "corocc1..."
    }

    "activeBorderTransportMeans" in {
      result.activeBorderTransportMeans.sequenceNumbers mustBe "1..."
      result.activeBorderTransportMeans.customsOfficeAtBorderReferenceNumbers mustBe "coabrn1..."
      result.activeBorderTransportMeans.typesOfIdentification mustBe "toi1..."
      result.activeBorderTransportMeans.identificationNumbers mustBe "in1..."
      result.activeBorderTransportMeans.nationalities mustBe "nat1..."
    }

    "conveyanceReferenceNumbers" in {
      result.conveyanceReferenceNumbers mustBe "crn1..."
    }

    "placeOfLoading" in {
      result.placeOfLoading.unLocode mustBe "polunl"
      result.placeOfLoading.country mustBe "polc"
      result.placeOfLoading.location mustBe "poll"
    }

    "placeOfUnloading" in {
      result.placeOfUnloading.unLocode mustBe "pouunl"
      result.placeOfUnloading.country mustBe "pouc"
      result.placeOfUnloading.location mustBe "poul"
    }

    "transportCharges" in {
      result.transportCharges mustBe "mop..."
    }

    "descriptionOfGoods" in {
      result.descriptionOfGoods mustBe "dog..."
    }

    "dangerousGoods" in {
      result.dangerousGoods.sequenceNumbers mustBe "1..."
      result.dangerousGoods.unNumbers mustBe "unn1..."
    }
  }
}
