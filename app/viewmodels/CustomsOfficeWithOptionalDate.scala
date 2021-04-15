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

package viewmodels

import models.reference.CustomsOffice
import utils.DateFormatter
import utils.StringTransformer

import java.time.LocalDateTime

case class CustomsOfficeWithOptionalDate(office: CustomsOffice, date: Option[LocalDateTime], maxLength: Int = 45, trimmed: String = "***") {
  import StringTransformer._

  def reference: String =
    office.name
      .map(name => s"$name (${office.countryId})")
      .getOrElse(office.id)
      .shorten(maxLength)(trimmed)

  def dateTimeFormatted: Option[String] =
    date.map(_.format(DateFormatter.dateTimeFormatter))

  def arrivalDateTimeFormatted: Option[String] =
    date.map(_.format(DateFormatter.arrivalDateTimeFormatter))
}
