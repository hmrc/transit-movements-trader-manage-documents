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
  transitOperation: TransitOperation,
  authorisation: Option[List[Authorisation]],
  customsOfficeOfDeparture: CustomsOfficeOfDeparture,
  customsOfficeOfDestinationDeclared: CustomsOfficeOfDestinationDeclared,
  customsOfficeOfTransitDeclared: Option[List[CustomsOfficeOfTransitDeclared]],
  customsOfficeOfExitForTransitDeclared: Option[List[CustomsOfficeOfExitForTransitDeclared]],
  holderOfTheTransitProcedure: HolderOfTransitProcedure,
  representative: Representative,
  controlResult: ControlResult,
  guarantee: Option[List[Guarantee]],
  consignment: Consignment
) {

  val guaranteeDisplay: Option[String] = guarantee.map(
    _.map(_.toString).mkString("; ")
  )

  val authorisationDisplay: Option[String] = authorisation.map(
    _.map(_.toString).mkString("; ")
  )

  val customsOfficeOfTransitDeclaredDisplay: Option[String] = customsOfficeOfTransitDeclared.map(
    _.map(_.toString).mkString("; ")
  )

  val customsOfficeOfExitForTransitDeclaredDisplay: Option[String] = customsOfficeOfExitForTransitDeclared.map(
    _.map(_.toString).mkString("; ")
  )

  val customsOfficeOfDepartureDisplay: String           = customsOfficeOfDeparture.toString
  val customsOfficeOfDestinationDeclaredDisplay: String = customsOfficeOfDestinationDeclared.toString

}

object DepartureMessageData {
  implicit val reads: Reads[DepartureMessageData]   = Json.reads[DepartureMessageData]
  implicit val writes: Writes[DepartureMessageData] = Json.writes[DepartureMessageData]

}
