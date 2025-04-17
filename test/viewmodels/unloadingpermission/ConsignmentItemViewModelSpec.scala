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
import generated._

class ConsignmentItemViewModelSpec extends SpecBase {

  private val data: ConsignmentItemType04 = ConsignmentItemType04(
    goodsItemNumber = 1,
    declarationGoodsItemNumber = BigInt(1),
    declarationType = Some("T"),
    countryOfDestination = Some("cod"),
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
    Commodity = CUSTOM_CommodityType08(
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
          sequenceNumber = 1,
          UNNumber = "unn1"
        ),
        DangerousGoodsType01(
          sequenceNumber = 2,
          UNNumber = "unn2"
        )
      ),
      GoodsMeasure = Some(
        CUSTOM_GoodsMeasureType03(
          grossMass = Some(BigDecimal(200)),
          netMass = Some(BigDecimal(100))
        )
      )
    ),
    Packaging = Seq(
      PackagingType02(
        sequenceNumber = 1,
        typeOfPackages = "top1",
        numberOfPackages = Some(BigInt(100)),
        shippingMarks = Some("sm1")
      ),
      PackagingType02(
        sequenceNumber = 2,
        typeOfPackages = "top2",
        numberOfPackages = Some(BigInt(200)),
        shippingMarks = Some("sm2")
      )
    ),
    PreviousDocument = Seq(
      PreviousDocumentType04(
        sequenceNumber = 1,
        typeValue = "ptv1",
        referenceNumber = "prn1",
        goodsItemNumber = Some(1),
        complementOfInformation = Some("pcoi1")
      ),
      PreviousDocumentType04(
        sequenceNumber = 2,
        typeValue = "ptv2",
        referenceNumber = "prn2",
        goodsItemNumber = Some(2),
        complementOfInformation = Some("pcoi2")
      )
    ),
    SupportingDocument = Seq(
      SupportingDocumentType02(
        sequenceNumber = 1,
        typeValue = "stv1",
        referenceNumber = "srn1",
        complementOfInformation = Some("scoi1")
      ),
      SupportingDocumentType02(
        sequenceNumber = 2,
        typeValue = "stv2",
        referenceNumber = "srn2",
        complementOfInformation = Some("scoi2")
      )
    ),
    TransportDocument = Seq(
      TransportDocumentType02(
        sequenceNumber = 1,
        typeValue = "ttv1",
        referenceNumber = "trn1"
      ),
      TransportDocumentType02(
        sequenceNumber = 2,
        typeValue = "ttv2",
        referenceNumber = "trn2"
      )
    ),
    AdditionalReference = Seq(
      AdditionalReferenceType02(
        sequenceNumber = 1,
        typeValue = "artv1",
        referenceNumber = Some("arrn1")
      ),
      AdditionalReferenceType02(
        sequenceNumber = 2,
        typeValue = "artv2",
        referenceNumber = Some("arrn2")
      )
    ),
    AdditionalInformation = Seq(
      AdditionalInformationType02(
        sequenceNumber = 1,
        code = "aic1",
        text = Some("ait1")
      ),
      AdditionalInformationType02(
        sequenceNumber = 2,
        code = "aic2",
        text = Some("ait2")
      )
    )
  )

  "must map data to view model" - {

    val result = ConsignmentItemViewModel(data)

    "declarationGoodsItemNumber" in {
      result.declarationGoodsItemNumber mustBe "1"
    }

    "goodsItemNumber" in {
      result.goodsItemNumber mustBe "1"
    }

    "packaging" in {
      result.packaging mustBe "top1, 100, sm1; top2, 200, sm2"
    }

    "consignee" in {
      result.consignee mustBe "name, san, pc, city, country"
    }

    "consigneeId" in {
      result.consigneeId mustBe "in"
    }

    "udng" in {
      result.udng mustBe "1, unn1; 2, unn2"
    }

    "cusCode" in {
      result.cusCode mustBe "cus"
    }

    "descriptionOfGoods" in {
      result.descriptionOfGoods mustBe "dog"
    }

    "previousDocuments" in {
      result.previousDocuments mustBe "1, ptv1, prn1, 1, pcoi1; 2, ptv2, prn2, 2, pcoi2"
    }

    "supportingDocuments" in {
      result.supportingDocuments mustBe "1, stv1, srn1, scoi1; 2, stv2, srn2, scoi2"
    }

    "additionalReferences" in {
      result.additionalReferences mustBe "1, artv1, arrn1; 2, artv2, arrn2"
    }

    "additionalInformation" in {
      result.additionalInformation mustBe "1, aic1, ait1; 2, aic2, ait2"
    }

    "transportDocuments" in {
      result.transportDocuments mustBe "1, ttv1, trn1; 2, ttv2, trn2"
    }

    "commodityCode" in {
      result.commodityCode mustBe "hsshc, cnc"
    }

    "grossMass" in {
      result.grossMass mustBe "200.0"
    }

    "cOfDest" in {
      result.cOfDest mustBe "cod"
    }

    "netMass" in {
      result.netMass mustBe "100.0"
    }
  }
}
