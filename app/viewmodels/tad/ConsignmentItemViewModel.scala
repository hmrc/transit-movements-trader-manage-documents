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

package viewmodels.tad

import generated.{CC015CType, CUSTOM_ConsignmentItemType03, CUSTOM_HouseConsignmentType03, ConsignmentItemType03, HouseConsignmentType03}
import viewmodels.*

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
  countryOfDestination: String,
  supplementaryUnits: String
)

object ConsignmentItemViewModel {

  def apply(ie015: CC015CType, houseConsignment: CUSTOM_HouseConsignmentType03, consignmentItem: CUSTOM_ConsignmentItemType03): ConsignmentItemViewModel =
    new ConsignmentItemViewModel(
      declarationGoodsItemNumber = consignmentItem.declarationGoodsItemNumber.toString(),
      goodsItemNumber = consignmentItem.goodsItemNumber.toString(),
      packagesType = consignmentItem.Packaging.map(_.asTadString).semiColonSeparate.appendPeriod,
      descriptionOfGoods = if (consignmentItem.goodsItemNumber == 1) houseConsignment.grossMass.asString else consignmentItem.Commodity.descriptionOfGoods,
      previousDocuments = consignmentItem.PreviousDocument.map(_.asString).semiColonSeparate.appendPeriod,
      supportingDocuments = consignmentItem.SupportingDocument.map(_.asString).semiColonSeparate.appendPeriod,
      consignee = houseConsignment.Consignee.map(_.asTadString).orElseBlank.appendPeriod,
      consigneeId = houseConsignment.Consignee.flatMap(_.identificationNumber).orElseBlank.appendPeriod,
      consignor = houseConsignment.Consignor.map(_.asString).orElseBlank.appendPeriod,
      consignorId = houseConsignment.Consignor.flatMap(_.identificationNumber).orElseBlank.appendPeriod,
      additionalReferences = consignmentItem.AdditionalReference.map(_.asTadString).semiColonSeparate.appendPeriod,
      additionalInformation = consignmentItem.AdditionalInformation.map(_.asTadString).semiColonSeparate.appendPeriod,
      additionalSupplyChainActors = consignmentItem.AdditionalSupplyChainActor.map(_.asString).semiColonSeparate.appendPeriod,
      supplyChainActorId = consignmentItem.AdditionalSupplyChainActor.map(_.identificationNumber).semiColonSeparate.appendPeriod,
      transportDocuments = consignmentItem.TransportDocument.map(_.asTadString).semiColonSeparate.appendPeriod,
      referenceNumberUCR = consignmentItem.referenceNumberUCR.orElseBlank,
      grossMass = consignmentItem.Commodity.GoodsMeasure.flatMap(_.grossMass.map(_.asString)).orElseBlank,
      departureTransportMeans = houseConsignment.DepartureTransportMeans.map(_.asString).semiColonSeparate.appendPeriod,
      commodityCode = consignmentItem.Commodity.CommodityCode.map(_.asString).orElseBlank,
      netMass = consignmentItem.Commodity.GoodsMeasure.flatMap(_.netMass.map(_.asString)).orElseBlank,
      dangerousGoods = consignmentItem.Commodity.DangerousGoods.map(_.asString).semiColonSeparate,
      cusCode = consignmentItem.Commodity.cusCode.orElseBlank,
      transportCharges = consignmentItem.TransportCharges.map(_.asString).orElseBlank,
      declarationType = consignmentItem.declarationType.orElseBlank,
      countryOfDispatch = consignmentItem.countryOfDispatch.orElseBlank,
      countryOfDestination = consignmentItem.countryOfDestination.orElseBlank,
      supplementaryUnits = ie015.Consignment.HouseConsignment
        .flatMap(_.ConsignmentItem)
        .find(_.declarationGoodsItemNumber == consignmentItem.declarationGoodsItemNumber)
        .flatMap(_.Commodity.GoodsMeasure.flatMap(_.supplementaryUnits.map(_.asString)))
        .getOrElse("")
    )
}
