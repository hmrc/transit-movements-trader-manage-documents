/*
 * Copyright 2019 HM Revenue & Customs
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

import play.api.libs.json._

trait SpecialMention

object SpecialMention {

  implicit lazy val reads: Reads[SpecialMention] = {
    implicit class ReadsWithContravariantOr[A](a: Reads[A]) {

      def or[B >: A](b: Reads[B]): Reads[B] = a.map[B](identity).orElse(b)
    }

    implicit def convertToSupertype[A, B >: A](a: Reads[A]): Reads[B] = a.map(identity)

    SpecialMentionEc.reads or SpecialMentionNonEc.reads
  }

  implicit lazy val writes: OWrites[SpecialMention] = OWrites {
    case ec: SpecialMentionEc     => Json.toJsObject(ec)(SpecialMentionEc.writes)
    case non: SpecialMentionNonEc => Json.toJsObject(non)(SpecialMentionNonEc.writes)
  }
}

final case class SpecialMentionEc(additionalInformationCoded: String) extends SpecialMention

object SpecialMentionEc {

  implicit lazy val reads: Reads[SpecialMentionEc] = {

    import play.api.libs.functional.syntax._

    (__ \ "exportFromEc")
      .read[Boolean]
      .flatMap[Boolean] {
        fromEc =>
          if (fromEc) {
            Reads(_ => JsSuccess(fromEc))
          } else {
            Reads(_ => JsError("exportFromEc must be true"))
          }
      }
      .andKeep((__ \ "additionalInformationCoded").read[String].map(SpecialMentionEc(_)))
  }

  implicit lazy val writes: OWrites[SpecialMentionEc] = {

    import play.api.libs.functional.syntax._

    ((__ \ "exportFromEc").write[Boolean] and (__ \ "additionalInformationCoded").write[String])(s => (true, s.additionalInformationCoded))
  }
}

final case class SpecialMentionNonEc(additionalInformationCoded: String, exportFromCountry: String) extends SpecialMention

object SpecialMentionNonEc {

  implicit lazy val reads: Reads[SpecialMentionNonEc] = {

    import play.api.libs.functional.syntax._

    (__ \ "exportFromEc")
      .read[Boolean]
      .flatMap[Boolean] {
        fromEc =>
          if (fromEc) {
            Reads(_ => JsError("exportFromEc must be false"))
          } else {
            Reads(_ => JsSuccess(fromEc))
          }
      }
      .andKeep(((__ \ "additionalInformationCoded").read[String] and (__ \ "exportFromCountry").read[String])(SpecialMentionNonEc(_, _)))
  }

  implicit lazy val writes: OWrites[SpecialMentionNonEc] = {

    import play.api.libs.functional.syntax._

    ((__ \ "exportFromEc").write[Boolean] and (__ \ "additionalInformationCoded").write[String] and (__ \ "exportFromCountry").write[String])(s =>
      (false, s.additionalInformationCoded, s.exportFromCountry))
  }
}
