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
import models.reference.Country

object Helpers {

  private def singleValue[A](items: Seq[A], goodsItems: NonEmptyList[GoodsItem]): Option[A] =
    if (items.distinct.size == 1 && items.size == goodsItems.size) {
      Some(items.head)
    } else {
      None
    }

  def consignorOne(goodsItems: NonEmptyList[GoodsItem], consignor: Option[Consignor]): Option[Consignor] =
    if (goodsItems.toList.flatMap(_.consignor).nonEmpty) {
      singleValue(goodsItems.toList.flatMap(_.consignor), goodsItems)
    } else {
      consignor
    }

  def consigneeOne(goodsItems: NonEmptyList[GoodsItem], consignee: Option[Consignee]): Option[Consignee] =
    if (goodsItems.toList.flatMap(_.consignee).nonEmpty) {
      singleValue(goodsItems.toList.flatMap(_.consignee), goodsItems)
    } else {
      consignee
    }

  def countryOfDispatch(goodsItems: NonEmptyList[GoodsItem]): Option[Country] =
    singleValue(goodsItems.toList.flatMap(_.countryOfDispatch), goodsItems)

  def countryOfDestination(goodsItems: NonEmptyList[GoodsItem]): Option[Country] =
    singleValue(goodsItems.toList.flatMap(_.countryOfDestination), goodsItems)

  def printListOfItems(goodsItems: NonEmptyList[GoodsItem]): Boolean =
    goodsItems.size > 1 ||
      goodsItems.head.containers.size > 1 ||
      goodsItems.head.sensitiveGoodsInformation.length > 1 ||
      goodsItems.head.packages.size > 1 ||
      goodsItems.head.specialMentions.size > 4 ||
      goodsItems.head.producedDocuments.size > 4
}
