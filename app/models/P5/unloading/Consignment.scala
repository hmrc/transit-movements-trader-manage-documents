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

package models.P5.unloading

import models.P5.unloading.TransportEquipment.sealToString
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class Consignment(
  countryOfDestination: Option[String],
  containerIndicator: String,
  inlandModeOfTransport: Option[String],
  grossMass: Option[Double],
  Consignor: Option[Consignor],
  Consignee: Option[Consignee],
  TransportEquipment: Option[List[TransportEquipment]],
  DepartureTransportMeans: Option[List[DepartureTransportMeans]],
  PreviousDocument: Option[List[PreviousDocument]],
  SupportingDocument: Option[List[SupportingDocument]],
  TransportDocument: Option[List[TransportDocument]],
  AdditionalReference: Option[List[AdditionalReference]],
  AdditionalInformation: Option[List[AdditionalInformation]],
  HouseConsignment: Seq[HouseConsignment]
) {

  val totalPackages: Int = HouseConsignment.foldLeft(0)(
    (total, houseConsignments) => total + houseConsignments.totalPackages
  )

  val totalItems: Int = HouseConsignment.foldLeft(0)(
    (total, houseConsignments) => total + houseConsignments.totalItems
  )

  val transportEquipment: Option[String] = TransportEquipment.map(
    _.map(_.toString).mkString("; ")
  )

  val transportEquipmentContainer: Option[String] = TransportEquipment.map(
    _.map(_.containerIdentificationNumber.getOrElse("")).mkString("; ")
  )

  val departureTransportMeans: Option[String] = DepartureTransportMeans.map(
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

  val supportingDocument: Option[String] = SupportingDocument.map(
    _.map(_.toString).mkString("; ")
  )

  val additionalInformation: Option[String] = AdditionalInformation.map(
    _.map(_.toString).mkString("; ")
  )

  val additionalReference: Option[String] = AdditionalReference.map(
    _.map(_.toString).mkString("; ")
  )

  val transportDocument: Option[String] = TransportDocument.map(
    _.map(_.toString).mkString("; ")
  )
}

object Consignment {
  implicit val formats: OFormat[Consignment] = Json.format[Consignment]
}
