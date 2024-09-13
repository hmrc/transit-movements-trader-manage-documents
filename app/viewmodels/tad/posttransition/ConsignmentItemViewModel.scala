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

import generated.rfc37.ConsignmentItemType03
import generated.rfc37.HouseConsignmentType03
import viewmodels._

case class ConsignmentItemViewModel(
  declarationGoodsItemNumber: String,
  goodsItemNumber: String,
  packagesType: String,
  descriptionOfGoods: String,
  previousDocuments: String,
  supportingDocuments: String,
  consignee: String,
  consigneeId: String,
  consignor: String,
  consignorId: String,
  additionalReferences: String,
  additionalInformation: String,
  additionalSupplyChainActors: String,
  supplyChainActorId: String,
  transportDocuments: String,
  referenceNumberUCR: String,
  grossMass: String,
  departureTransportMeans: String,
  commodityCode: String,
  netMass: String,
  dangerousGoods: String,
  cusCode: String,
  transportCharges: String,
  declarationType: String,
  countryOfDispatch: String,
  countryOfDestination: String
)

object ConsignmentItemViewModel {

  def apply(houseConsignment: HouseConsignmentType03, consignmentItem: ConsignmentItemType03): ConsignmentItemViewModel =
    new ConsignmentItemViewModel(
      declarationGoodsItemNumber = consignmentItem.declarationGoodsItemNumber.toString(),
      goodsItemNumber = consignmentItem.goodsItemNumber,
      packagesType = consignmentItem.Packaging.map(_.asString).semiColonSeparate,
      descriptionOfGoods = consignmentItem.Commodity.descriptionOfGoods,
      previousDocuments = consignmentItem.PreviousDocument.map(_.asString).semiColonSeparate,
      supportingDocuments = consignmentItem.SupportingDocument.map(_.asString).semiColonSeparate,
      consignee = houseConsignment.Consignee.map(_.asString).orElseBlank,
      consigneeId = houseConsignment.Consignee.flatMap(_.identificationNumber).orElseBlank,
      consignor = houseConsignment.Consignor.map(_.asString).orElseBlank,
      consignorId = houseConsignment.Consignor.flatMap(_.identificationNumber).orElseBlank,
      additionalReferences = consignmentItem.AdditionalReference.map(_.asString).semiColonSeparate,
      additionalInformation = consignmentItem.AdditionalInformation.map(_.asString).semiColonSeparate,
      additionalSupplyChainActors = consignmentItem.AdditionalSupplyChainActor.map(_.asString).take3(_.semiColonSeparate),
      supplyChainActorId = consignmentItem.AdditionalSupplyChainActor.map(_.identificationNumber).take3(_.semiColonSeparate),
      transportDocuments = consignmentItem.TransportDocument.map(_.asString).semiColonSeparate,
      referenceNumberUCR = consignmentItem.referenceNumberUCR.orElseBlank,
      grossMass = consignmentItem.Commodity.GoodsMeasure.flatMap(_.grossMass.map(_.asString)).orElseBlank,
      departureTransportMeans = houseConsignment.DepartureTransportMeans.map(_.asString).semiColonSeparate,
      commodityCode = consignmentItem.Commodity.CommodityCode.map(_.asString).orElseBlank,
      netMass = consignmentItem.Commodity.GoodsMeasure.flatMap(_.netMass.map(_.asString)).orElseBlank,
      dangerousGoods = consignmentItem.Commodity.DangerousGoods.map(_.asString).semiColonSeparate,
      cusCode = consignmentItem.Commodity.cusCode.orElseBlank,
      transportCharges = consignmentItem.TransportCharges.map(_.asString).orElseBlank,
      declarationType = consignmentItem.declarationType.orElseBlank,
      countryOfDispatch = consignmentItem.countryOfDispatch.orElseBlank,
      countryOfDestination = consignmentItem.countryOfDestination.orElseBlank
    )
}
