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
      Some(goodsReferences)
    )
    stringList.flatten.mkString(", ")
  }

  val display: String = s"$sequenceNumber, ${containerIdentificationNumber.getOrElse("")}"

  def goodsReferences: String = GoodsReference.showFirstAndLast

  val sealsList: Seq[String] = Seal
    .map(
      sealList =>
        sealList.map(
          seal => seal.toString
        )
    )
    .getOrElse(Nil)

  def seal: String = Seal.showFirstAndLast

}

object TransportEquipment {

  implicit val formats: OFormat[TransportEquipment] = Json.format[TransportEquipment]
}
