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

package models

import cats.data.NonEmptyList
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import json.NonEmptyListOps._

final case class GoodsItem(
  itemNumber: Int,
  commodityCode: Option[String],
  declarationType: Option[DeclarationType],
  description: String,
  grossMass: Option[BigDecimal],
  netMass: Option[BigDecimal],
  countryOfDispatch: String,
  countryOfDestination: String,
  producedDocuments: Seq[ProducedDocument],
  specialMentions: Seq[SpecialMention],
  consignor: Option[Consignor],
  consignee: Option[Consignee],
  containers: Seq[String],
  packages: NonEmptyList[Package]
)

object GoodsItem {

  implicit lazy val format: OFormat[GoodsItem] = Json.format[GoodsItem]

//  implicit val xmlReader: XmlReader[GoodsItem] = (
//    (__ \ "IteNumGDS7").read[Int],
//    (__ \ "ComCodTarCodGDS10").read[String].optional,
//    (__ \ "DecTypGDS15").read[DeclarationType].optional,
//    (__ \ "GooDesGDS23").read[String],
//    (__ \ "GroMasGDS46").read[BigDecimal].optional,
//    (__ \ "NetMasGDS48").read[BigDecimal].optional,
//    (__ \ "CouOfDisGDS58").read[String],
//    (__ \ "CouOfDesGDS59").read[String],
//    (__ \ "PRODOCDC2").read[ProducedDocument],
//    (__ \ "SPEMENMT2").read[SpecialMention],
//    (__ \ "TRACONCO2").read[Consignor],
//    (__ \ "TRACONCE2").read[Consignee],
//    (__ \ "CONNR2").read(seq[String]),
//    (__ \ "PACGS2").read(seq[Package])
//  ).mapN(apply)
}
