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

import cats.implicits.catsSyntaxTuple7Semigroupal
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json.OFormat

final case class SecurityConsignee(
  name: String,
  streetAndNumber: String,
  postCode: String,
  city: String,
  countryCode: String,
  nadLanguageCode: Option[String],
  eori: Option[String]
)

object SecurityConsignee {

  implicit lazy val format: OFormat[SecurityConsignee] = Json.format[SecurityConsignee]

  implicit val xmlReader: XmlReader[SecurityConsignee] = (
    (__ \ "NamTRACONSECGOO017").read[String],
    (__ \ "StrNumTRACONSECGOO019").read[String],
    (__ \ "PosCodTRACONSECGOO018").read[String],
    (__ \ "CityTRACONSECGOO014").read[String],
    (__ \ "CouCodTRACONSECGOO015").read[String],
    (__ \ "TRACONSECGOO013LNG").read[String].optional,
    (__ \ "TINTRACONSECGOO020").read[String].optional
  ).mapN(apply)

  implicit val xmlReaderRootLevel: XmlReader[SecurityConsignee] = (
    (__ \ "NameTRACONSEC033").read[String],
    (__ \ "StrNumTRACONSEC035").read[String],
    (__ \ "PosCodTRACONSEC034").read[String],
    (__ \ "CitTRACONSEC030").read[String],
    (__ \ "CouCodTRACONSEC031").read[String],
    (__ \ "TRACONSEC029LNG").read[String].optional,
    (__ \ "TINTRACONSEC036").read[String].optional,
  ).mapN(apply)

}
