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

package viewmodels.P5

import models.P5.departure.IE029Data
import models.P5.departure.Packaging

case class ConsignmentItemViewModel(implicit ie029Data: IE029Data) {

  val declarationGoodsItemNumberString: Seq[String] =
    ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.declarationGoodsItemNumber.toString))

  val goodsItemNumberString: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.goodsItemNumber))

  val packagings: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Packaging.toString))

  val consignor = ie029Data.data.Consignment.Consignor match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val tup = declarationGoodsItemNumberString zip goodsItemNumberString zip packagings map {
    case ((x, y), z) => (x, y, z)
  }

  val items = tup.map(
    x => Item(x._1, x._2, x._3)
  )

}

case class Item(declarationGoodsItemNumber: String, goodsItemNumber: String, packaging: String)
