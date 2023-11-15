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
import generated.p5._

class ConsignmentItemViewModelSpec extends SpecBase {

  private val data: ConsignmentItemType03 = ConsignmentItemType03(
    goodsItemNumber = "gin",
    declarationGoodsItemNumber = BigInt(1),
    declarationType = Some("T"),
    countryOfDispatch = Some("c of dispatch"),
    countryOfDestination = Some("c of destination"),
    referenceNumberUCR = Some("ucr"),
    Consignee = Some(
      ConsigneeType03(
        identificationNumber = Some("in"),
        name = Some("name"),
        Address = Some(
          AddressType09(
            streetAndNumber = "san",
            postcode = Some("pc"),
            city = "city",
            country = "country"
          )
        )
      )
    ),
    AdditionalSupplyChainActor = Seq(
      AdditionalSupplyChainActorType(
        sequenceNumber = "1",
        role = "role1",
        identificationNumber = "id1"
      ),
      AdditionalSupplyChainActorType(
        sequenceNumber = "2",
        role = "role2",
        identificationNumber = "id2"
      )
    ),
    Commodity = CommodityType08(
      descriptionOfGoods = "dog",
      cusCode = Some("cus"),
      CommodityCode = Some(
        CommodityCodeType05(
          harmonizedSystemSubHeadingCode = "hsshc",
          combinedNomenclatureCode = Some("cnc")
        )
      ),
      DangerousGoods = Seq(
        DangerousGoodsType01(
          sequenceNumber = "1",
          UNNumber = "unn1"
        ),
        DangerousGoodsType01(
          sequenceNumber = "2",
          UNNumber = "unn2"
        )
      ),
      GoodsMeasure = Some(
        GoodsMeasureType03(
          grossMass = BigDecimal(200),
          netMass = Some(BigDecimal(100))
        )
      )
    ),
    Packaging = Seq(
      PackagingType02(
        sequenceNumber = "1",
        typeOfPackages = "top1",
        numberOfPackages = Some(BigInt(100)),
        shippingMarks = Some("sm1")
      ),
      PackagingType02(
        sequenceNumber = "2",
        typeOfPackages = "top2",
        numberOfPackages = Some(BigInt(200)),
        shippingMarks = Some("sm2")
      )
    ),
    PreviousDocument = Seq(
      PreviousDocumentType03(
        sequenceNumber = "1",
        typeValue = "tv1",
        referenceNumber = "rn1",
        goodsItemNumber = Some(BigInt(1)),
        typeOfPackages = Some("top1"),
        numberOfPackages = Some(BigInt(11)),
        measurementUnitAndQualifier = Some("muaq1"),
        quantity = Some(BigDecimal(10)),
        complementOfInformation = Some("pcoi1")
      ),
      PreviousDocumentType03(
        sequenceNumber = "2",
        typeValue = "tv2",
        referenceNumber = "rn2",
        goodsItemNumber = Some(BigInt(2)),
        typeOfPackages = Some("top2"),
        numberOfPackages = Some(BigInt(22)),
        measurementUnitAndQualifier = Some("muaq2"),
        quantity = Some(BigDecimal(20)),
        complementOfInformation = Some("pcoi2")
      )
    ),
    SupportingDocument = Seq(
      SupportingDocumentType06(
        sequenceNumber = "1",
        typeValue = "stv1",
        referenceNumber = "srn1",
        documentLineItemNumber = Some(BigInt(11)),
        complementOfInformation = Some("scoi1")
      ),
      SupportingDocumentType06(
        sequenceNumber = "2",
        typeValue = "stv2",
        referenceNumber = "srn2",
        documentLineItemNumber = Some(BigInt(22)),
        complementOfInformation = Some("scoi2")
      )
    ),
    TransportDocument = Seq(
      TransportDocumentType02(
        sequenceNumber = "1",
        typeValue = "ttv1",
        referenceNumber = "trn1"
      ),
      TransportDocumentType02(
        sequenceNumber = "2",
        typeValue = "ttv2",
        referenceNumber = "trn2"
      )
    ),
    AdditionalReference = Seq(
      AdditionalReferenceType02(
        sequenceNumber = "1",
        typeValue = "artv1",
        referenceNumber = Some("arrn1")
      ),
      AdditionalReferenceType02(
        sequenceNumber = "2",
        typeValue = "artv2",
        referenceNumber = Some("arrn2")
      )
    ),
    AdditionalInformation = Seq(
      AdditionalInformationType02(
        sequenceNumber = "1",
        code = "aic1",
        text = Some("ait1")
      ),
      AdditionalInformationType02(
        sequenceNumber = "2",
        code = "aic2",
        text = Some("ait2")
      )
    ),
    TransportCharges = Some(
      TransportChargesType(
        methodOfPayment = "mop"
      )
    )
  )

  "must map data to view model" - {

    val result = ConsignmentItemViewModel(data)

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
      result.consignee mustBe "name, san, pc, city, country"
    }

    "consigneeId" in {
      result.consigneeId mustBe "in"
    }

    "additionalReferences" in {
      result.additionalReferences mustBe "1, artv1, arrn1; 2, artv2, arrn2"
    }

    "additionalInformation" in {
      result.additionalInformation mustBe "1, aic1, ait1; 2, aic2, ait2"
    }

    "additionalSupplyChainActors" in {
      result.additionalSupplyChainActors mustBe "role1; role2"
    }

    "supplyChainActorId" in {
      result.supplyChainActorId mustBe "id1; id2"
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
