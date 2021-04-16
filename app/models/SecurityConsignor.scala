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

import cats.implicits.catsSyntaxTuple5Semigroupal
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json._

sealed trait SecurityConsignor

object SecurityConsignor {

  implicit lazy val reads: Reads[SecurityConsignor] = {

    implicit class ReadsWithContravariantOr[A](a: Reads[A]) {

      def or[B >: A](b: Reads[B]): Reads[B] =
        a.map[B](identity).orElse(b)
    }

    implicit def convertToSupertype[A, B >: A](a: Reads[A]): Reads[B] =
      a.map(identity)

    SecurityConsignorWithEori.reads or
      SecurityConsignorWithoutEori.reads
  }

  implicit lazy val writes: Writes[SecurityConsignor] = Writes {
    case t: SecurityConsignorWithEori    => Json.toJson(t)(SecurityConsignorWithEori.writes)
    case t: SecurityConsignorWithoutEori => Json.toJson(t)(SecurityConsignorWithoutEori.writes)
  }

  implicit val xmlReader: XmlReader[SecurityConsignor] =
    SecurityConsignorWithEori.xmlReader
      .or(SecurityConsignorWithoutEori.xmlReader)

  implicit val xmlReaderRootLevel: XmlReader[SecurityConsignor] =
    SecurityConsignorWithEori.xmlRootLevelReader
      .or(SecurityConsignorWithoutEori.xmlRootLevelReader)
}

final case class SecurityConsignorWithEori(eori: String) extends SecurityConsignor

object SecurityConsignorWithEori {

  implicit lazy val reads: Reads[SecurityConsignorWithEori] = Json.reads[SecurityConsignorWithEori]

  implicit lazy val writes: Writes[SecurityConsignorWithEori] = Json.writes[SecurityConsignorWithEori]

  implicit val xmlReader: XmlReader[SecurityConsignorWithEori] = (__ \ "TINTRACORSECGOO028").read[String].map(apply)

  implicit val xmlRootLevelReader: XmlReader[SecurityConsignorWithEori] = (__ \ "TINTRACORSEC044").read[String].map(apply)

}

final case class SecurityConsignorWithoutEori(
  name: String,
  streetAndNumber: String,
  postCode: String,
  city: String,
  countryCode: String
) extends SecurityConsignor

object SecurityConsignorWithoutEori {
  implicit lazy val reads: Reads[SecurityConsignorWithoutEori] =
    Json.reads[SecurityConsignorWithoutEori]

  implicit lazy val writes: Writes[SecurityConsignorWithoutEori] =
    Json.writes[SecurityConsignorWithoutEori]

  implicit val xmlReader: XmlReader[SecurityConsignorWithoutEori] = (
    (__ \ "NamTRACORSECGOO025").read[String],
    (__ \ "StrNumTRACORSECGOO027").read[String],
    (__ \ "PosCodTRACORSECGOO026").read[String],
    (__ \ "CitTRACORSECGOO022").read[String],
    (__ \ "CouCodTRACORSECGOO023").read[String]
  ).mapN(apply)

  implicit val xmlRootLevelReader: XmlReader[SecurityConsignorWithoutEori] = (
    (__ \ "NamTRACORSEC041").read[String],
    (__ \ "StrNumTRACORSEC043").read[String],
    (__ \ "PosCodTRACORSEC042").read[String],
    (__ \ "CitTRACORSEC038").read[String],
    (__ \ "CouCodTRACORSEC039").read[String],
  ).mapN(apply)

}
