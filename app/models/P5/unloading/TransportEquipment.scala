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

package models.P5.unloading

import models.P5.unloading.TransportEquipment.goodsReferenceToString
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class TransportEquipment(
  sequenceNumber: String,
  containerIdentificationNumber: Option[String],
  numberOfSeals: Int,
  Seal: Option[List[Seal]],
  GoodsReference: Option[List[GoodsReference]]
) {

  override def toString: String = {

    val stringList: Seq[Option[String]] = List(
      Some(sequenceNumber),
      containerIdentificationNumber,
      Some(numberOfSeals.toString),
      goodsReferenceToString(GoodsReference)
    )
    stringList.flatten.mkString(", ")
  }
}

object TransportEquipment {

  def sealToString(Seal: Option[List[Seal]]): String = {

    val sealString = Seal match {
      case Some(firstElem :: Nil) => Some(s"${firstElem.sequenceNumber},[${firstElem.identifier}]")
      case Some(firstElem :: tail) =>
        Some(
          s"${firstElem.sequenceNumber},[${firstElem.identifier}]...${tail.last.sequenceNumber},[${tail.last.identifier}]"
        )
      case _ => Some("")
    }
    sealString.getOrElse("")
  }

  private def goodsReferenceToString(GoodsReference: Option[List[GoodsReference]]): Option[String] = {

    val goodsRefString = GoodsReference match {
      case Some(firstElem :: Nil) => Some(s"${firstElem.sequenceNumber}:${firstElem.declarationGoodsItemNumber}")
      case Some(firstElem :: tail) =>
        Some(
          s"${firstElem.sequenceNumber}:${firstElem.declarationGoodsItemNumber}...${tail.last.sequenceNumber}:${tail.last.declarationGoodsItemNumber}"
        )
      case _ => Some("")
    }
    goodsRefString
  }

  implicit val formats: OFormat[TransportEquipment] = Json.format[TransportEquipment]
}
