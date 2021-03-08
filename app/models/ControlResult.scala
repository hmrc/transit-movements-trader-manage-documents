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

import cats.syntax.all._
import com.lucidchart.open.xtract.XmlReader
import com.lucidchart.open.xtract.__
import utils.DateFormatter
import utils.LocalDateXMLReader._

import java.time.LocalDate

case class ControlResult(conResCodERS16: String, datLimERS69: LocalDate) {
  lazy val formattedDate: String = datLimERS69.format(DateFormatter.dateFormatter)
  def isPrintable: Boolean       = conResCodERS16 == "A3"
}

object ControlResult {

  object Constants {
    val controlResultCodeLength = 2
  }

  implicit val xmlReader: XmlReader[ControlResult] =
    ((__ \ "ConResCodERS16").read[String], (__ \ "DatLimERS69").read[LocalDate])
      .mapN(apply)
}
