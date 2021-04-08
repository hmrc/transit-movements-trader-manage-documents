/*
 * Copyright 2021 HM Revenue & Customs
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

package viewmodels

import cats.data.NonEmptyList
import models.DeclarationType
import models.GuaranteeDetails
import models.reference.Country
import utils.FormattedDate

final case class TransitSecurityAccompanyingDocumentPDF(
  movementReferenceNumber: String,
  declarationType: DeclarationType,
  singleCountryOfDispatch: Option[Country],
  singleCountryOfDestination: Option[Country],
  transportIdentity: Option[String],
  transportCountry: Option[Country],
  acceptanceDate: Option[FormattedDate],
  numberOfItems: Int,
  numberOfPackages: Option[Int],
  grossMass: BigDecimal,
  printBindingItinerary: Boolean,
  authId: Option[String],
  copyType: Boolean,
  circumstanceIndicator: Option[String],
  security: Option[Int],
  commercialReferenceNumber: Option[String],
  principal: Principal,
  consignor: Option[Consignor],
  consignee: Option[Consignee],
  departureOffice: CustomsOfficeWithOptionalDate,
  destinationOffice: CustomsOfficeWithOptionalDate,
  customsOfficeOfTransit: Seq[CustomsOfficeWithOptionalDate],
  guaranteeDetails: Seq[GuaranteeDetails],
  seals: Seq[String],
  returnCopiesCustomsOffice: Option[ReturnCopiesCustomsOffice],
  controlResult: Option[ControlResult],
  goodsItems: NonEmptyList[GoodsItem]
) {

  val consignorOne: Option[Consignor] = Helpers.consignorOne(goodsItems, consignor)

  val consigneeOne: Option[Consignee] = Helpers.consigneeOne(goodsItems, consignee)

  val countryOfDispatch: Option[Country] = Helpers.countryOfDispatch(goodsItems)

  val countryOfDestination: Option[Country] = Helpers.countryOfDestination(goodsItems)

  val printListOfItems: Boolean = Helpers.printListOfItems(goodsItems)

  val printVariousConsignees: Boolean =
    printListOfItems &&
      consigneeOne.isEmpty &&
      goodsItems.toList.flatMap(_.consignee).nonEmpty

  val printVariousConsignors: Boolean =
    printListOfItems &&
      consignorOne.isEmpty &&
      goodsItems.toList.flatMap(_.consignor).nonEmpty

  val totalNumberOfPackages: String = numberOfPackages.fold("")(_.toString)

  val firstCustomsOfficeOfTransitArrivalTime: String = customsOfficeOfTransit.headOption.flatMap(_.dateTimeFormatted).getOrElse("---")
}
