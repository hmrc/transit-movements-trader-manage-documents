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

trait TransitDocument {
  val movementReferenceNumber: String
  val declarationType: DeclarationType
  val singleCountryOfDispatch: Option[Country]
  val singleCountryOfDestination: Option[Country]
  val transportIdentity: Option[String]
  val transportCountry: Option[Country]
  val numberOfItems: Int
  val numberOfPackages: Int
  val grossMass: BigDecimal
  val principal: Principal
  val consignor: Option[Consignor]
  val consignee: Option[Consignee]
  val departureOffice: String
  val departureOfficeTrimmed: String
  val seals: Seq[String]
  val goodsItems: NonEmptyList[GoodsItem]
  val documentHeading: DocumentHeading

  val presentationOffice: Option[String]               = None
  val traderAtDestination: Option[TraderAtDestination] = None
  val acceptanceDate: Option[LocalDate]                = None
  val acceptanceDateFormatted: Option[String]          = None

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
      goodsItems.head.producedDocuments.size > 4 ||
      goodsItems.head.previousAdministrativeReferences.size > 1

  val printVariousConsignees: Boolean =
    printListOfItems &&
      consigneeOne.isEmpty &&
      goodsItems.toList.flatMap(_.consignee).nonEmpty

  val printVariousConsignors: Boolean =
    printListOfItems &&
      consignorOne.isEmpty &&
      goodsItems.toList.flatMap(_.consignor).nonEmpty
}
