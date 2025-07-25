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

class ConsignmentItemViewModelSpec extends SpecBase {

  private val data: ConsignmentItem = new ConsignmentItem(
    goodsItemNumber = 1,
    declarationGoodsItemNumber = BigInt(1),
    declarationType = Some("T"),
    countryOfDestination = Some("cod"),
    Consignee = Some(
      new ConsignmentItemConsignee(
        identificationNumber = Some("in"),
        name = Some("name"),
        Address = Some(
          new ConsignmentItemConsigneeAddress(
            streetAndNumber = "san",
            postcode = Some("pc"),
            city = "city",
            country = "country"
          )
        )
      )
    ),
    Commodity = new Commodity(
      descriptionOfGoods = "dog",
      cusCode = Some("cus"),
      CommodityCode = Some(
        new CommodityCode(
          harmonizedSystemSubHeadingCode = "hsshc",
          combinedNomenclatureCode = Some("cnc")
        )
      ),
      DangerousGoods = Seq(
        new DangerousGoods(
          sequenceNumber = 1,
          UNNumber = "unn1"
        ),
        new DangerousGoods(
          sequenceNumber = 2,
          UNNumber = "unn2"
        )
      ),
      GoodsMeasure = Some(
        new GoodsMeasure(
          grossMass = Some(BigDecimal(200)),
          netMass = Some(BigDecimal(100))
        )
      )
    ),
    Packaging = Seq(
      new Packaging(
        sequenceNumber = 1,
        typeOfPackages = "top1",
        numberOfPackages = Some(BigInt(100)),
        shippingMarks = Some("sm1")
      ),
      new Packaging(
        sequenceNumber = 2,
        typeOfPackages = "top2",
        numberOfPackages = Some(BigInt(200)),
        shippingMarks = Some("sm2")
      )
    ),
    PreviousDocument = Seq(
      new ConsignmentItemPreviousDocument(
        sequenceNumber = 1,
        typeValue = "ptv1",
        referenceNumber = "prn1",
        goodsItemNumber = Some(1),
        complementOfInformation = Some("pcoi1")
      ),
      new ConsignmentItemPreviousDocument(
        sequenceNumber = 2,
        typeValue = "ptv2",
        referenceNumber = "prn2",
        goodsItemNumber = Some(2),
        complementOfInformation = Some("pcoi2")
      )
    ),
    SupportingDocument = Seq(
      new SupportingDocument(
        sequenceNumber = 1,
        typeValue = "stv1",
        referenceNumber = "srn1",
        complementOfInformation = Some("scoi1")
      ),
      new SupportingDocument(
        sequenceNumber = 2,
        typeValue = "stv2",
        referenceNumber = "srn2",
        complementOfInformation = Some("scoi2")
      )
    ),
    TransportDocument = Seq(
      new TransportDocument(
        sequenceNumber = 1,
        typeValue = "ttv1",
        referenceNumber = "trn1"
      ),
      new TransportDocument(
        sequenceNumber = 2,
        typeValue = "ttv2",
        referenceNumber = "trn2"
      )
    ),
    AdditionalReference = Seq(
      new ConsignmentItemAdditionalReference(
        sequenceNumber = 1,
        typeValue = "artv1",
        referenceNumber = Some("arrn1")
      ),
      new ConsignmentItemAdditionalReference(
        sequenceNumber = 2,
        typeValue = "artv2",
        referenceNumber = Some("arrn2")
      )
    ),
    AdditionalInformation = Seq(
      new AdditionalInformation(
        sequenceNumber = 1,
        code = "aic1",
        text = Some("ait1")
      ),
      new AdditionalInformation(
        sequenceNumber = 2,
        code = "aic2",
        text = Some("ait2")
      )
    )
  )

  "must map data to view model" - {

    val result = ConsignmentItemViewModel(data)

    "declarationGoodsItemNumber" in {
      result.declarationGoodsItemNumber mustEqual "1"
    }

    "goodsItemNumber" in {
      result.goodsItemNumber mustEqual "1"
    }

    "packaging" in {
      result.packaging mustEqual "top1, 100, sm1; top2, 200, sm2"
    }

    "consignee" in {
      result.consignee mustEqual "name, san, pc, city, country"
    }

    "consigneeId" in {
      result.consigneeId mustEqual "in"
    }

    "udng" in {
      result.udng mustEqual "1, unn1; 2, unn2"
    }

    "cusCode" in {
      result.cusCode mustEqual "cus"
    }

    "descriptionOfGoods" in {
      result.descriptionOfGoods mustEqual "dog"
    }

    "previousDocuments" in {
      result.previousDocuments mustEqual "1, ptv1, prn1, 1, pcoi1; 2, ptv2, prn2, 2, pcoi2"
    }

    "supportingDocuments" in {
      result.supportingDocuments mustEqual "1, stv1, srn1, scoi1; 2, stv2, srn2, scoi2"
    }

    "additionalReferences" in {
      result.additionalReferences mustEqual "1, artv1, arrn1; 2, artv2, arrn2"
    }

    "additionalInformation" in {
      result.additionalInformation mustEqual "1, aic1, ait1; 2, aic2, ait2"
    }

    "transportDocuments" in {
      result.transportDocuments mustEqual "1, ttv1, trn1; 2, ttv2, trn2"
    }

    "commodityCode" in {
      result.commodityCode mustEqual "hsshc, cnc"
    }

    "grossMass" in {
      result.grossMass mustEqual "200.0"
    }

    "cOfDest" in {
      result.cOfDest mustEqual "cod"
    }

    "netMass" in {
      result.netMass mustEqual "100.0"
    }
  }
}
