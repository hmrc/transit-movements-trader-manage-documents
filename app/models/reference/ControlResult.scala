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

package models.reference
import cats.implicits.catsSyntaxTuple2Semigroupal
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.json.Reads
import play.api.libs.json.Writes

import java.time.LocalDate

final case class ControlResult(code: String, description: String) extends CodedReferenceData
//case class ControlResult(conResCodERS16: String, datLimERS69: LocalDate)

object ControlResult {

  object Constants {
    val controlResultCodeLength = 2
  }

  implicit lazy val reads: Reads[ControlResult] =
    Json.reads[ControlResult]

  implicit lazy val writes: Writes[ControlResult] =
    Json.writes[ControlResult]

  implicit val xmlReader: XmlReader[ControlResult] =
    ((__ \ "ConResCodERS16").read[String], (__ \ "DatLimERS69").read[String])
      .mapN(apply)
}
