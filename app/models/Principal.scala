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

import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import cats.syntax.all._

final case class Principal(
  name: String,
  streetAndNumber: String,
  postCode: String,
  city: String,
  countryCode: String,
  eori: Option[String],
  tir: Option[String]
)

object Principal {

  implicit lazy val reads: Reads[Principal] =
    Json.reads[Principal]

  implicit lazy val writes: Writes[Principal] =
    Json.writes[Principal]

  implicit val xmlReads: XmlReader[Principal] =
    (
      (__ \ "NamPC17").read[String],
      (__ \ "StrAndNumPC122").read[String],
      (__ \ "PosCodPC123").read[String],
      (__ \ "CitPC124").read[String],
      (__ \ "CouPC125").read[String],
      (__ \ "TINPC159").read[String].optional,
      (__ \ "HITPC126").read[String].optional
    ).mapN(apply)
}
