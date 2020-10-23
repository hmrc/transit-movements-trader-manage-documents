/*
 * Copyright 2020 HM Revenue & Customs
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

import java.time.LocalDate

import cats.data.NonEmptyList
import models.DeclarationType
import models.reference.Country

final case class PermissionToStartUnloading(
  movementReferenceNumber: String,
  declarationType: DeclarationType,
  singleCountryOfDispatch: Option[Country],
  singleCountryOfDestination: Option[Country],
  transportIdentity: Option[String],
  transportCountry: Option[Country],
  acceptanceDate: Option[LocalDate],
  acceptanceDateFormatted: Option[String],
  numberOfItems: Int,
  numberOfPackages: Int,
  grossMass: BigDecimal,
  principal: Principal,
  consignor: Option[Consignor],
  consignee: Option[Consignee],
  traderAtDestination: Option[TraderAtDestination],
  departureOffice: String,
  departureOfficeTrimmed: String,
  presentationOffice: Option[String],
  seals: Seq[String],
  goodsItems: NonEmptyList[GoodsItem]
) {

  private def singleValue[A](items: Seq[A]): Option[A] =
    if (items.distinct.size == 1 && items.size == goodsItems.size) {
      Some(items.head)
    } else {
      None
    }

  val consignorOne: Option[Consignor] = {
    if (goodsItems.toList.flatMap(_.consignor).nonEmpty) {
      singleValue(goodsItems.toList.flatMap(_.consignor))
    } else {
      consignor
    }
  }

  val consigneeOne: Option[Consignee] = {
    if (goodsItems.toList.flatMap(_.consignee).nonEmpty) {
      singleValue(goodsItems.toList.flatMap(_.consignee))
    } else {
      consignee
    }
  }

  val countryOfDispatch: Option[Country] =
    singleValue(goodsItems.toList.flatMap(_.countryOfDispatch))

  val countryOfDestination: Option[Country] =
    singleValue(goodsItems.toList.flatMap(_.countryOfDestination))

  val printListOfItems: Boolean =
    goodsItems.size > 1 ||
      goodsItems.head.containers.size > 1 ||
      goodsItems.head.sensitiveGoodsInformation.length > 1 ||
      goodsItems.head.packages.size > 1 ||
      goodsItems.head.specialMentions.size > 4 ||
      goodsItems.head.producedDocuments.size > 4

  val printVariousConsignees: Boolean =
    printListOfItems &&
      consigneeOne.isEmpty &&
      goodsItems.toList.flatMap(_.consignee).nonEmpty

  val printVariousConsignors: Boolean =
    printListOfItems &&
      consignorOne.isEmpty &&
      goodsItems.toList.flatMap(_.consignor).nonEmpty
}
