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

import com.lucidchart.open.xtract.XmlReader._
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import cats.syntax.all._

final case class TransitAccompanyingDocument(
  localReferenceNumber: String,
  declarationType: DeclarationType,
  countryOfDispatch: Option[String],
  countryOfDestination: Option[String]
)

object TransitAccompanyingDocument {

  implicit lazy val format: OFormat[TransitAccompanyingDocument] =
    Json.format[TransitAccompanyingDocument]

  implicit val xmlReader: XmlReader[TransitAccompanyingDocument] = {
    ((__ \ "HEAHEA" \ "RefNumHEA4").read[String],
     (__ \ "HEAHEA" \ "TypOfDecHEA24").read[DeclarationType],
     (__ \ "HEAHEA" \ "CouOfDisCodHEA55").read[String].optional,
     (__ \ "HEAHEA" \ "CouOfDesCodHEA30").read[String].optional).mapN(apply)
  }
}
