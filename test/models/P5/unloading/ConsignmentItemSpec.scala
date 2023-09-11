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

package models.P5.unloading

import base.SpecBase

class ConsignmentItemSpec extends SpecBase {

  private val consignmentItem = ConsignmentItem(
    goodsItemNumber = "Goods item number",
    declarationGoodsItemNumber = 1,
    declarationType = Some("Declaration type"),
    countryOfDestination = Some("Country of destination"),
    Consignee = Some(
      Consignee(
        identificationNumber = Some("Identification number"),
        name = Some("Name"),
        Address = Some(
          Address(
            streetAndNumber = "Street and number",
            postcode = Some("Postcode"),
            city = "City",
            country = "Country"
          )
        )
      )
    ),
    Commodity = Commodity(
      descriptionOfGoods = "Description of goods",
      cusCode = Some("CUS code"),
      CommodityCode = Some(
        CommodityCode(
          harmonizedSystemSubHeadingCode = "Harmonized system sub-heading code",
          combinedNomenclatureCode = Some("Combined nomenclature code")
        )
      ),
      DangerousGoods = Some(
        Seq(
          DangerousGoods("UN number 1"),
          DangerousGoods("UN number 2")
        )
      ),
      GoodsMeasure = Some(
        GoodsMeasure(
          grossMass = BigDecimal(123),
          netMass = Some(BigDecimal(456))
        )
      )
    ),
    Packaging = Seq(
      Packaging(
        typeOfPackages = "Type of packages 1",
        numberOfPackages = Some(1),
        shippingMarks = Some("Shipping marks 1")
      ),
      Packaging(
        typeOfPackages = "Type of packages 2",
        numberOfPackages = Some(2),
        shippingMarks = Some("Shipping marks 2")
      )
    ),
    PreviousDocument = Some(
      Seq(
        PreviousDocument(
          `type` = "Type 1 (Previous)",
          referenceNumber = "Reference number 1 (Previous)",
          goodsItemNumber = Some("Goods item number 1 (Previous)"),
          complementOfInformation = Some("Complement of information 1 (Previous)")
        ),
        PreviousDocument(
          `type` = "Type 2 (Previous)",
          referenceNumber = "Reference number 2 (Previous)",
          goodsItemNumber = Some("Goods item number 2 (Previous)"),
          complementOfInformation = Some("Complement of information 2 (Previous)")
        )
      )
    ),
    SupportingDocument = Some(
      Seq(
        SupportingDocument(
          `type` = "Type 1 (Supporting)",
          referenceNumber = "Reference number 1 (Supporting)",
          complementOfInformation = Some("Complement of information 1 (Supporting)")
        ),
        SupportingDocument(
          `type` = "Type 2 (Supporting)",
          referenceNumber = "Reference number 2 (Supporting)",
          complementOfInformation = Some("Complement of information 2 (Supporting)")
        )
      )
    ),
    TransportDocument = Some(
      Seq(
        TransportDocument(
          `type` = "Type 1 (Transport)",
          referenceNumber = "Reference number 1 (Transport)"
        ),
        TransportDocument(
          `type` = "Type 2 (Transport)",
          referenceNumber = "Reference number 2 (Transport)"
        )
      )
    ),
    AdditionalReference = Some(
      Seq(
        AdditionalReference(
          `type` = "Additional reference 1",
          referenceNumber = Some("Reference number 1")
        ),
        AdditionalReference(
          `type` = "Additional reference 2",
          referenceNumber = Some("Reference number 2")
        )
      )
    ),
    AdditionalInformation = Some(
      Seq(
        AdditionalInformation(
          code = "Code 1",
          text = Some("Text 1")
        ),
        AdditionalInformation(
          code = "Code 2",
          text = Some("Text 2")
        )
      )
    )
  )

  "must correctly render values as strings" - {

    "when total packages" in {
      consignmentItem.totalPackages mustBe 3
    }

    "when packaging" in {
      consignmentItem.packaging mustBe "Type of packages 1, 1, Shipping marks 1; Type of packages 2, 2, Shipping marks 2"
    }

    "when consignee" in {
      consignmentItem.consignee mustBe "Name, Street and number, Postcode, City, Country"
    }

    "when consignee ID" in {
      consignmentItem.consigneeId mustBe "Identification number"
    }

    "when UDNG" in {
      consignmentItem.udng mustBe "UN number 1; UN number 2"
    }

    "when CUS code" in {
      consignmentItem.cusCode mustBe "CUS code"
    }

    "when previous documents" in {
      consignmentItem.previousDocuments mustBe "Type 1 (Previous), Reference number 1 (Previous), Goods item number 1 (Previous), Complement of information 1 (Previous); Type 2 (Previous), Reference number 2 (Previous), Goods item number 2 (Previous), Complement of information 2 (Previous)"
    }

    "when supporting documents" in {
      consignmentItem.supportingDocuments mustBe "Type 1 (Supporting), Reference number 1 (Supporting), Complement of information 1 (Supporting); Type 2 (Supporting), Reference number 2 (Supporting), Complement of information 2 (Supporting)"
    }

    "when transport documents" in {
      consignmentItem.transportDocuments mustBe "Type 1 (Transport), Reference number 1 (Transport); Type 2 (Transport), Reference number 2 (Transport)"
    }

    "when additional references" in {
      consignmentItem.additionalReferences mustBe "Additional reference 1, Reference number 1; Additional reference 2, Reference number 2"
    }

    "when additional information" in {
      consignmentItem.additionalInformation mustBe "Code 1, Text 1; Code 2, Text 2"
    }

    "when commodity code" in {
      consignmentItem.commodityCode mustBe "Harmonized system sub-heading code, Combined nomenclature code"
    }

    "when gross mass" in {
      consignmentItem.grossMass mustBe "123"
    }

    "when net mass" in {
      consignmentItem.netMass mustBe "456"
    }

    "when country of destination" in {
      consignmentItem.cOfDest mustBe "Country of destination"
    }
  }
}
