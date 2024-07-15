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

class ConsignmentItemViewModelSpec extends SpecBase with DummyData {

  "must map data to view model" - {

    val result = ConsignmentItemViewModel(houseConsignmentType03, consignmentItemType03)

    "declarationGoodsItemNumber" in {
      result.declarationGoodsItemNumber mustBe "1"
    }

    "goodsItemNumber" in {
      result.goodsItemNumber mustBe "gin"
    }

    "packagesType" in {
      result.packagesType mustBe "top1, 100, sm1; top2, 200, sm2"
    }

    "descriptionOfGoods" in {
      result.descriptionOfGoods mustBe "dog"
    }

    "previousDocuments" in {
      result.previousDocuments mustBe "1, tv1, rn1, 1, top1, 11, muaq1, 10, pcoi1; 2, tv2, rn2, 2, top2, 22, muaq2, 20, pcoi2"
    }

    "supportingDocuments" in {
      result.supportingDocuments mustBe "1, stv1, srn1, 11, scoi1; 2, stv2, srn2, 22, scoi2"
    }

    "consignee" in {
      result.consignee mustBe "hc consignee name, san, pc, city, country"
    }

    "consigneeId" in {
      result.consigneeId mustBe "hc consignee in"
    }

    "consignor" in {
      result.consignor mustBe "hc consignor name, san, pc, city, country, cp, cptel, cpemail"
    }

    "consignorId" in {
      result.consignorId mustBe "hc consignor in"
    }

    "additionalReferences" in {
      result.additionalReferences mustBe "1, artv1, arrn1; 2, artv2, arrn2"
    }

    "additionalInformation" in {
      result.additionalInformation mustBe "1, aic1, ait1; 2, aic2, ait2"
    }

    "additionalSupplyChainActors" in {
      result.additionalSupplyChainActors mustBe "role1; role2; role3..."
    }

    "supplyChainActorId" in {
      result.supplyChainActorId mustBe "id1; id2; id3..."
    }

    "transportDocuments" in {
      result.transportDocuments mustBe "1, ttv1, trn1; 2, ttv2, trn2"
    }

    "referenceNumberUCR" in {
      result.referenceNumberUCR mustBe "ucr"
    }

    "grossMass" in {
      result.grossMass mustBe "200.0"
    }

    "departureTransportMeans" in {
      result.departureTransportMeans mustBe "toi1, in1, nat1; toi2, in2, nat2"
    }

    "commodityCode" in {
      result.commodityCode mustBe "hsshc, cnc"
    }

    "netMass" in {
      result.netMass mustBe "100.0"
    }

    "dangerousGoods" in {
      result.dangerousGoods mustBe "1, unn1; 2, unn2"
    }

    "cusCode" in {
      result.cusCode mustBe "cus"
    }

    "transportCharges" in {
      result.transportCharges mustBe "mop"
    }

    "declarationType" in {
      result.declarationType mustBe "T"
    }

    "countryOfDispatch" in {
      result.countryOfDispatch mustBe "c of dispatch"
    }

    "countryOfDestination" in {
      result.countryOfDestination mustBe "c of destination"
    }
  }
}
