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

import models.P5.departure.TransportEquipment.sealToString
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class Consignment(
  grossMass: Double,
  inlandModeOfTransport: Option[String],
  modeOfTransportAtTheBorder: Option[String],
  referenceNumberUCR: Option[String],
  Consignor: Option[Consignor],
  Consignee: Option[Consignee],
  Carrier: Option[Carrier],
  LocationOfGoods: Option[LocationOfGoods],
  AdditionalSupplyChainActor: Option[List[AdditionalSupplyChainActor]],
  DepartureTransportMeans: Option[List[DepartureTransportMeans]],
  TransportEquipment: Option[List[TransportEquipment]],
  ActiveBorderTransportMeans: Option[List[ActiveBorderTransportMeans]],
  PlaceOfLoading: Option[PlaceOfLoading],
  PlaceOfUnloading: Option[PlaceOfUnloading],
  HouseConsignment: Seq[HouseConsignment],
  PreviousDocument: Option[List[PreviousDocument]],
  TransportDocument: Option[List[TransportDocument]]
) {

  val totalPackages: Int = HouseConsignment.foldLeft(0)(
    (total, houseConsignments) => total + houseConsignments.totalPackages
  )

  val totalItems: Int = HouseConsignment.foldLeft(0)(
    (total, houseConsignments) => total + houseConsignments.totalItems
  )

  val additionalSupplyChainActorsRole: Option[String] = AdditionalSupplyChainActor.map(
    _.map(_.toString).mkString("; ")
  )

  val additionalSupplyChainActorIdentificationNumbers: Option[String] = AdditionalSupplyChainActor.map {
    _.map(_.identificationNumber).mkString("; ")
  }

  val departureTransportMeans: Option[String] = DepartureTransportMeans.map(
    _.map(_.toString).mkString("; ")
  )

  val transportEquipment: Option[String] = TransportEquipment.map(
    _.map(_.toString).mkString("; ")
  )

  val seals: Option[String] = TransportEquipment.map(
    _.map(
      x => sealToString(x.Seal)
    ).mkString("; ")
  )

  val previousDocument: Option[String] = PreviousDocument.map(
    _.map(_.toString).mkString("; ")
  )

  val transportDocument: Option[String] = TransportDocument.map(
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
