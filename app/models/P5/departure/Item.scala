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

import play.api.libs.json._

case class Item(
  declarationGoodsItemNumber: String,
  goodsItemNumber: String,
  packaging: String,
  consignor: String,
  consignee: String,
  referenceNumberUcr: String,
  transportCharges: String,
  countryOfDispatch: String,
  countryOfDestination: String,
  declarationType: String,
  additionalSupplyChainActor: String,
  commodityCode: String,
  departureTransportMeans: String,
  dangerousGoods: String,
  cusCode: String,
  descriptionOfGoods: String,
  previousDocument: String,
  supportingDocument: String,
  transportDocument: String,
  additionalReference: String,
  additionalInformation: String,
  grossMass: String,
  netMass: String
)

object Item {
//  implicit val formats: OFormat[Item] = Json.format[Item]

  implicit val itemReads: Reads[Item] = new Reads[Item] {

    override def reads(json: JsValue): JsResult[Item] =
      JsSuccess(
        Item(
          (json \ "declarationGoodsItemNumber").as[String],
          (json \ "goodsItemNumber").as[String],
          (json \ "packaging").as[String],
          (json \ "consignor").as[String],
          (json \ "consignee").as[String],
          (json \ "referenceNumberUcr").as[String],
          (json \ "transportCharges").as[String],
          (json \ "countryOfDispatch").as[String],
          (json \ "countryOfDestination").as[String],
          (json \ "declarationType").as[String],
          (json \ "additionalSupplyChainActor").as[String],
          (json \ "commodityCode").as[String],
          (json \ "departureTransportMeans").as[String],
          (json \ "dangerousGoods").as[String],
          (json \ "cusCode").as[String],
          (json \ "descriptionOfGoods").as[String],
          (json \ "previousDocument").as[String],
          (json \ "supportingDocument").as[String],
          (json \ "transportDocument").as[String],
          (json \ "additionalReference").as[String],
          (json \ "additionalInformation").as[String],
          (json \ "grossMass").as[String],
          (json \ "netMass").as[String]
        )
      )
  }

  implicit val itemWrites: Writes[Item] = new Writes[Item] {

    override def writes(item: Item): JsValue =
      Json.obj(
        "declarationGoodsItemNumber" -> item.declarationGoodsItemNumber,
        "goodsItemNumber"            -> item.goodsItemNumber,
        "packaging"                  -> item.goodsItemNumber,
        "consignor"                  -> item.goodsItemNumber,
        "consignee"                  -> item.goodsItemNumber,
        "referenceNumberUcr"         -> item.goodsItemNumber,
        "transportCharges"           -> item.goodsItemNumber,
        "countryOfDispatch"          -> item.goodsItemNumber,
        "countryOfDestination"       -> item.goodsItemNumber,
        "declarationType"            -> item.goodsItemNumber,
        "additionalSupplyChainActor" -> item.goodsItemNumber,
        "commodityCode"              -> item.goodsItemNumber,
        "departureTransportMeans"    -> item.goodsItemNumber,
        "dangerousGoods"             -> item.goodsItemNumber,
        "cusCode"                    -> item.goodsItemNumber,
        "descriptionOfGoods"         -> item.goodsItemNumber,
        "previousDocument"           -> item.goodsItemNumber,
        "supportingDocument"         -> item.goodsItemNumber,
        "transportDocument"          -> item.goodsItemNumber,
        "additionalReference"        -> item.goodsItemNumber,
        "additionalInformation"      -> item.goodsItemNumber,
        "grossMass"                  -> item.goodsItemNumber,
        "netMass"                    -> item.goodsItemNumber
      )
  }

}
