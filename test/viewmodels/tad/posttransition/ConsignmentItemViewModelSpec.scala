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

package viewmodels.tad.posttransition

import base.SpecBase
import generated.{CommodityType07, ConsignmentItemType09, GoodsMeasureType02, HouseConsignmentType10}
import viewmodels.DummyData

class ConsignmentItemViewModelSpec extends SpecBase with DummyData {

  private val lineWithSpaces = " " * 80

  "must map data to view model" - {

    val result = ConsignmentItemViewModel(cc015c, houseConsignmentType03, consignmentItemType03)

    "declarationGoodsItemNumber" in {
      result.declarationGoodsItemNumber mustBe "1"
    }

    "goodsItemNumber" in {
      result.goodsItemNumber mustBe "1"
    }

    "packagesType" in {
      result.packagesType mustBe "top1/100/sm1; top2/200/sm2."
    }

    "descriptionOfGoods" - {
      "must be house consignment gross mass and description for the first item in HC" in {
        result.descriptionOfGoods mustBe s"${houseConsignmentType03.grossMass.toDouble.toString} / ${consignmentItemType03.Commodity.descriptionOfGoods}"
      }

      "must be description of goods for the rest of the other consignment items in HC" in {
        ConsignmentItemViewModel(cc015c, houseConsignmentType03, consignmentItemType03.copy(goodsItemNumber = 2)).descriptionOfGoods mustBe "dog"
      }
    }

    "previousDocuments" in {
      result.previousDocuments mustBe "1/tv1/rn1/1/top1/11/muaq1/10/pcoi1; 2/tv2/rn2/2/top2/22/muaq2/20/pcoi2."
    }

    "supportingDocuments" in {
      result.supportingDocuments mustBe "1/stv1/srn1/11/scoi1; 2/stv2/srn2/22/scoi2."
    }

    "consignee" in {
      result.consignee mustBe "hc consignee name/san, pc, city, country."
    }

    "consigneeId" in {
      result.consigneeId mustBe "hc consignee in."
    }

    "consignor" in {
      result.consignor mustBe "hc consignor name/san, pc, city, country/cp/cptel."
    }

    "consignorId" in {
      result.consignorId mustBe "hc consignor in."
    }

    "additionalReferences" in {
      result.additionalReferences mustBe "1/artv1/arrn1; 2/artv2/arrn2."
    }

    "additionalInformation" in {
      result.additionalInformation mustBe "1/aic1/ait1; 2/aic2/ait2."
    }

    "additionalSupplyChainActors" in {
      result.additionalSupplyChainActors mustBe "1/role1; 2/role2; 3/role3; 4/role4."
    }

    "supplyChainActorId" in {
      result.supplyChainActorId mustBe "id1; id2; id3; id4."
    }

    "transportDocuments" in {
      result.transportDocuments mustBe "1/ttv1/trn1; 2/ttv2/trn2."
    }

    "referenceNumberUCR" in {
      result.referenceNumberUCR mustBe "ucr"
    }

    "grossMass" in {
      result.grossMass mustBe "200.0"
    }

    "departureTransportMeans" in {
      result.departureTransportMeans mustBe "toi1/in1/nat1; toi2/in2/nat2."
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

    "supplementaryUnits returns" - {

      "ie15 data for the matching consignment item" in {
        val searchedDeclarationGoodsItemNo = 4
        val ie15CI_1 = ConsignmentItemType09(
          goodsItemNumber = 1,
          declarationGoodsItemNumber = searchedDeclarationGoodsItemNo,
          Commodity = CommodityType07(descriptionOfGoods = "description", GoodsMeasure = Some(GoodsMeasureType02(supplementaryUnits = Some(BigDecimal(4.8)))))
        )
        val ie15CI_2 = ConsignmentItemType09(
          goodsItemNumber = 2,
          declarationGoodsItemNumber = searchedDeclarationGoodsItemNo + 1,
          Commodity = CommodityType07(descriptionOfGoods = "description", GoodsMeasure = Some(GoodsMeasureType02(supplementaryUnits = Some(BigDecimal(7.6)))))
        )
        val ie15CI_3 = ConsignmentItemType09(
          goodsItemNumber = 1,
          declarationGoodsItemNumber = searchedDeclarationGoodsItemNo + 1,
          Commodity = CommodityType07(descriptionOfGoods = "description", GoodsMeasure = Some(GoodsMeasureType02(supplementaryUnits = Some(BigDecimal(9.5)))))
        )

        val ie15HC_1 = HouseConsignmentType10(sequenceNumber = 1, grossMass = BigDecimal(2.1), ConsignmentItem = Seq(ie15CI_1, ie15CI_2))
        val ie15HC_2 = HouseConsignmentType10(sequenceNumber = 2, grossMass = BigDecimal(2.1), ConsignmentItem = Seq(ie15CI_3))

        val viewModel = ConsignmentItemViewModel(
          cc015c.copy(Consignment = cc015c.Consignment.copy(HouseConsignment = Seq(ie15HC_1, ie15HC_2))),
          houseConsignmentType03.copy(sequenceNumber = 1),
          consignmentItemType03.copy(declarationGoodsItemNumber = searchedDeclarationGoodsItemNo)
        )

        viewModel.supplementaryUnits mustBe "4.8"
      }

      "empty when there is no matching consignment item by declarationGoodsItemNumber" in {
        val hcSeqNo = 2
        val ie15CI = ConsignmentItemType09(goodsItemNumber = 4, declarationGoodsItemNumber = 5, Commodity = CommodityType07(descriptionOfGoods = "description"))
        val ie15HC = HouseConsignmentType10(sequenceNumber = hcSeqNo, grossMass = BigDecimal(2.1), ConsignmentItem = Seq(ie15CI))
        val viewModel = ConsignmentItemViewModel(
          cc015c.copy(Consignment = cc015c.Consignment.copy(HouseConsignment = Seq(ie15HC))),
          houseConsignmentType03.copy(sequenceNumber = hcSeqNo),
          consignmentItemType03.copy(declarationGoodsItemNumber = 6)
        )
        viewModel.supplementaryUnits mustBe ""
      }

      "empty when there is no GoodsMeasure" in {
        val declarationGoodsItemNo = 4
        val ie15CI = ConsignmentItemType09(goodsItemNumber = 1,
                                           declarationGoodsItemNumber = declarationGoodsItemNo,
                                           Commodity = CommodityType07(descriptionOfGoods = "description", GoodsMeasure = None)
        )
        val ie15HC = HouseConsignmentType10(sequenceNumber = 2, grossMass = BigDecimal(2.1), ConsignmentItem = Seq(ie15CI))
        val viewModel = ConsignmentItemViewModel(
          cc015c.copy(Consignment = cc015c.Consignment.copy(HouseConsignment = Seq(ie15HC))),
          houseConsignmentType03.copy(sequenceNumber = 2),
          consignmentItemType03.copy(declarationGoodsItemNumber = declarationGoodsItemNo)
        )
        viewModel.supplementaryUnits mustBe ""
      }

      "empty when there is no supplementaryUnits" in {
        val declarationGoodsItemNo = 4
        val ie15CI = ConsignmentItemType09(
          goodsItemNumber = 1,
          declarationGoodsItemNumber = declarationGoodsItemNo,
          Commodity = CommodityType07(descriptionOfGoods = "description", GoodsMeasure = Some(GoodsMeasureType02(supplementaryUnits = None)))
        )
        val ie15HC = HouseConsignmentType10(sequenceNumber = 2, grossMass = BigDecimal(2.1), ConsignmentItem = Seq(ie15CI))
        val viewModel = ConsignmentItemViewModel(
          cc015c.copy(Consignment = cc015c.Consignment.copy(HouseConsignment = Seq(ie15HC))),
          houseConsignmentType03.copy(sequenceNumber = 2),
          consignmentItemType03.copy(declarationGoodsItemNumber = declarationGoodsItemNo)
        )
        viewModel.supplementaryUnits mustBe ""
      }
    }
  }
}
