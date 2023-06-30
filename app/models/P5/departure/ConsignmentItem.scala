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

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class ConsignmentItem(
  declarationType: Option[String],
  countryOfDispatch: Option[String],
  countryOfDestination: Option[String],
  Consignor: Option[Consignor],
  Consignee: Option[Consignee],
  goodsItemNumber: String,
  declarationGoodsItemNumber: Int,
  Packaging: Seq[Packaging],
  Commodity: Commodity,
  referenceNumberUCR: Option[String],
  TransportCharges: Option[TransportCharges],
  previousDocument: Option[List[PreviousDocument]],
  supportingDocument: Option[List[SupportingDocument]],
  transportDocument: Option[List[TransportDocument]],
  additionalReference: Option[List[AdditionalReference]],
  additionalInformation: Option[List[AdditionalInformation]],
  AdditionalSupplyChainActor: Option[List[AdditionalSupplyChainActor]],
  DepartureTransportMeans: Option[List[DepartureTransportMeans]]
) {
  val declarationTypeString: String            = declarationType.getOrElse("")
  val countryOfDispatchString: String          = countryOfDispatch.getOrElse("")
  val countryOfDestinationString: String       = countryOfDestination.getOrElse("")
  val consignor: String                        = Consignor.map(_.toString).getOrElse("")
  val consignee: String                        = Consignee.map(_.toString).getOrElse("")
  val goodsItemNumberString: String            = goodsItemNumber
  val declarationGoodsItemNumberString: String = declarationGoodsItemNumber.toString
  val packaging: String                        = Packaging.map(_.toString).mkString("; ")
  val referenceNumberUCRString: String         = referenceNumberUCR.getOrElse("")

  val transportCharges: String            = TransportCharges.map(_.toString).getOrElse("")
  val additionalSupplyChainActor: String = AdditionalSupplyChainActor.flatMap(_.map(_.toString)).mkString("; ")
  val commodityCode: String               = Commodity.CommodityCode.map(_.toString).getOrElse("")
  val departureTransportMeans: String     = DepartureTransportMeans.flatMap(_.map(_.toString)).mkString("; ")
  val dangerousGoods: String              = Commodity.dangerousGoods.toString
  val cusCode: String                     = Commodity.cusCode.getOrElse("")
  val descriptionOfGoods: String          = Commodity.descriptionOfGoods
  val previousDocumentString: String      = previousDocument.flatMap(_.map(_.toString)).mkString("; ")
  val supportingDocumentString: String    = supportingDocument.flatMap(_.map(_.toString)).mkString("; ")
  val transportDocumentString: String     = transportDocument.flatMap(_.map(_.toString)).mkString("; ")
  val additionalReferenceString: String   = additionalReference.flatMap(_.map(_.toString)).mkString("; ")
  val additionalInformationString: String = additionalInformation.flatMap(_.map(_.toString)).mkString("; ")

  val grossMass: String = Commodity.GoodsMeasure.grossMass.toString
  val netMass: String   = Commodity.GoodsMeasure.netMass.toString

  val totalPackages: Int = Packaging.foldLeft(0)(
    (total, packaging) => total + packaging.numberOfPackages.getOrElse(0)
  )
}

object ConsignmentItem {
  implicit val formats: OFormat[ConsignmentItem] = Json.format[ConsignmentItem]
}
