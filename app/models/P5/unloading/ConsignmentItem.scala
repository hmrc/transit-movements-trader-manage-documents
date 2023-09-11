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

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class ConsignmentItem(
  goodsItemNumber: String,
  declarationGoodsItemNumber: Int,
  declarationType: Option[String],
  countryOfDestination: Option[String],
  Consignee: Option[Consignee],
  Commodity: Commodity,
  Packaging: Seq[Packaging],
  PreviousDocument: Option[Seq[PreviousDocument]],
  SupportingDocument: Option[Seq[SupportingDocument]],
  TransportDocument: Option[Seq[TransportDocument]],
  AdditionalReference: Option[Seq[AdditionalReference]],
  AdditionalInformation: Option[Seq[AdditionalInformation]]
) {

  val totalPackages: Int = Packaging.total(_.numberOfPackages.getOrElse(0))

  val packaging: String = Packaging.showAll

  val consignee: String   = Consignee.map(_.toString).getOrElse("")
  val consigneeId: String = Consignee.flatMap(_.identificationNumber).getOrElse("")

  val udng: String    = Commodity.DangerousGoods.showAll
  val cusCode: String = Commodity.cusCode.getOrElse("")

  val previousDocuments: String   = PreviousDocument.showAll
  val supportingDocuments: String = SupportingDocument.showAll
  val transportDocuments: String  = TransportDocument.showAll

  val additionalReferences: String  = AdditionalReference.showAll
  val additionalInformation: String = AdditionalInformation.showAll

  val commodityCode: String = Commodity.CommodityCode.map(_.toString).getOrElse("")

  val grossMass: String = Commodity.GoodsMeasure.map(_.grossMass.toString()).getOrElse("")
  val netMass: String   = Commodity.GoodsMeasure.flatMap(_.netMass.map(_.toString())).getOrElse("")

  val cOfDest: String = countryOfDestination.getOrElse("")
}

object ConsignmentItem {
  implicit val formats: OFormat[ConsignmentItem] = Json.format[ConsignmentItem]
}
