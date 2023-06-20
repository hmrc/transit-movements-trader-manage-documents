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

  val declarationType: String = data.TransitOperation.declarationType

  val security: String = data.TransitOperation.security

  val customOfficeOfDestination: String = data.CustomOfficeOfDestinationActual.referenceNumber

  val tir: String = data.HolderOfTheTransitProcedure.TIRHolderIdentificationNumber.getOrElse("")

  val holderOfTransitName: String = data.HolderOfTheTransitProcedure.name

  val holderOfTransitID: String = data.HolderOfTheTransitProcedure.identificationNumber.getOrElse("")

  val totalPackages: Int = data.Consignment.totalPackages

  val totalItems: Int = data.Consignment.totalItems

  val coountryOfDestination: String = data.Consignment.countryOfDestination.getOrElse("")

  val totalGrossMass: String = data.Consignment.grossMass.map(_.toString).getOrElse("")

  val inlandModeOfTransport: String = data.Consignment.inlandModeOfTransport.getOrElse("")

  val consignorIdentificationNumber: String = data.Consignment.Consignor
    .flatMap(
      _.identificationNumber
    )
    .getOrElse("")

  val consignorIdentificationName: String = data.Consignment.Consignor
    .flatMap(
      _.name
    )
    .getOrElse("")

  val consigneeIdentificationNumber: String = data.Consignment.Consignee
    .flatMap(
      _.identificationNumber
    )
    .getOrElse("")

  val consigneeIdentificationName: String = data.Consignment.Consignee
    .flatMap(
      _.name
    )
    .getOrElse("")

  val transportEquipment: String = data.Consignment.transportEquipment.getOrElse("")

  val transportEquipmentContainer: String = data.Consignment.transportEquipmentContainer.getOrElse("")

  val seals: String = data.Consignment.seals.getOrElse("")

  val previousDocument: String = data.Consignment.previousDocument.getOrElse("")

  val supportingDocument: String = data.Consignment.supportingDocument.getOrElse("")

  val transportDocument: String = data.Consignment.transportDocument.getOrElse("")

  val additionalInformation: String = data.Consignment.additionalInformation.getOrElse("")

  val additionalReference: String = data.Consignment.additionalReference.getOrElse("")
}

object IE043Data {
  implicit val reads: Reads[IE043Data]    = (__ \ "body" \ "n1:CC043C").read[UnloadingMessageData].map(IE043Data.apply)
  implicit val writes: OWrites[IE043Data] = Json.writes[IE043Data]
}
