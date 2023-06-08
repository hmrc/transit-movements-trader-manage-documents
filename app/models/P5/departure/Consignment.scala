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

case class Consignment(
  inlandModeOfTransport: Option[String],
  modeOfTransportAtTheBorder: Option[String],
  referenceNumberUCR: Option[String],
  Consignor: Option[Consignor],
  Consignee: Option[Consignee],
  Carrier: Option[Carrier],
  LocationOfGoods: Option[LocationOfGoods],
  AdditionalSupplyChainActor: Option[List[AdditionalSupplyChainActor]],
  DepartureTransportMeans: Option[List[DepartureTransportMeans]],
  ActiveBorderTransportMeans: Option[List[ActiveBorderTransportMeans]],
  PlaceOfLoading: Option[PlaceOfLoading],
  PlaceOfUnloading: Option[PlaceOfUnloading]
) {

  val additionalSupplyChainActorsRole: Option[String] = AdditionalSupplyChainActor.map(
    _.map(_.toString).mkString("; ")
  )

  val additionalSupplyChainActorIdentificationNumbers: Option[String] = AdditionalSupplyChainActor.map {
    _.map(_.identificationNumber).mkString("; ")
  }

  val departureTransportMeans: Option[String] = DepartureTransportMeans.map(
    _.map(_.toString).mkString("; ")
  )

  val activeBorderTransportMeans: Option[String] = ActiveBorderTransportMeans.map(
    _.map(_.toString).mkString("; ")
  )

  val activeBorderTransportMeansConveyanceNumbers: Option[String] = ActiveBorderTransportMeans.map(
    _.map(_.conveyanceReferenceNumberToString).mkString("; ")
  )
}

object Consignment {
  implicit val formats: OFormat[Consignment] = Json.format[Consignment]
}
