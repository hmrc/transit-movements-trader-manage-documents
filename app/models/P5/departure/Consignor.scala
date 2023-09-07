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

case class Consignor(
  identificationNumber: Option[String],
  name: Option[String],
  address: Option[Address],
  contactPerson: Option[ContactPerson]
) {

  val toP4: models.Consignor = models.Consignor(
    name.getOrElse(""),
    address.map(_.streetAndNumber).getOrElse(""),
    address.flatMap(_.postcode).getOrElse(""),
    address.map(_.city).getOrElse(""),
    address.map(_.country).getOrElse(""),
    None,
    identificationNumber
  )

  override def toString: String = {
    val stringList: Seq[Option[String]] = List(name, address.map(_.toString), contactPerson.map(_.toString))
    stringList.flatten.mkString(", ")
  }
}

object Consignor {
  implicit val formats: OFormat[Consignor] = Json.format[Consignor]
}
