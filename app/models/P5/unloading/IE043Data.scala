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

import play.api.libs.json.__
import play.api.libs.json.Json
import play.api.libs.json.OWrites
import play.api.libs.json.Reads

case class IE043Data(data: UnloadingMessageData) {

  val mrn: String = data.TransitOperation.MRN

  val declarationType: String = data.TransitOperation.declarationType.getOrElse("")

  val security: String = data.TransitOperation.security

  val customsOfficeOfDestination: String = data.CustomsOfficeOfDestinationActual.referenceNumber

  val tir: String = data.HolderOfTheTransitProcedure.TIRHolderIdentificationNumber.getOrElse("")

  val holderOfTransit: String = data.HolderOfTheTransitProcedure.toString

  val holderOfTransitID: String = data.HolderOfTheTransitProcedure.identificationNumber.getOrElse("")

  val totalPackages: Int = data.Consignment
    .map(
      _.totalPackages
    )
    .getOrElse(0)

  val totalItems: Int = data.Consignment
    .map(
      _.totalItems
    )
    .getOrElse(0)

  val countryOfDestination: String = data.Consignment.flatMap(_.countryOfDestination).getOrElse("")

  val totalGrossMass: Double = data.Consignment.flatMap(_.grossMass).getOrElse(0: Double)

  val departureTransportMeans: String = data.Consignment.flatMap(_.departureTransportMeans).getOrElse("")

  val inlandModeOfTransport: String = data.Consignment.flatMap(_.inlandModeOfTransport).getOrElse("")

  val consignorIdentificationNumber: String = data.Consignment
    .flatMap(
      _.Consignor
        .flatMap(
          _.identificationNumber
        )
    )
    .getOrElse("")

  val consignor: String = data.Consignment
    .flatMap(
      _.Consignor.map(_.toString)
    )
    .getOrElse("")

  val consigneeIdentificationNumber: String = data.Consignment
    .flatMap(
      _.Consignee
        .flatMap(
          _.identificationNumber
        )
    )
    .getOrElse("")

  val consignee: String = data.Consignment
    .flatMap(
      _.Consignee.map(_.toString)
    )
    .getOrElse("")

  val transportEquipment: String = data.Consignment
    .flatMap(
      _.transportEquipment
    )
    .getOrElse("")

  val container: String = data.Consignment.map(_.containerIndicator).getOrElse("")

  val seals: String = data.Consignment.flatMap(_.seals).getOrElse("")

  val previousDocument: String = data.Consignment.flatMap(_.previousDocument).getOrElse("")

  val supportingDocument: String = data.Consignment.flatMap(_.supportingDocument).getOrElse("")

  val transportDocument: String = data.Consignment.flatMap(_.transportDocument).getOrElse("")

  val additionalInformation: String = data.Consignment.flatMap(_.additionalInformation).getOrElse("")

  val additionalReference: String = data.Consignment.flatMap(_.additionalReference).getOrElse("")

}

object IE043Data {
  implicit val reads: Reads[IE043Data]    = (__ \ "body" \ "n1:CC043C").read[UnloadingMessageData].map(IE043Data.apply)
  implicit val writes: OWrites[IE043Data] = Json.writes[IE043Data]
}
