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

import models.P5.departure.{Commodity, CommodityCode, IE029Data, Packaging}

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

  val consignee = ie029Data.data.Consignment.Consignee match {
    case Some(value) => value.toString
    case None => "TODO get multiple consignor"
  }

  val referenceNumberUcr = ie029Data.data.Consignment.referenceNumberUCR.getOrElse("")

  val transportCharges = ie029Data.data.Consignment.TransportCharges match {
    case Some(value) => value.toString
    case None => "TODO get multiple consignor"
  }

  val countryOfDispatch = ie029Data.data.Consignment.countryOfDispatch.getOrElse("")

  val countryOfDestination = ie029Data.data.Consignment.countryOfDestination.getOrElse("")

  val declarationType = ie029Data.data.TransitOperation.declarationType

  val additionalSupplyChainActor = ie029Data.data.Consignment.additionalSupplyChainActorsRole.getOrElse("")

  val commodity: String = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.CommodityCode.toString)).mkString("; ")

  val departureTransportMeans: String = ie029Data.data.Consignment.departureTransportMeans.getOrElse("")

  val commodity: String = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.dangerousGoods.toString)).mkString("; ")

  val cusCode: String = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.cusCode.toString)).mkString("; ")

  val descriptionOfGoods: String = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.descriptionOfGoods)).mkString("; ")

  val previousDocument: String = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.previousDocument.toString)).mkString("; ")

}

case class Item(declarationGoodsItemNumber: String, goodsItemNumber: String, packaging: String)
