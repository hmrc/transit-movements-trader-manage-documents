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

package viewmodels.unloadingpermission

import base.SpecBase
import generated.rfc37.DepartureTransportMeansType02
import viewmodels.DummyData

class Table1ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = Table1ViewModel(cc043c)

    "consignorIdentificationNumber" in {
      result.consignorIdentificationNumber mustBe "in"
    }

    "consignor" in {
      result.consignor mustBe "name, san, pc, city, country"
    }

    "consigneeIdentificationNumber" in {
      result.consigneeIdentificationNumber mustBe "in"
    }

    "consignee" in {
      result.consignee mustBe "name, san, pc, city, country"
    }

    "holderOfTransitID" in {
      result.holderOfTransitID mustBe "hin"
    }

    "holderOfTransit" in {
      result.holderOfTransit mustBe "hn, san, pc, city, country"
    }

    "declarationType" in {
      result.declarationType mustBe "T"
    }

    "totalItems" in {
      result.totalItems mustBe "2"
    }

    "totalPackages" in {
      result.totalPackages mustBe "0"
    }

    "totalGrossMass" in {
      result.totalGrossMass mustBe "200"
    }

    "tir" in {
      result.tir mustBe "thin"
    }

    "security" in {
      result.security mustBe "0"
    }

    "inlandModeOfTransport" in {
      result.inlandModeOfTransport mustBe "imot"
    }

    "departureTransportMeans" in {
      result.departureTransportMeans.head mustBe DepartureTransportMeansType02("1", Some("toi1"), Some("in1"), Some("nat1"))
      result.departureTransportMeans(1) mustBe DepartureTransportMeansType02("2", Some("toi2"), Some("in2"), Some("nat2"))
    }

    "container" in {
      result.container mustBe "1"
    }

    "transportEquipment" in {
      result.transportEquipment mustBe "1, cin1, 2, 1:1...2:3"
    }

    "seals" in {
      result.seals mustBe "1,[sid1]; 2,[sid2]"
    }

    "previousDocument" in {
      result.previousDocument mustBe "1, ptv1, prn1, pcoi1; 2, ptv2, prn2, pcoi1"
    }

    "transportDocument" in {
      result.transportDocument mustBe "1, ttv1, trn1; 2, ttv2, trn2"
    }

    "supportingDocument" in {
      result.supportingDocument mustBe "1, stv1, srn1, scoi1; 2, stv2, srn2, scoi1"
    }

    "additionalReference" in {
      result.additionalReference mustBe "1, artv1, arrn1; 2, artv2, arrn2"
    }

    "additionalInformation" in {
      result.additionalInformation mustBe "1, aic1, ait1; 2, aic2, ait2"
    }

    "customsOfficeOfDestination" in {
      result.customsOfficeOfDestination mustBe "cooda"
    }

    "countryOfDestination" in {
      result.countryOfDestination mustBe "cod"
    }
  }
}
