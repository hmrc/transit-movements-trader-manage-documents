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

package refactor.viewmodels.p5.unloadingpermission

import generated.p5.ConsignmentItemType04
import refactor.viewmodels._
import refactor.viewmodels.p5._

case class ConsignmentItemViewModel(
  declarationGoodsItemNumber: String,
  goodsItemNumber: String,
  packaging: String,
  consignee: String,
  consigneeId: String,
  udng: String,
  cusCode: String,
  descriptionOfGoods: String,
  previousDocuments: String,
  supportingDocuments: String,
  additionalReferences: String,
  additionalInformation: String,
  transportDocuments: String,
  commodityCode: String,
  grossMass: String,
  cOfDest: String,
  netMass: String
)

object ConsignmentItemViewModel {

  def apply(consignmentItem: ConsignmentItemType04): ConsignmentItemViewModel =
    new ConsignmentItemViewModel(
      declarationGoodsItemNumber = consignmentItem.declarationGoodsItemNumber.toString(),
      goodsItemNumber = consignmentItem.goodsItemNumber,
      packaging = consignmentItem.Packaging.map(_.asString).separateWithSemiColon,
      consignee = consignmentItem.Consignee.map(_.asString).orElseBlank,
      consigneeId = consignmentItem.Consignee.flatMap(_.identificationNumber).orElseBlank,
      udng = consignmentItem.Commodity.DangerousGoods.map(_.asString).separateWithSemiColon,
      cusCode = consignmentItem.Commodity.cusCode.orElseBlank,
      descriptionOfGoods = consignmentItem.Commodity.descriptionOfGoods,
      previousDocuments = consignmentItem.PreviousDocument.map(_.asString).separateWithSemiColon,
      supportingDocuments = consignmentItem.SupportingDocument.map(_.asString).separateWithSemiColon,
      additionalReferences = consignmentItem.AdditionalReference.map(_.asString).separateWithSemiColon,
      additionalInformation = consignmentItem.AdditionalInformation.map(_.asString).separateWithSemiColon,
      transportDocuments = consignmentItem.TransportDocument.map(_.asString).separateWithSemiColon,
      commodityCode = consignmentItem.Commodity.CommodityCode.map(_.asString).orElseBlank,
      grossMass = consignmentItem.Commodity.GoodsMeasure.map(_.grossMass.toString).orElseBlank,
      cOfDest = consignmentItem.countryOfDestination.orElseBlank,
      netMass = consignmentItem.Commodity.GoodsMeasure.flatMap(_.netMass.map(_.toString())).orElseBlank
    )
}
