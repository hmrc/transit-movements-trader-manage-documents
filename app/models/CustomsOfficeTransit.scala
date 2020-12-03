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
import java.time.LocalDateTime

import com.lucidchart.open.xtract.__
import com.lucidchart.open.xtract.XmlReader
import utils.LocalDateXMLReader._
import cats.syntax.all._
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class CustomsOfficeTransit(referenceNumber: String, arrivalTime: Option[LocalDateTime])

object CustomsOfficeTransit {

  object Constants {
    val customsOfficeLength = 8
  }

  implicit lazy val format: OFormat[CustomsOfficeTransit] =
    Json.format[CustomsOfficeTransit]

  implicit val xmlReader: XmlReader[CustomsOfficeTransit] = (
    (__ \ "RefNumRNS1").read[String],
    (__ \ "ArrTimTRACUS085").read[LocalDateTime].optional
  ).mapN(apply)

}
