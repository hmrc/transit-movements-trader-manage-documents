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

import cats.syntax.all._
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes

sealed trait SafetyAndSecurityCarrier

object SafetyAndSecurityCarrier {

  implicit lazy val reads: Reads[SafetyAndSecurityCarrier] = {

    implicit class ReadsWithContravariantOr[A](a: Reads[A]) {

      def or[B >: A](b: Reads[B]): Reads[B] =
        a.map[B](identity).orElse(b)
    }

    implicit def convertToSupertype[A, B >: A](a: Reads[A]): Reads[B] =
      a.map(identity)

    SafetyAndSecurityCarrierWithEori.reads or
      SafetyAndSecurityCarrierWithoutEori.reads
  }

  implicit lazy val writes: Writes[SafetyAndSecurityCarrier] = Writes {
    case t: SafetyAndSecurityCarrierWithEori    => Json.toJson(t)(SafetyAndSecurityCarrierWithEori.writes)
    case t: SafetyAndSecurityCarrierWithoutEori => Json.toJson(t)(SafetyAndSecurityCarrierWithoutEori.writes)
  }

  implicit val xmlReader: XmlReader[SafetyAndSecurityCarrier] =
    SafetyAndSecurityCarrierWithEori.xmlReader
      .or(SafetyAndSecurityCarrierWithoutEori.xmlReader)
}

final case class SafetyAndSecurityCarrierWithEori(eori: String) extends SafetyAndSecurityCarrier

object SafetyAndSecurityCarrierWithEori {

  implicit lazy val reads: Reads[SafetyAndSecurityCarrierWithEori] = Json.reads[SafetyAndSecurityCarrierWithEori]

  implicit lazy val writes: Writes[SafetyAndSecurityCarrierWithEori] = Json.writes[SafetyAndSecurityCarrierWithEori]

  implicit val xmlReader: XmlReader[SafetyAndSecurityCarrierWithEori] = (__ \ "TINCARTRA254").read[String].map(apply)
}

final case class SafetyAndSecurityCarrierWithoutEori(
  name: String,
  streetAndNumber: String,
  postCode: String,
  city: String,
  countryCode: String
) extends SafetyAndSecurityCarrier

object SafetyAndSecurityCarrierWithoutEori {

  implicit lazy val reads: Reads[SafetyAndSecurityCarrierWithoutEori] =
    Json.reads[SafetyAndSecurityCarrierWithoutEori]

  implicit lazy val writes: Writes[SafetyAndSecurityCarrierWithoutEori] =
    Json.writes[SafetyAndSecurityCarrierWithoutEori]

  implicit val xmlReader: XmlReader[SafetyAndSecurityCarrierWithoutEori] = (
    (__ \ "NamCARTRA121").read[String],
    (__ \ "StrAndNumCARTRA254").read[String],
    (__ \ "PosCodCARTRA121").read[String],
    (__ \ "CitCARTRA789").read[String],
    (__ \ "CouCodCARTRA587").read[String]
  ).mapN(apply)
}
