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
  transportIdentity: Option[String],
  transportCountry: Option[Country],
  acceptanceDate: LocalDate,
  acceptanceDateFormatted: String,
  numberOfItems: Int,
  numberOfPackages: Int,
  grossMass: BigDecimal,
  principal: Principal,
  traderAtDestination: TraderAtDestination,
  departureOffice: String,
  departureOfficeTrimmed: String,
  presentationOffice: String,
  seals: Seq[String],
  goodsItems: NonEmptyList[GoodsItem]
) {

  private def singleValue[A](items: Seq[A]): Option[A] =
    if (items.distinct.size == 1 && items.size == goodsItems.size) {
      Some(items.head)
    } else {
      None
    }

  val consignor: Option[Consignor] =
    singleValue(goodsItems.toList.flatMap(_.consignor))

  val consignee: Option[Consignee] =
    singleValue(goodsItems.toList.flatMap(_.consignee))

  val countryOfDispatch: Option[Country] =
    singleValue(goodsItems.toList.map(_.countryOfDispatch))

  val countryOfDestination: Option[Country] =
    singleValue(goodsItems.toList.map(_.countryOfDestination))

  val printListOfItems: Boolean =
    goodsItems.size > 1 ||
      goodsItems.head.containers.size > 1 ||
      goodsItems.head.sensitiveGoodsInformation.length > 1 ||
      goodsItems.head.packages.size > 1 ||
      goodsItems.head.specialMentions.size > 4 ||
      goodsItems.head.producedDocuments.size > 4

  val printVariousConsignees: Boolean =
    printListOfItems &&
      consignee.isEmpty &&
      goodsItems.toList.flatMap(_.consignee).nonEmpty

  val printVariousConsignors: Boolean =
    printListOfItems &&
      consignor.isEmpty &&
      goodsItems.toList.flatMap(_.consignor).nonEmpty
}
