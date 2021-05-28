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

package utils

import java.time.LocalDate
import java.time.LocalDateTime
import com.lucidchart.open.xtract.ParseError
import com.lucidchart.open.xtract.ParseFailure
import com.lucidchart.open.xtract.ParseSuccess
import com.lucidchart.open.xtract.XmlReader
import utils.DateFormatter.dateFormatter

import scala.util.Failure
import scala.util.Success
import scala.util.Try
import scala.xml.NodeSeq

object LocalDateXMLReader {

  case class LocalDateParseFailure(message: String)     extends ParseError
  case class LocalDateTimeParseFailure(message: String) extends ParseError

  implicit val xmlDateReads: XmlReader[LocalDate] = {
    xml =>
      Try(LocalDate.parse(xml.text, dateFormatter)) match {
        case Success(value) => ParseSuccess(value)
        case Failure(e)     => ParseFailure(LocalDateParseFailure(e.getMessage))
      }
  }

  implicit val xmlDateTimeReads: XmlReader[LocalDateTime] = {
    (xml: NodeSeq) =>
      Try(LocalDateTime.parse(xml.text, DateFormatter.dateTimeFormatter)) match {
        case Success(value) => ParseSuccess(value)
        case Failure(e)     => ParseFailure(LocalDateTimeParseFailure(e.getMessage))
      }
  }

}
