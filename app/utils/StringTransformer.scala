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
import java.time.format.DateTimeFormatter

case class ShortenedString(originalValue: String, maxlength: Int, shortener: String) {
  import StringTransformer._

  def trimmed: String = originalValue.shorten(maxlength)(shortener)
}

case class FormattedDate(originalValue: LocalDate, formatter: DateTimeFormatter = DateFormatter.readableDateFormatter) {
  def formattedDate: String = originalValue.format(formatter)
}

case class FormattedDateTime(originalValue: LocalDateTime, formatter: DateTimeFormatter = DateFormatter.dateTimeFormatter) {
  def formattedDate: String = originalValue.format(formatter)
}

object StringTransformer {

  implicit class StringFormatter(val x: String) {

    val shorten: Int => String => String = {
      maxLength => concealCharacters =>
        if (x.length > maxLength) {
          if (x.length <= concealCharacters.length) {
            x.slice(0, maxLength)
          } else x.slice(0, maxLength - concealCharacters.length) + concealCharacters
        } else x
    }
  }

}
