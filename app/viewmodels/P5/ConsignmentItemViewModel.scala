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

import models.P5.departure.{Commodity, CommodityCode, ConsignmentItem, IE029Data, Packaging}

case class ConsignmentItemViewModel(implicit ie029Data: IE029Data) {

  val declarationGoodsItemNumberString: Seq[String] =
    ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.declarationGoodsItemNumber.toString))

  val goodsItemNumberString: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.goodsItemNumber))

  val packagings: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Packaging.toString))

  val consignor = ie029Data.data.Consignment.Consignor match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val consignmentItems: Seq[ConsignmentItem] = ie029Data.data.Consignment.consignmentItems

  val consignee = ie029Data.data.Consignment.Consignee match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val referenceNumberUcr = ie029Data.data.Consignment.referenceNumberUCR.getOrElse("")

  val transportCharges = ie029Data.data.Consignment.TransportCharges match {
    case Some(value) => value.toString
    case None        => "TODO get multiple consignor"
  }

  val countryOfDispatch: String = ie029Data.data.Consignment.countryOfDispatch.getOrElse("")

  val countryOfDestination: String = ie029Data.data.Consignment.countryOfDestination.getOrElse("")

  val declarationType: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.flatMap(_.declarationType))

  val additionalSupplyChainActor = ie029Data.data.Consignment.additionalSupplyChainActorsRole.getOrElse("")

  val commodityCode: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.CommodityCode.toString))

  val departureTransportMeans: String = ie029Data.data.Consignment.departureTransportMeans.getOrElse("")

  val dangerousGoods: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.dangerousGoods.toString))

  val cusCode: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.cusCode.toString))

  val descriptionOfGoods: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.descriptionOfGoods))

  val previousDocument: Seq[String] =
    ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.flatMap(_.previousDocument.map(_.toString())))

  val supportingDocument: Seq[String] =
    ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.flatMap(_.supportingDocument.map(_.toString())))

  val transportDocument: Seq[String] =
    ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.flatMap(_.transportDocument.map(_.toString())))

  val additionalReference: Seq[String] =
    ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.flatMap(_.additionalReference.map(_.toString())))

  val additionalInformation: Seq[String] =
    ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.flatMap(_.additionalInformation.map(_.toString())))

  val grossMass: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.GoodsMeasure.grossMass.toString))

  val netMass: Seq[String] = ie029Data.data.Consignment.HouseConsignment.flatMap(_.ConsignmentItem.map(_.Commodity.GoodsMeasure.netMass.toString))

}


