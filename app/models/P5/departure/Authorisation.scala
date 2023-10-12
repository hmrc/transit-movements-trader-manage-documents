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

package models.P5.departure

import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class Authorisation(
  sequenceNumber: String,
  `type`: String,
  referenceNumber: String
) {

  override def toString: String = {
    val stringList: List[String] = List(
      sequenceNumber,
      `type`,
      referenceNumber
    )
    stringList.mkString(", ")
  }
}

object Authorisation {
  implicit val formats: OFormat[Authorisation] = Json.format[Authorisation]
}
