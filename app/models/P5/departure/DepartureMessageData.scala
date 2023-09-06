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
import play.api.libs.json.Reads
import play.api.libs.json.Writes

case class DepartureMessageData(
  TransitOperation: TransitOperation,
  HolderOfTheTransitProcedure: HolderOfTransitProcedure,
  Representative: Representative,
  Consignment: Consignment,
  Guarantee: Option[List[Guarantee]],
  Authorisation: Option[List[Authorisation]],
  Seals: Option[List[Seal]],
  Items: List[Item],
  CustomsOfficeOfTransitDeclared: Option[List[CustomsOfficeOfTransitDeclared]],
  CustomsOfficeOfExitForTransitDeclared: Option[List[CustomsOfficeOfExitForTransitDeclared]],
  CustomsOfficeOfDeparture: CustomsOfficeOfDeparture,
  CustomsOfficeOfDestinationDeclared: CustomsOfficeOfDestinationDeclared
) {

  val guarantee: Option[String] = Guarantee.map(
    _.map(_.toString).mkString("; ")
  )

  val seals: Seq[String] = Seals
    .map(
      _.map(_.toString)
    )
    .getOrElse(Nil)

  val authorisation: Option[String] = Authorisation.map(
    _.map(_.toString).mkString("; ")
  )

  val customsOfficeOfTransitDeclared: Option[String] = CustomsOfficeOfTransitDeclared.map(
    _.map(_.toString).mkString("; ")
  )

  val customsOfficeOfExitForTransitDeclared: Option[String] = CustomsOfficeOfExitForTransitDeclared.map(
    _.map(_.toString).mkString("; ")
  )

  val customsOfficeOfDeparture: String           = CustomsOfficeOfDeparture.toString
  val customsOfficeOfDestinationDeclared: String = CustomsOfficeOfDestinationDeclared.toString

}

object DepartureMessageData {
  implicit val reads: Reads[DepartureMessageData]   = Json.reads[DepartureMessageData]
  implicit val writes: Writes[DepartureMessageData] = Json.writes[DepartureMessageData]

}
