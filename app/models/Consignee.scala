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

import cats.syntax.all._
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json.OFormat

final case class Consignee(
  name: String,
  streetAndNumber: String,
  postCode: String,
  city: String,
  countryCode: String,
  nadLanguageCode: Option[String],
  eori: Option[String]
)

object Consignee {

  implicit lazy val format: OFormat[Consignee] = Json.format[Consignee]

  implicit val xmlReaderGoodsLevel: XmlReader[Consignee] = {
    (
      (__ \ "NamCE27").read[String],
      (__ \ "StrAndNumCE222").read[String],
      (__ \ "PosCodCE223").read[String],
      (__ \ "CitCE224").read[String],
      (__ \ "CouCE225").read[String],
      (__ \ "NADLNGGICE").read[String].optional,
      (__ \ "TINCE259").read[String].optional
    ).mapN(apply)
  }

  implicit val xmlReaderRootLevel: XmlReader[Consignee] = {
    (
      (__ \ "NamCE17").read[String],
      (__ \ "StrAndNumCE122").read[String],
      (__ \ "PosCodCE123").read[String],
      (__ \ "CitCE124").read[String],
      (__ \ "CouCE125").read[String],
      (__ \ "NADLNGCE").read[String].optional,
      (__ \ "TINCE159").read[String].optional
    ).mapN(apply)
  }
}
