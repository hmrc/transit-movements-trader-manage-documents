/*
 * Copyright 2022 HM Revenue & Customs
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
import cats.syntax.all._
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import com.lucidchart.open.xtract.XmlReader._
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import utils.BigDecimalXMLReader._
import utils.NonEmptyListXMLReader._
import json.NonEmptyListOps._

final case class GoodsItem(
  itemNumber: Int,
  commodityCode: Option[String],
  declarationType: Option[DeclarationType],
  description: String,
  grossMass: Option[BigDecimal],
  netMass: Option[BigDecimal],
  countryOfDispatch: Option[String],
  countryOfDestination: Option[String],
  methodOfPayment: Option[String],
  commercialReferenceNumber: Option[String],
  unDangerGoodsCode: Option[String],
  previousAdminRef: Seq[PreviousAdministrativeReference],
  producedDocuments: Seq[ProducedDocument],
  specialMentions: Seq[SpecialMention],
  consignor: Option[Consignor],
  consignee: Option[Consignee],
  containers: Seq[String],
  packages: NonEmptyList[Package],
  sensitiveGoodsInformation: Seq[SensitiveGoodsInformation],
  securityConsignor: Option[SecurityConsignor],
  securityConsignee: Option[SecurityConsignee]
)

object GoodsItem {

  implicit lazy val format: OFormat[GoodsItem] = Json.format[GoodsItem]

  implicit val xmlReader: XmlReader[GoodsItem] =
    (
      (__ \ "IteNumGDS7").read[Int],
      (__ \ "ComCodTarCodGDS10").read[String].optional,
      (__ \ "DecTypGDS15").read[DeclarationType].optional,
      (__ \ "GooDesGDS23").read[String],
      (__ \ "GroMasGDS46").read[BigDecimal].optional,
      (__ \ "NetMasGDS48").read[BigDecimal].optional,
      (__ \ "CouOfDisGDS58").read[String].optional,
      (__ \ "CouOfDesGDS59").read[String].optional,
      (__ \ "MetOfPayGDI12").read[String].optional,
      (__ \ "ComRefNumGIM1").read[String].optional,
      (__ \ "UNDanGooCodGDI1").read[String].optional,
      (__ \ "PREADMREFAR2").read(strictReadSeq[PreviousAdministrativeReference]),
      (__ \ "PRODOCDC2").read(strictReadSeq[ProducedDocument]),
      (__ \ "SPEMENMT2").read(strictReadSeq[SpecialMention]),
      (__ \ "TRACONCO2").read[Consignor](Consignor.xmlReaderGoodsLevel).optional,
      (__ \ "TRACONCE2").read[Consignee](Consignee.xmlReaderGoodsLevel).optional,
      (__ \ "CONNR2" \ "ConNumNR21").read(strictReadSeq[String]),
      (__ \ "PACGS2").read(xmlNonEmptyListReads[Package]),
      (__ \ "SGICODSD2").read(seq[SensitiveGoodsInformation]),
      (__ \ "TRACORSECGOO021").read[SecurityConsignor](SecurityConsignor.xmlReader).optional,
      (__ \ "TRACONSECGOO013").read[SecurityConsignee](SecurityConsignee.xmlReader).optional
    ).mapN(apply)
}
