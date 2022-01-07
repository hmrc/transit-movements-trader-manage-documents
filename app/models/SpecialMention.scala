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

import cats.implicits.catsSyntaxTuple4Semigroupal
import com.lucidchart.open.xtract._
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class SpecialMention(
  additionalInformation: Option[String],
  additionalInformationCoded: String,
  exportFromEC: Option[Boolean],
  exportFromCountry: Option[String]
)

object SpecialMention {

  val calCode = "CAL"

  implicit val format: OFormat[SpecialMention] = Json.format[SpecialMention]

  import utils.XMLReadersImplicits.OptionalXmlReaderOps

  implicit val xmlReads: XmlReader[SpecialMention] = (
    (__ \ "AddInfMT21").read[String].validateOnlyIfPresent,
    (__ \ "AddInfCodMT23").read[String],
    (__ \ "ExpFroECMT24").read[Int].map(_ == 1).validateOnlyIfPresent,
    (__ \ "ExpFroCouMT25").read[String].validateOnlyIfPresent
  ).mapN(SpecialMention.apply)
}
