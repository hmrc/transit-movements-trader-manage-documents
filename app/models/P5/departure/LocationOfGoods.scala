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

import scala.collection.immutable

case class LocationOfGoods(
  typeOfLocation: String,
  qualifierOfIdentification: String,
  authorisationNumber: Option[String],
  additionalIdentifier: Option[String],
  UNLocode: Option[String],
  CustomsOffice: Option[CustomsOffice],
  GNSS: Option[GNSS],
  EconomicOperator: Option[EconomicOperator],
  Address: Option[Address],
  PostcodeAddress: Option[PostcodeAddress],
  ContactPerson: Option[ContactPerson]
) {

  override def toString: String = {
    val stringList: Seq[Option[String]] = List(
      Some(typeOfLocation),
      Some(qualifierOfIdentification),
      authorisationNumber,
      UNLocode,
      CustomsOffice.map(_.referenceNumber),
      GNSS.map(_.toString),
      EconomicOperator.map(_.identificationNumber),
      Address.map(_.toString),
      PostcodeAddress.map(_.toString)
    )

    stringList.flatten.mkString(", ")
  }
}

object LocationOfGoods {
  implicit val formats: OFormat[LocationOfGoods] = Json.format[LocationOfGoods]
}

case class CustomsOffice(referenceNumber: String)

object CustomsOffice {
  implicit val formats: OFormat[CustomsOffice] = Json.format[CustomsOffice]
}

case class GNSS(latitude: String, longitude: String) {

  override def toString: String = {
    val stringList: Seq[String] = List(latitude, longitude)
    stringList.mkString(", ")
  }
}

object GNSS {
  implicit val formats: OFormat[GNSS] = Json.format[GNSS]
}

case class EconomicOperator(identificationNumber: String)

object EconomicOperator {
  implicit val formats: OFormat[EconomicOperator] = Json.format[EconomicOperator]
}

case class PostcodeAddress(houseNumber: Option[String], postcode: String, country: String) {

  override def toString: String = {
    val stringList: immutable.Seq[Option[String]] = List(houseNumber, Some(postcode), Some(country))
    stringList.flatten.mkString(", ")
  }
}

object PostcodeAddress {
  implicit val formats: OFormat[PostcodeAddress] = Json.format[PostcodeAddress]
}
