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
  goodsItemNumber: String,
  declarationGoodsItemNumber: Int,
  declarationType: Option[String],
  countryOfDispatch: Option[String],
  countryOfDestination: Option[String],
  referenceNumberUCR: Option[String],
  consignee: Option[Consignee],
  additionalSupplyChainActor: Option[List[AdditionalSupplyChainActor]],
  commodity: Commodity,
  packaging: Seq[Packaging],
  previousDocument: Option[List[PreviousDocument]],
  supportingDocument: Option[List[SupportingDocument]],
  transportDocument: Option[List[TransportDocument]],
  additionalReference: Option[List[AdditionalReference]],
  additionalInformation: Option[List[AdditionalInformation]],
  transportCharges: Option[TransportCharges]
) {
  val declarationTypeString: String            = declarationType.getOrElse("")
  val countryOfDispatchString: String          = countryOfDispatch.getOrElse("")
  val countryOfDestinationString: String       = countryOfDestination.getOrElse("")
  val consigneeFormat: String                  = consignee.map(_.toString).getOrElse("")
  val goodsItemNumberString: String            = goodsItemNumber
  val declarationGoodsItemNumberString: String = declarationGoodsItemNumber.toString
  val packagingFormat: String                  = packaging.showAll
  val referenceNumberUCRString: String         = referenceNumberUCR.getOrElse("")

  val transportChargesFormat: String           = transportCharges.map(_.toString).getOrElse("")
  val additionalSupplyChainActorFormat: String = additionalSupplyChainActor.map(_.showAll).getOrElse("")
  val commodityCode: String                    = commodity.commodityCode.map(_.toString).getOrElse("")
  val dangerousGoods: String                   = commodity.dangerousGoods.map(_.showAll).getOrElse("")
  val cusCode: String                          = commodity.cusCode.getOrElse("")
  val descriptionOfGoods: String               = commodity.descriptionOfGoods
  val previousDocumentString: String           = previousDocument.map(_.showAll).getOrElse("")
  val supportingDocumentString: String         = supportingDocument.map(_.showAll).getOrElse("")
  val transportDocumentString: String          = transportDocument.map(_.showAll).getOrElse("")
  val additionalReferenceString: String        = additionalReference.map(_.showAll).getOrElse("")
  val additionalInformationString: String      = additionalInformation.map(_.showAll).getOrElse("")

  val grossMass: String          = commodity.goodsMeasure.grossMass.toString
  val netMass: String            = commodity.goodsMeasure.netMass.getOrElse("").toString
  val consigneeId: String        = consignee.flatMap(_.identificationNumber).getOrElse("")
  val supplyChainActorId: String = additionalSupplyChainActor.map(_.map(_.identificationNumber).mkString("; ")).getOrElse("")

  val packagesType: String = packaging.map(_.toString).mkString("; ")

}

object ConsignmentItem {
  implicit val formats: OFormat[ConsignmentItem] = Json.format[ConsignmentItem]
}
