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
  PreviousDocument: Option[List[PreviousDocument]],
  SupportingDocument: Option[List[SupportingDocument]],
  TransportDocument: Option[List[TransportDocument]],
  AdditionalReference: Option[List[AdditionalReference]],
  AdditionalInformation: Option[List[AdditionalInformation]],
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
  val additionalSupplyChainActor: String  = AdditionalSupplyChainActor.map(_.map(_.toString).mkString("; ")).getOrElse("")
  val commodityCode: String               = Commodity.CommodityCode.map(_.toString).getOrElse("")
  val departureTransportMeans: String     = DepartureTransportMeans.map(_.map(_.toString).mkString("; ")).getOrElse("")
  val dangerousGoods: String              = Commodity.DangerousGoods.map(_.toString).mkString("; ")
  val cusCode: String                     = Commodity.cusCode.getOrElse("")
  val descriptionOfGoods: String          = Commodity.descriptionOfGoods
  val previousDocumentString: String      = PreviousDocument.map(_.map(_.toString).mkString("; ")).getOrElse("")
  val supportingDocumentString: String    = SupportingDocument.map(_.map(_.toString).mkString("; ")).getOrElse("")
  val transportDocumentString: String     = TransportDocument.map(_.map(_.toString).mkString("; ")).getOrElse("")
  val additionalReferenceString: String   = AdditionalReference.map(_.map(_.toString).mkString("; ")).getOrElse("")
  val additionalInformationString: String = AdditionalInformation.map(_.map(_.toString).mkString("; ")).getOrElse("")

  val grossMass: String          = Commodity.GoodsMeasure.grossMass.toString
  val netMass: String            = Commodity.GoodsMeasure.netMass.getOrElse("").toString
  val consignorId: String        = Consignor.flatMap(_.identificationNumber).getOrElse("")
  val consigneeId: String        = Consignee.flatMap(_.identificationNumber).getOrElse("")
  val supplyChainActorId: String = AdditionalSupplyChainActor.map(_.map(_.identificationNumber).mkString("; ")).getOrElse("")

  val totalPackages: Int = Packaging.foldLeft(0)(
    (total, packaging) => total + packaging.numberOfPackages.getOrElse(0)
  )

  val packagesType: String = Packaging.map(_.toString).mkString("; ")

}

object ConsignmentItem {
  implicit val formats: OFormat[ConsignmentItem] = Json.format[ConsignmentItem]
}
