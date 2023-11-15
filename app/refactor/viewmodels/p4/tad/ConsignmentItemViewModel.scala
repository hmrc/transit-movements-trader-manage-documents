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

package refactor.viewmodels.p4.tad

import generated.p5.CC029CType
import generated.p5.ConsigneeType03
import generated.p5.ConsignmentItemType03
import generated.p5.PackagingType02
import refactor.viewmodels._
import refactor.viewmodels.p4.tad.ConsignmentItemViewModel._
import refactor.viewmodels.p5._
import viewmodels.SpecialMention

case class ConsignmentItemViewModel(
  itemNumber: String,
  shippingMarks: Seq[String],
  packages: Seq[PackageViewModel],
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
  specialMentions: Option[Seq[SpecialMention]], // TODO
  producedDocuments: Option[Seq[String]],
  consigneeViewModel: Option[ConsigneeViewModel],
  consignorViewModel: Option[ConsignorViewModel]
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

  case class PackageViewModel(
    numberOfPackages: BigInt,
    description: String
  ) {

    override def toString: String = if (numberOfPackages > 0) {
      s"$numberOfPackages - $description"
    } else {
      description
    }
  }

  object PackageViewModel {

    def apply(`package`: PackagingType02): PackageViewModel =
      new PackageViewModel(
        numberOfPackages = `package`.numberOfPackages.getOrElse(BigInt(0)),
        description = `package`.typeOfPackages // In P4 we check this against reference data
      )
  }

  case class SensitiveGoodsInformationViewModel(
    goodsCode: String,
    quantity: String
  )

  def apply(ie029: CC029CType, consignmentItem: ConsignmentItemType03): ConsignmentItemViewModel =
    new ConsignmentItemViewModel(
      itemNumber = s"${consignmentItem.goodsItemNumber}/${consignmentItem.declarationGoodsItemNumber}",
      shippingMarks = consignmentItem.Packaging.flatMap(_.shippingMarks),
      packages = consignmentItem.Packaging.map(PackageViewModel(_)),
      containers = ie029.Consignment.TransportEquipment
        .filter(_.GoodsReference.exists(_.declarationGoodsItemNumber == consignmentItem.declarationGoodsItemNumber))
        .map(_.asP4String)
        .addDefaultIfEmpty(),
      description = consignmentItem.Commodity.descriptionOfGoods,
      declarationType = consignmentItem.declarationType.getOrElse(ie029.TransitOperation.declarationType),
      commodityCode = consignmentItem.Commodity.CommodityCode.map(_.asString).orElse2Dashes,
      sensitiveGoodsInformation = Seq(SensitiveGoodsInformationViewModel("--", "--")), // Not in P5?
      previousDocuments = consignmentItem.PreviousDocument.map(_.asP4String).addDefaultIfEmpty(),
      countryOfDispatch = consignmentItem.countryOfDispatch.orElse3Dashes,       // In P4 we check this against reference data
      countryOfDestination = consignmentItem.countryOfDestination.orElse3Dashes, // In P4 we check this against reference data
      grossMass = consignmentItem.Commodity.GoodsMeasure.map(_.grossMass.toString()).orElse3Dashes,
      netMass = consignmentItem.Commodity.GoodsMeasure.flatMap(_.netMass).map(_.toString()).orElse3Dashes,
      specialMentions = None,   // Not in P5?
      producedDocuments = None, // Not in P5?
      consigneeViewModel = consignmentItem.Consignee.map(ConsigneeViewModel(_)),
      consignorViewModel = None
    )
}
