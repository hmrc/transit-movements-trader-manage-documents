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

import generated.*
import viewmodels.*

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
      goodsItemNumber = consignmentItem.goodsItemNumber.toString,
      packaging = consignmentItem.Packaging.map(_.asString).semiColonSeparate,
      consignee = consignmentItem.Consignee.map(_.asString).orElseBlank,
      consigneeId = consignmentItem.Consignee.flatMap(_.identificationNumber).orElseBlank,
      udng = consignmentItem.Commodity.DangerousGoods.map(_.asString).semiColonSeparate,
      cusCode = consignmentItem.Commodity.cusCode.orElseBlank,
      descriptionOfGoods = consignmentItem.Commodity.descriptionOfGoods,
      previousDocuments = consignmentItem.PreviousDocument.map(_.asString).semiColonSeparate,
      supportingDocuments = consignmentItem.SupportingDocument.map(_.asString).semiColonSeparate,
      additionalReferences = consignmentItem.AdditionalReference.map(_.asString).semiColonSeparate,
      additionalInformation = consignmentItem.AdditionalInformation.map(_.asString).semiColonSeparate,
      transportDocuments = consignmentItem.TransportDocument.map(_.asString).semiColonSeparate,
      commodityCode = consignmentItem.Commodity.CommodityCode.map(_.asString).orElseBlank,
      grossMass = consignmentItem.Commodity.GoodsMeasure.grossMass.map(_.asString).orElseBlank,
      cOfDest = consignmentItem.countryOfDestination.orElseBlank,
      netMass = consignmentItem.Commodity.GoodsMeasure.netMass.map(_.asString).orElseBlank
    )
}
