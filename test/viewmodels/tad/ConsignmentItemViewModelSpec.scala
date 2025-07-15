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

package viewmodels.tad

import base.SpecBase
import models.IE015.*
import viewmodels.DummyData

class ConsignmentItemViewModelSpec extends SpecBase with DummyData {

  private val lineWithSpaces = " " * 80

  "must map data to view model" - {

    val result = ConsignmentItemViewModel(cc015c, ie029HouseConsignment, ie029ConsignmentItem)

    "declarationGoodsItemNumber" in {
      result.declarationGoodsItemNumber mustEqual "1"
    }

    "goodsItemNumber" in {
      result.goodsItemNumber mustEqual "1"
    }

    "packagesType" in {
      result.packagesType mustEqual "top1/100/sm1; top2/200/sm2."
    }

    "descriptionOfGoods" - {
      "must be house consignment gross mass and description for the first item in HC" in {
        result.descriptionOfGoods mustEqual s"${ie029HouseConsignment.grossMass.toDouble.toString} / ${ie029ConsignmentItem.Commodity.descriptionOfGoods}"
      }

      "must be description of goods for the rest of the other consignment items in HC" in {
        ConsignmentItemViewModel(cc015c, ie029HouseConsignment, ie029ConsignmentItem.copy(goodsItemNumber = 2)).descriptionOfGoods mustEqual "dog"
      }
    }

    "previousDocuments" in {
      result.previousDocuments mustEqual "1/tv1/rn1/1/top1/11/muaq1/10/pcoi1; 2/tv2/rn2/2/top2/22/muaq2/20/pcoi2."
    }

    "supportingDocuments" in {
      result.supportingDocuments mustEqual "1/stv1/srn1/11/scoi1; 2/stv2/srn2/22/scoi2."
    }

    "consignee" in {
      result.consignee mustEqual "hc consignee name/san, pc, city, country."
    }

    "consigneeId" in {
      result.consigneeId mustEqual "hc consignee in."
    }

    "consignor" in {
      result.consignor mustEqual "hc consignor name/san, pc, city, country/cp/cptel."
    }

    "consignorId" in {
      result.consignorId mustEqual "hc consignor in."
    }

    "additionalReferences" in {
      result.additionalReferences mustEqual "1/artv1/arrn1; 2/artv2/arrn2."
    }

    "additionalInformation" in {
      result.additionalInformation mustEqual "1/aic1/ait1; 2/aic2/ait2."
    }

    "additionalSupplyChainActors" in {
      result.additionalSupplyChainActors mustEqual "1/role1; 2/role2; 3/role3; 4/role4."
    }

    "supplyChainActorId" in {
      result.supplyChainActorId mustEqual "id1; id2; id3; id4."
    }

    "transportDocuments" in {
      result.transportDocuments mustEqual ""
    }

    "referenceNumberUCR" in {
      result.referenceNumberUCR mustEqual "ucr"
    }

    "grossMass" in {
      result.grossMass mustEqual "200.0"
    }

    "departureTransportMeans" in {
      result.departureTransportMeans mustEqual "toi1/in1/nat1; toi2/in2/nat2."
    }

    "commodityCode" in {
      result.commodityCode mustEqual "hsshc, cnc"
    }

    "netMass" in {
      result.netMass mustEqual "100.0"
    }

    "dangerousGoods" in {
      result.dangerousGoods mustEqual "1, unn1; 2, unn2"
    }

    "cusCode" in {
      result.cusCode mustEqual "cus"
    }

    "transportCharges" in {
      result.transportCharges mustEqual ""
    }

    "declarationType" in {
      result.declarationType mustEqual "T"
    }

    "countryOfDispatch" in {
      result.countryOfDispatch mustEqual "c of dispatch"
    }

    "countryOfDestination" in {
      result.countryOfDestination mustEqual "c of destination"
    }

    "supplementaryUnits returns" - {

      "ie15 data for the matching consignment item" in {
        val searchedDeclarationGoodsItemNo = 4
        val ie15CI_1 = ConsignmentItem(
          declarationGoodsItemNumber = searchedDeclarationGoodsItemNo,
          Commodity = Commodity(GoodsMeasure = Some(GoodsMeasure(supplementaryUnits = Some(BigDecimal(4.8)))))
        )
        val ie15CI_2 = ConsignmentItem(
          declarationGoodsItemNumber = searchedDeclarationGoodsItemNo + 1,
          Commodity = Commodity(GoodsMeasure = Some(GoodsMeasure(supplementaryUnits = Some(BigDecimal(7.6)))))
        )
        val ie15CI_3 = ConsignmentItem(
          declarationGoodsItemNumber = searchedDeclarationGoodsItemNo + 1,
          Commodity = Commodity(GoodsMeasure = Some(GoodsMeasure(supplementaryUnits = Some(BigDecimal(9.5)))))
        )

        val ie15HC_1 = HouseConsignment(ConsignmentItem = Seq(ie15CI_1, ie15CI_2))
        val ie15HC_2 = HouseConsignment(ConsignmentItem = Seq(ie15CI_3))

        val viewModel = ConsignmentItemViewModel(
          cc015c.copy(Consignment = cc015c.Consignment.copy(HouseConsignment = Seq(ie15HC_1, ie15HC_2))),
          ie029HouseConsignment.copy(sequenceNumber = 1),
          ie029ConsignmentItem.copy(declarationGoodsItemNumber = searchedDeclarationGoodsItemNo)
        )

        viewModel.supplementaryUnits mustEqual "4.8"
      }

      "empty when there is no matching consignment item by declarationGoodsItemNumber" in {
        val hcSeqNo = 2
        val ie15CI = ConsignmentItem(
          declarationGoodsItemNumber = 5,
          Commodity = Commodity(GoodsMeasure = None)
        )
        val ie15HC = HouseConsignment(ConsignmentItem = Seq(ie15CI))
        val viewModel = ConsignmentItemViewModel(
          cc015c.copy(Consignment = cc015c.Consignment.copy(HouseConsignment = Seq(ie15HC))),
          ie029HouseConsignment.copy(sequenceNumber = hcSeqNo),
          ie029ConsignmentItem.copy(declarationGoodsItemNumber = 6)
        )
        viewModel.supplementaryUnits mustEqual ""
      }

      "empty when there is no GoodsMeasure" in {
        val declarationGoodsItemNo = 4
        val ie15CI = ConsignmentItem(
          declarationGoodsItemNumber = declarationGoodsItemNo,
          Commodity = Commodity(GoodsMeasure = None)
        )
        val ie15HC = HouseConsignment(ConsignmentItem = Seq(ie15CI))
        val viewModel = ConsignmentItemViewModel(
          cc015c.copy(Consignment = cc015c.Consignment.copy(HouseConsignment = Seq(ie15HC))),
          ie029HouseConsignment.copy(sequenceNumber = 2),
          ie029ConsignmentItem.copy(declarationGoodsItemNumber = declarationGoodsItemNo)
        )
        viewModel.supplementaryUnits mustEqual ""
      }

      "empty when there is no supplementaryUnits" in {
        val declarationGoodsItemNo = 4
        val ie15CI = ConsignmentItem(
          declarationGoodsItemNumber = declarationGoodsItemNo,
          Commodity = Commodity(GoodsMeasure = Some(GoodsMeasure(supplementaryUnits = None)))
        )
        val ie15HC = HouseConsignment(ConsignmentItem = Seq(ie15CI))
        val viewModel = ConsignmentItemViewModel(
          cc015c.copy(Consignment = cc015c.Consignment.copy(HouseConsignment = Seq(ie15HC))),
          ie029HouseConsignment.copy(sequenceNumber = 2),
          ie029ConsignmentItem.copy(declarationGoodsItemNumber = declarationGoodsItemNo)
        )
        viewModel.supplementaryUnits mustEqual ""
      }
    }
  }
}
