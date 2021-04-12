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

final case class SecurityConsignor(
  name: String,
  streetAndNumber: String,
  postCode: String,
  city: String,
  countryCode: String,
  nadLanguageCode: Option[String],
  eori: Option[String]
)

object SecurityConsignor {

  implicit lazy val format: OFormat[SecurityConsignor] = Json.format[SecurityConsignor]

  implicit val xmlReader: XmlReader[SecurityConsignor] = (
    (__ \ "NamTRACORSECGOO025").read[String],
    (__ \ "StrNumTRACORSECGOO027").read[String],
    (__ \ "PosCodTRACORSECGOO026").read[String],
    (__ \ "CitTRACORSECGOO022").read[String],
    (__ \ "CouCodTRACORSECGOO023").read[String],
    (__ \ "TRACORSECGOO021LNG").read[String].optional,
    (__ \ "TINTRACORSECGOO028").read[String].optional,
  ).mapN(apply)

  implicit val xmlReaderRootLevel: XmlReader[SecurityConsignor] = (
    (__ \ "NamTRACORSEC041").read[String],
    (__ \ "StrNumTRACORSEC043").read[String],
    (__ \ "PosCodTRACORSEC042").read[String],
    (__ \ "CitTRACORSEC038").read[String],
    (__ \ "CouCodTRACORSEC039").read[String],
    (__ \ "TRACORSEC037LNG").read[String].optional,
    (__ \ "TINTRACORSEC044").read[String].optional,
  ).mapN(apply)

}
