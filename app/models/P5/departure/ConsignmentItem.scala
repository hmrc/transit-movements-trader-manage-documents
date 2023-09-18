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

import cats.data.NonEmptyList
import models.reference.KindOfPackage
import viewmodels.BulkPackage
import viewmodels.RegularPackage
import viewmodels.UnpackedPackage
import models.P5.departure.{Packaging => PackagingP5}
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class ConsignmentItem(
  goodsItemNumber: String,
  declarationGoodsItemNumber: Int,
  declarationType: Option[String],
  countryOfDispatch: Option[String],
  countryOfDestination: Option[String],
  referenceNumberUCR: Option[String],
  Consignee: Option[Consignee],
  AdditionalSupplyChainActor: Option[List[AdditionalSupplyChainActor]],
  Commodity: Commodity,
  Packaging: Seq[Packaging],
  PreviousDocument: Option[List[PreviousDocument]],
  SupportingDocument: Option[List[SupportingDocument]],
  TransportDocument: Option[List[TransportDocument]],
  AdditionalReference: Option[List[AdditionalReference]],
  AdditionalInformation: Option[List[AdditionalInformation]],
  TransportCharges: Option[TransportCharges]
) {
  val declarationTypeString: String            = declarationType.getOrElse("")
  val countryOfDispatchString: String          = countryOfDispatch.getOrElse("")
  val countryOfDestinationString: String       = countryOfDestination.getOrElse("")
  val consigneeFormat: String                  = Consignee.map(_.toString).getOrElse("")
  val goodsItemNumberString: String            = goodsItemNumber
  val declarationGoodsItemNumberString: String = declarationGoodsItemNumber.toString
  val referenceNumberUCRString: String         = referenceNumberUCR.getOrElse("")

  val transportChargesFormat: String           = TransportCharges.map(_.toString).getOrElse("")
  val additionalSupplyChainActorFormat: String = AdditionalSupplyChainActor.map(_.showAll).getOrElse("")
  val commodityCode: String                    = Commodity.CommodityCode.map(_.toString).getOrElse("")
  val dangerousGoods: String                   = Commodity.DangerousGoods.map(_.showAll).getOrElse("")
  val cusCode: String                          = Commodity.cusCode.getOrElse("")
  val descriptionOfGoods: String               = Commodity.descriptionOfGoods
  val previousDocumentString: String           = PreviousDocument.map(_.showAll).getOrElse("")
  val supportingDocumentString: String         = SupportingDocument.map(_.showAll).getOrElse("")
  val transportDocumentString: String          = TransportDocument.map(_.showAll).getOrElse("")
  val additionalReferenceString: String        = AdditionalReference.map(_.showAll).getOrElse("")
  val additionalInformationString: String      = AdditionalInformation.map(_.showAll).getOrElse("")

  val grossMass: String          = Commodity.GoodsMeasure.grossMass.toString
  val netMass: String            = Commodity.GoodsMeasure.netMass.getOrElse("").toString
  val consigneeId: String        = Consignee.flatMap(_.identificationNumber).getOrElse("")
  val supplyChainActorId: String = AdditionalSupplyChainActor.map(_.map(_.identificationNumber).mkString("; ")).getOrElse("")

  val packagesType: String = Packaging.map(_.toString).mkString("; ")

  val packagingFormat: NonEmptyList[viewmodels.Package] =
    NonEmptyList
      .fromList(Packaging.flatMap {
        case PackagingP5(_, Some(numberOfPackages), typeOfPackages, Some(shippingMarks)) =>
          Some(RegularPackage(KindOfPackage(typeOfPackages, ""), numberOfPackages, shippingMarks))
        case PackagingP5(_, None, typeOfPackages, shippingMarks) =>
          Some(BulkPackage(KindOfPackage(typeOfPackages, ""), shippingMarks))
        case PackagingP5(_, None, typeOfPackages, shippingMarks) =>
          Some(UnpackedPackage(KindOfPackage(typeOfPackages, ""), 0, shippingMarks))
        case _ => None
      }.toList)
      .get

}

object ConsignmentItem {
  implicit val formats: OFormat[ConsignmentItem] = Json.format[ConsignmentItem]
}
