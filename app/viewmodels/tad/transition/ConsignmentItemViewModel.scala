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

package viewmodels.tad.transition

import generated.{CC029CType, CUSTOM_ConsignmentItemType03, ConsigneeType03, ConsignmentItemType03}
import viewmodels.*
import viewmodels.tad.transition.ConsignmentItemViewModel.*

case class ConsignmentItemViewModel(
  itemNumber: String,
  shippingMarks: Seq[String],
  packages: Seq[String],
  containers: Seq[String],
  description: String,
  declarationType: String,
  commodityCode: String,
  sensitiveGoodsInformation: Seq[SensitiveGoodsInformationViewModel],
  previousDocuments: Seq[String],
  countryOfDispatch: String,
  countryOfDestination: String,
  grossMass: String,
  netMass: String,
  producedDocuments: Option[Seq[String]],
  consigneeViewModel: Option[ConsigneeViewModel],
  consignorViewModel: Option[ConsignorViewModel],
  supportingDocuments: Seq[String],
  transportDocuments: Seq[String],
  additionalInformation: Seq[String],
  additionalReference: Seq[String]
)

object ConsignmentItemViewModel {

  case class ConsigneeViewModel(
    name: String,
    eori: String,
    streetAndNumber: String,
    postcode: String,
    city: String,
    country: String
  )

  object ConsigneeViewModel {

    def apply(consignee: ConsigneeType03): ConsigneeViewModel =
      new ConsigneeViewModel(
        name = consignee.name.orElseBlank,
        eori = consignee.identificationNumber.orElse3Dashes,
        streetAndNumber = consignee.Address.map(_.streetAndNumber).orElseBlank,
        postcode = consignee.Address.flatMap(_.postcode).orElseBlank,
        city = consignee.Address.map(_.city).orElseBlank,
        country = consignee.Address.map(_.country).orElseBlank
      )
  }

  case class ConsignorViewModel(
    name: String,
    eori: String,
    streetAndNumber: String,
    postcode: String,
    city: String,
    country: String
  )

  case class SensitiveGoodsInformationViewModel(
    goodsCode: String,
    quantity: String
  )

  object SensitiveGoodsInformationViewModel {

    def apply(): SensitiveGoodsInformationViewModel =
      new SensitiveGoodsInformationViewModel("--", "--")
  }

  def apply(ie029: CC029CType, consignmentItem: CUSTOM_ConsignmentItemType03): ConsignmentItemViewModel =
    new ConsignmentItemViewModel(
      itemNumber = s"${consignmentItem.goodsItemNumber}/${consignmentItem.declarationGoodsItemNumber}",
      shippingMarks = consignmentItem.Packaging.flatMap(_.shippingMarks),
      packages = consignmentItem.Packaging.map(_.asTransitionString),
      containers = ie029.Consignment.TransportEquipment
        .filter(_.GoodsReference.exists(_.declarationGoodsItemNumber == consignmentItem.declarationGoodsItemNumber))
        .flatMap(_.containerIdentificationNumber),
      description = consignmentItem.Commodity.descriptionOfGoods,
      declarationType = consignmentItem.declarationType.getOrElse(ie029.TransitOperation.declarationType),
      commodityCode = consignmentItem.Commodity.CommodityCode.map(_.asString).orElse2Dashes,
      sensitiveGoodsInformation = Seq(SensitiveGoodsInformationViewModel()), // Not in P5?
      previousDocuments = consignmentItem.PreviousDocument.map(_.asString),
      countryOfDispatch = consignmentItem.countryOfDispatch.orElse3Dashes, // In P4 we check this against reference data
      countryOfDestination = consignmentItem.countryOfDestination.orElse3Dashes, // In P4 we check this against reference data
      grossMass = consignmentItem.Commodity.GoodsMeasure.flatMap(_.grossMass.map(_.asString)).orElse3Dashes,
      netMass = consignmentItem.Commodity.GoodsMeasure.flatMap(_.netMass).map(_.asString).orElse3Dashes,
      producedDocuments = None, // Not in P5?
      consigneeViewModel = consignmentItem.Consignee.map(ConsigneeViewModel(_)),
      consignorViewModel = None,
      supportingDocuments = consignmentItem.SupportingDocument.map(_.asString),
      transportDocuments = consignmentItem.TransportDocument.map(_.asString),
      additionalInformation = consignmentItem.AdditionalInformation.map(_.asString),
      additionalReference = consignmentItem.AdditionalReference.map(_.asString)
    )
}
