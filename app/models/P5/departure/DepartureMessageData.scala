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
  Authorisation: Option[List[Authorisation]],
  CustomsOfficeOfDeparture: CustomsOfficeOfDeparture,
  CustomsOfficeOfDestinationDeclared: CustomsOfficeOfDestinationDeclared,
  CustomsOfficeOfTransitDeclared: Option[List[CustomsOfficeOfTransitDeclared]],
  CustomsOfficeOfExitForTransitDeclared: Option[List[CustomsOfficeOfExitForTransitDeclared]],
  HolderOfTheTransitProcedure: HolderOfTransitProcedure,
  Representative: Representative,
  ControlResult: ControlResult,
  Guarantee: Option[List[Guarantee]],
  Consignment: Consignment
) {

  val guaranteeDisplay: Option[String] = Guarantee.map(
    _.map(_.toString).mkString("; ")
  )

  val authorisationDisplay: Option[String] = Authorisation.map(
    _.map(_.toString).mkString("; ")
  )

  val customsOfficeOfTransitDeclaredDisplay: Option[String] = CustomsOfficeOfTransitDeclared.map(
    _.map(_.toString).mkString("; ")
  )

  val customsOfficeOfExitForTransitDeclaredDisplay: Option[String] = CustomsOfficeOfExitForTransitDeclared.map(
    _.map(_.toString).mkString("; ")
  )

  val customsOfficeOfDepartureDisplay: String           = CustomsOfficeOfDeparture.toString
  val customsOfficeOfDestinationDeclaredDisplay: String = CustomsOfficeOfDestinationDeclared.toString

}

object DepartureMessageData {
  implicit val reads: Reads[DepartureMessageData]   = Json.reads[DepartureMessageData]
  implicit val writes: Writes[DepartureMessageData] = Json.writes[DepartureMessageData]

}
