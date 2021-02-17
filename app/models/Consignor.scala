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

package models

import cats.syntax.all._
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json.OFormat

final case class Consignor(
  name: String,
  streetAndNumber: String,
  postCode: String,
  city: String,
  countryCode: String,
  nadLanguageCode: Option[String],
  eori: Option[String]
)

object Consignor {

  implicit lazy val format: OFormat[Consignor] = Json.format[Consignor]

  implicit val xmlReaderGoodsLevel: XmlReader[Consignor] = {
    (
      (__ \ "NamCO27").read[String],
      (__ \ "StrAndNumCO222").read[String],
      (__ \ "PosCodCO223").read[String],
      (__ \ "CitCO224").read[String],
      (__ \ "CouCO225").read[String],
      (__ \ "NADLNGGTCO").read[String].optional,
      (__ \ "TINCO259").read[String].optional
    ).mapN(apply)
  }

  implicit val xmlReaderRootLevel: XmlReader[Consignor] = {
    (
      (__ \ "NamCO17").read[String],
      (__ \ "StrAndNumCO122").read[String],
      (__ \ "PosCodCO123").read[String],
      (__ \ "CitCO124").read[String],
      (__ \ "CouCO125").read[String],
      (__ \ "NADLNGCO").read[String].optional,
      (__ \ "TINCO159").read[String].optional
    ).mapN(apply)
  }
}
