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
import viewmodels.DummyData

class Table1ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = Table1ViewModel(cc043c)

    "consignorIdentificationNumber" in {
      result.consignorIdentificationNumber mustEqual "in"
    }

    "consignor" in {
      result.consignor mustEqual "name, san, pc, city, country"
    }

    "consigneeIdentificationNumber" in {
      result.consigneeIdentificationNumber mustEqual "in"
    }

    "consignee" in {
      result.consignee mustEqual "name, san, pc, city, country"
    }

    "holderOfTransitID" in {
      result.holderOfTransitID mustEqual "hin"
    }

    "holderOfTransit" in {
      result.holderOfTransit mustEqual "hn, san, pc, city, country"
    }

    "declarationType" in {
      result.declarationType mustEqual "T"
    }

    "totalItems" in {
      result.totalItems mustEqual "2"
    }

    "totalPackages" in {
      result.totalPackages mustEqual "0"
    }

    "totalGrossMass" in {
      result.totalGrossMass mustEqual "200"
    }

    "tir" in {
      result.tir mustEqual "thin"
    }

    "security" in {
      result.security mustEqual "0"
    }

    "inlandModeOfTransport" in {
      result.inlandModeOfTransport mustEqual "imot"
    }

    "departureTransportMeans" in {
      result.departureTransportMeans.head mustEqual new DepartureTransportMeans(1, Some("toi1"), Some("in1"), Some("nat1"))
      result.departureTransportMeans(1) mustEqual new DepartureTransportMeans(2, Some("toi2"), Some("in2"), Some("nat2"))
    }

    "container" in {
      result.container mustEqual "1"
    }

    "transportEquipment" in {
      result.transportEquipment mustEqual "1, cin1, 2, 1:1...2:3"
    }

    "seals" in {
      result.seals mustEqual "1,[sid1]; 2,[sid2]"
    }

    "previousDocument" in {
      result.previousDocument mustEqual "1, ptv1, prn1, pcoi1; 2, ptv2, prn2, pcoi1"
    }

    "transportDocument" in {
      result.transportDocument mustEqual "1, ttv1, trn1; 2, ttv2, trn2"
    }

    "supportingDocument" in {
      result.supportingDocument mustEqual "1, stv1, srn1, scoi1; 2, stv2, srn2, scoi1"
    }

    "additionalReference" in {
      result.additionalReference mustEqual "1, artv1, arrn1; 2, artv2, arrn2"
    }

    "additionalInformation" in {
      result.additionalInformation mustEqual "1, aic1, ait1; 2, aic2, ait2"
    }

    "customsOfficeOfDestination" in {
      result.customsOfficeOfDestination mustEqual "cooda"
    }

    "countryOfDestination" in {
      result.countryOfDestination mustEqual "cod"
    }
  }
}
