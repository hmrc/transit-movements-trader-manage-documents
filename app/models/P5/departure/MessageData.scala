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

sealed trait MessageData

case class IE015MessageData(TransitOperation: IE015TransitOperation) extends MessageData

object IE015MessageData {
  implicit val reads: Reads[IE015MessageData] = Json.reads[IE015MessageData]
}

case class IE029MessageData(
  TransitOperation: IE029TransitOperation,
  Authorisation: Option[List[Authorisation]],
  CustomsOfficeOfDeparture: CustomsOfficeOfDeparture,
  CustomsOfficeOfDestinationDeclared: CustomsOfficeOfDestinationDeclared,
  CustomsOfficeOfTransitDeclared: Option[List[CustomsOfficeOfTransitDeclared]],
  CustomsOfficeOfExitForTransitDeclared: Option[List[CustomsOfficeOfExitForTransitDeclared]],
  HolderOfTheTransitProcedure: HolderOfTransitProcedure,
  Representative: Representative,
  ControlResult: Option[ControlResult],
  Guarantee: Option[List[Guarantee]],
  Consignment: Consignment
) extends MessageData {

  val guaranteeDisplay: Option[String] = Guarantee.map(
    _.map(_.toString).mkString("; ")
  )

  val authorisationDisplay: Option[String] = Authorisation.map(
    authorisations =>
      authorisations.length match {
        case 0 => ""
        case 1 => authorisations.head.toString
        case _ => s"${authorisations.head.toString}..."
      }
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

object IE029MessageData {
  implicit val reads: Reads[IE029MessageData] = Json.reads[IE029MessageData]
}
