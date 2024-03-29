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

import cats.implicits.catsSyntaxTuple2Semigroupal
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import utils.DateFormatter
import utils.LocalDateXMLReader

import java.time.LocalDateTime

case class CustomsOfficeOfTransit(reference: String, arrivalTime: Option[LocalDateTime]) {
  val formattedDate: Option[String] = arrivalTime.map(_.format(DateFormatter.dateTimeFormatter))
}

object CustomsOfficeOfTransit {
  implicit lazy val format: OFormat[CustomsOfficeOfTransit] = Json.format[CustomsOfficeOfTransit]

  implicit val xmlReads: XmlReader[CustomsOfficeOfTransit] = (
    (__ \ "RefNumRNS1").read[String],
    (__ \ "ArrTimTRACUS085").read(LocalDateXMLReader.xmlDateTimeReads).optional
  ).mapN(CustomsOfficeOfTransit.apply)
}
