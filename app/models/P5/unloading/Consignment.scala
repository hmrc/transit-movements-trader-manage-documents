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

import models.P5.RichSeqT
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

  val totalPackages: Int = HouseConsignment.total(_.totalPackages)

  val totalItems: Int = HouseConsignment.total(_.totalItems)

  val transportEquipment: String = TransportEquipment.showAll

  val departureTransportMeans: String = DepartureTransportMeans.showAll

  val seals: String = TransportEquipment.getOrElse(Nil).map(_.seals).showAll

  val previousDocument: String = PreviousDocument.showAll

  val supportingDocument: String = SupportingDocument.showAll

  val additionalInformation: String = AdditionalInformation.showAll

  val additionalReference: String = AdditionalReference.showAll

  val transportDocument: String = TransportDocument.showAll
}

object Consignment {
  implicit val formats: OFormat[Consignment] = Json.format[Consignment]
}
