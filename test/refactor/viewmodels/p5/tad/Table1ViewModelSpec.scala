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

package refactor.viewmodels.p5.tad

import base.SpecBase
import refactor.viewmodels.DummyData

class Table1ViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = Table1ViewModel(cc029c)

    "additionalDeclarationType" in {
      result.additionalDeclarationType mustBe "adt"
    }

    "consignees" in {
      result.consignees mustBe "name, san, pc, city, country"
    }

    "consigneeIdentificationNumbers" in {
      result.consigneeIdentificationNumbers mustBe "in"
    }

    "consignors" in {
      result.consignors mustBe "name, san, pc, city, country"
    }

    "consignorIdentificationNumbers" in {
      result.consignorIdentificationNumbers mustBe "in"
    }

    "consignorContactPersons" in {
      result.consignorContactPersons mustBe "ccp, ccptel, ccpemail"
    }

    "declarationType" in {
      result.declarationType mustBe "T"
    }

    "holderOfTransitProcedure" in {
      result.holderOfTransitProcedure mustBe "thin, hn, san, pc, city, country"
    }

    "holderOfTransitProcedureIdentificationNumber" in {
      result.holderOfTransitProcedureIdentificationNumber mustBe "hin"
    }

    "representative" in {
      result.representative mustBe "rstatus"
    }

    "lrn" in {
      result.lrn mustBe "lrn"
    }

    "carrierIdentificationNumber" in {
      result.carrierIdentificationNumber mustBe "cin"
    }

    "additionalSupplyChainActorRoles" in {
      result.additionalSupplyChainActorRoles mustBe "role1; role2"
    }

    "additionalSupplyChainActorIdentificationNumbers" in {
      result.additionalSupplyChainActorIdentificationNumbers mustBe "id1; id2"
    }

    "departureTransportMeans" in {
      result.departureTransportMeans mustBe "toi1, in1, nat1; toi2, in2, nat2; toi3, in3, na..."
    }

    "activeBorderTransportMeans" in {
      result.activeBorderTransportMeans mustBe "coabrn1, toi1, in1, nat1; coabrn2, toi2, in2, nat2"
    }

    "activeBorderTransportMeansConveyanceNumbers" in {
      result.activeBorderTransportMeansConveyanceNumbers mustBe "crn1; crn2"
    }

    "placeOfLoading" in {
      result.placeOfLoading mustBe "polunl, polc, poll"
    }

    "placeOfUnloading" in {
      result.placeOfUnloading mustBe "pouunl, pouc, poul"
    }

    "modeOfTransportAtBorder" in {
      result.modeOfTransportAtBorder mustBe "motatb"
    }

    "inlandModeOfTransport" in {
      result.inlandModeOfTransport mustBe "imot"
    }

    "locationOfGoods" in {
      result.locationOfGoods mustBe "tol, qoi, an, unl, corn, lat, lon, eoin, san, pc, city, country, hn, pc, country"
    }

    "locationOfGoodsContactPerson" in {
      result.locationOfGoodsContactPerson mustBe "logcp, logcptel, logcpemail"
    }

    "security" in {
      result.security mustBe "security"
    }

    "tir" in {
      result.tir mustBe "tir"
    }

    "totalGrossMass" in {
      result.totalGrossMass mustBe "200.0"
    }

    "specificCircumstanceIndicator" in {
      result.specificCircumstanceIndicator mustBe "sci"
    }

    "totalItems" in {
      result.totalItems mustBe "2"
    }

    "totalPackages" in {
      result.totalPackages mustBe "66"
    }

    "ucr" in {
      result.ucr mustBe "ucr"
    }
  }
}
