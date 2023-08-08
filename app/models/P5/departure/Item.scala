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

package models.P5.departure

case class Item(
  declarationGoodsItemNumber: String,
  goodsItemNumber: String,
  packaging: String,
  consignor: String,
  consignee: String,
  referenceNumberUcr: String,
  transportCharges: String,
  countryOfDispatch: String,
  countryOfDestination: String,
  declarationType: String,
  additionalSupplyChainActor: String,
  commodityCode: String,
  departureTransportMeans: String,
  dangerousGoods: String,
  cusCode: String,
  descriptionOfGoods: String,
  previousDocument: String,
  supportingDocument: String,
  transportDocument: String,
  additionalReference: String,
  additionalInformation: String,
  grossMass: String,
  netMass: String
)
