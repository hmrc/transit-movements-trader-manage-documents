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

import play.api.libs.json.{Json, Reads, Writes}

sealed trait TraderAtDestination

object TraderAtDestination {

  implicit lazy val reads: Reads[TraderAtDestination] = {

    implicit class ReadsWithContravariantOr[A](a: Reads[A]) {

      def or[B >: A](b: Reads[B]): Reads[B] =
        a.map[B](identity).orElse(b)
    }

    implicit def convertToSupertype[A, B >: A](a: Reads[A]): Reads[B] =
      a.map(identity)

    TraderAtDestinationWithEori.reads or
      TraderAtDestinationWithoutEori.reads
  }

  implicit lazy val writes: Writes[TraderAtDestination] = Writes {
    case t: TraderAtDestinationWithEori    => Json.toJson(t)(TraderAtDestinationWithEori.writes)
    case t: TraderAtDestinationWithoutEori => Json.toJson(t)(TraderAtDestinationWithoutEori.writes)
  }
}

final case class TraderAtDestinationWithEori(
                                              eori: String,
                                              name: Option[String],
                                              streetAndNumber: Option[String],
                                              postCode: Option[String],
                                              city: Option[String],
                                              countryCode: Option[String]
                                            ) extends TraderAtDestination

object TraderAtDestinationWithEori {

  implicit lazy val reads: Reads[TraderAtDestinationWithEori] =
    Json.reads[TraderAtDestinationWithEori]

  implicit lazy val writes: Writes[TraderAtDestinationWithEori] =
    Json.writes[TraderAtDestinationWithEori]
}

final case class TraderAtDestinationWithoutEori (
                                                  name: String,
                                                  streetAndNumber: String,
                                                  postCode: String,
                                                  city: String,
                                                  countryCode: String
                                                ) extends TraderAtDestination

object TraderAtDestinationWithoutEori {

  implicit lazy val reads: Reads[TraderAtDestinationWithoutEori] =
    Json.reads[TraderAtDestinationWithoutEori]

  implicit lazy val writes: Writes[TraderAtDestinationWithoutEori] =
    Json.writes[TraderAtDestinationWithoutEori]
}
