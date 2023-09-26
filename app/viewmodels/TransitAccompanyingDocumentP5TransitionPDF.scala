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

package viewmodels

import cats.data.NonEmptyList
import models.DeclarationType
import models.GuaranteeDetails
import models.P5.departure._
import models.reference.Country
import utils.FormattedDate

final case class TransitAccompanyingDocumentP5TransitionPDF(
  movementReferenceNumber: String,
  declarationType: DeclarationType,
  singleCountryOfDispatch: Option[Country],
  singleCountryOfDestination: Option[Country],
  transportIdentity: Option[String],
  transportCountry: Option[Country],
  limitDate: String,
  acceptanceDate: Option[FormattedDate],
  numberOfItems: Int,
  numberOfPackages: Option[Int],
  grossMass: BigDecimal,
  printBindingItinerary: Boolean,
  authId: Option[String],
  goodsReference: Seq[GoodsReference],
  copyType: Boolean,
  holderOfTransitProcedure: HolderOfTransitProcedure,
  consignor: Option[models.P5.departure.Consignor],
  consignee: Option[models.P5.departure.Consignee],
  departureOffice: CustomsOfficeOfDeparture,
  destinationOffice: CustomsOfficeOfDestinationDeclared,
  customsOfficeOfTransit: Seq[CustomsOfficeOfTransitDeclared],
  previousDocuments: Seq[PreviousDocument],
  supportingDocuments: Seq[SupportingDocument],
  transportDocuments: Seq[TransportDocument],
  additionalInformation: Seq[AdditionalInformation],
  additionalReferences: Seq[AdditionalReference],
  guaranteeType: String,
  seals: Seq[String],
  returnCopiesCustomsOffice: Option[ReturnCopiesCustomsOffice],
  controlResult: Option[ControlResult],
  goodsItems: NonEmptyList[GoodsItemP5Transition]
) {

  val itemConsignorOne: Option[models.P5.departure.Consignor] = Helpers.consignorOneTransition(goodsItems, consignor)

  val itemConsigneeOne: Option[models.P5.departure.Consignee] = Helpers.consigneeOneTransition(goodsItems, consignee)

  val countryOfDispatch: Option[Country] = Helpers.countryOfDispatchTransition(goodsItems)

  val countryOfDestination: Option[Country] = Helpers.countryOfDestinationTransition(goodsItems)

  val printListOfItems: Boolean = Helpers.printListOfItemsTransition(goodsItems)

  val printVariousConsignees: Boolean =
    printListOfItems &&
      itemConsigneeOne.isEmpty &&
      goodsItems.toList.flatMap(_.consignee).nonEmpty

  val printVariousConsignors: Boolean =
    printListOfItems &&
      itemConsignorOne.isEmpty &&
      goodsItems.toList.flatMap(_.consignor).nonEmpty

  val totalNumberOfPackages: String = numberOfPackages.fold("")(_.toString)
}
