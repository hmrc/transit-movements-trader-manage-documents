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

package models

import cats.implicits.catsSyntaxTuple5Semigroupal
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json._

sealed trait SecurityConsignee

object SecurityConsignee {

  implicit lazy val reads: Reads[SecurityConsignee] = {

    implicit class ReadsWithContravariantOr[A](a: Reads[A]) {

      def or[B >: A](b: Reads[B]): Reads[B] =
        a.map[B](identity).orElse(b)
    }

    implicit def convertToSupertype[A, B >: A](a: Reads[A]): Reads[B] =
      a.map(identity)

    SecurityConsigneeWithEori.reads or
      SecurityConsigneeWithoutEori.reads
  }

  implicit lazy val writes: Writes[SecurityConsignee] = Writes {
    case t: SecurityConsigneeWithEori    => Json.toJson(t)(SecurityConsigneeWithEori.writes)
    case t: SecurityConsigneeWithoutEori => Json.toJson(t)(SecurityConsigneeWithoutEori.writes)
  }

  implicit val xmlReader: XmlReader[SecurityConsignee] =
    SecurityConsigneeWithEori.xmlReader
      .or(SecurityConsigneeWithoutEori.xmlReader)

  implicit val xmlReaderRootLevel: XmlReader[SecurityConsignee] =
    SecurityConsigneeWithEori.xmlRootLevelReader
      .or(SecurityConsigneeWithoutEori.xmlRootLevelReader)
}

final case class SecurityConsigneeWithEori(eori: String) extends SecurityConsignee

object SecurityConsigneeWithEori {

  implicit lazy val reads: Reads[SecurityConsigneeWithEori] = Json.reads[SecurityConsigneeWithEori]

  implicit lazy val writes: Writes[SecurityConsigneeWithEori] = Json.writes[SecurityConsigneeWithEori]

  implicit val xmlReader: XmlReader[SecurityConsigneeWithEori] = (__ \ "TINTRACONSECGOO020").read[String].map(apply)

  implicit val xmlRootLevelReader: XmlReader[SecurityConsigneeWithEori] = (__ \ "TINTRACONSEC036").read[String].map(apply)

}

final case class SecurityConsigneeWithoutEori(
  name: String,
  streetAndNumber: String,
  postCode: String,
  city: String,
  countryCode: String
) extends SecurityConsignee

object SecurityConsigneeWithoutEori {

  implicit lazy val reads: Reads[SecurityConsigneeWithoutEori] =
    Json.reads[SecurityConsigneeWithoutEori]

  implicit lazy val writes: Writes[SecurityConsigneeWithoutEori] =
    Json.writes[SecurityConsigneeWithoutEori]

  implicit val xmlReader: XmlReader[SecurityConsigneeWithoutEori] = (
    (__ \ "NamTRACONSECGOO017").read[String],
    (__ \ "StrNumTRACONSECGOO019").read[String],
    (__ \ "PosCodTRACONSECGOO018").read[String],
    (__ \ "CityTRACONSECGOO014").read[String],
    (__ \ "CouCodTRACONSECGOO015").read[String]
  ).mapN(apply)

  implicit val xmlRootLevelReader: XmlReader[SecurityConsigneeWithoutEori] = (
    (__ \ "NameTRACONSEC033").read[String],
    (__ \ "StrNumTRACONSEC035").read[String],
    (__ \ "PosCodTRACONSEC034").read[String],
    (__ \ "CitTRACONSEC030").read[String],
    (__ \ "CouCodTRACONSEC031").read[String]
  ).mapN(apply)

}
